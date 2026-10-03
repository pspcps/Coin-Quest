package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyHabitLog
import com.example.data.model.HabitItem
import com.example.data.model.RecoveryQuest
import com.example.data.model.SampleGameData
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdventuresHabitsScreen(
    profile: UserProfile,
    habits: List<HabitItem>,
    dailyLogs: List<DailyHabitLog>,
    todayDateString: String,
    onBack: () -> Unit,
    onLogHabit: (HabitItem) -> Unit,
    onCompleteRecoveryQuest: (RecoveryQuest) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Daily Habits, 1 = Recovery Quests
    val recoveryQuests = SampleGameData.recoveryQuests

    val todayCompletedHabitIds = remember(dailyLogs, todayDateString) {
        dailyLogs.filter { it.dateString == todayDateString }.map { it.habitId }.toSet()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Adventures & Habits 🎒",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("habits_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GardenGreenMint)
                            .border(1.dp, GardenGreenLight, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🔥 ${profile.streakDays} Day Streak",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = GardenGreenDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BoldSurface,
                    titleContentColor = BoldTextPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(BoldCanvas)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp)
        ) {
            item {
                BennySpeechBubble(
                    text = "Doing small good things every day builds your superhero power and earns shiny coins! If you miss a task, don't worry — hero recovery quests help you bounce right back! 🌟"
                )
            }

            // Tab Selector
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BoldSurfaceVariant)
                        .padding(4.dp)
                ) {
                    TabPill(
                        title = "Daily Habits (${habits.size})",
                        icon = "🎒",
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f).testTag("tab_daily_habits")
                    )
                    TabPill(
                        title = "Recovery Quests",
                        icon = "⚡",
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f).testTag("tab_recovery_quests")
                    )
                }
            }

            if (selectedTab == 0) {
                item {
                    Text(
                        text = "TODAY'S DAILY ROUTINE",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                items(habits) { habit ->
                    val isDoneToday = todayCompletedHabitIds.contains(habit.id)
                    val statusLog = dailyLogs.find { it.habitId == habit.id && it.dateString == todayDateString }

                    HabitCard(
                        habit = habit,
                        isCompletedToday = isDoneToday,
                        statusLog = statusLog,
                        onComplete = { onLogHabit(habit) }
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "💡 TEAMWORK & FAMILY RESPONSIBILITY",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = CoinGoldDark,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Family chores (like clearing your plate) have no coin reward because taking care of our home is teamwork we do with love for each other! ❤️",
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldTextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            } else {
                item {
                    Text(
                        text = "HERO RECOVERY QUESTS (EARN & BOUNCE BACK)",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                items(recoveryQuests) { quest ->
                    RecoveryQuestCard(
                        quest = quest,
                        onComplete = { onCompleteRecoveryQuest(quest) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TabPill(
    title: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) BoldSurface else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                color = if (isSelected) BoldTextPrimary else BoldTextSecondary
            )
        }
    }
}

@Composable
fun HabitCard(
    habit: HabitItem,
    isCompletedToday: Boolean,
    statusLog: DailyHabitLog?,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompletedToday) BoldSurfaceVariant.copy(alpha = 0.7f) else BoldSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isCompletedToday) GardenGreenLight else BoldBorder
        ),
        elevation = CardDefaults.cardElevation(if (isCompletedToday) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (habit.isFamilyTeamwork) GiveHeartPinkBg else CoinGoldBg)
                        .border(1.dp, if (habit.isFamilyTeamwork) GiveHeartPinkLight else CoinGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = habit.icon, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (habit.isFamilyTeamwork) {
                            Text(
                                text = "❤️ Family Teamwork",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = GiveHeartPinkDark
                            )
                        } else {
                            Text(
                                text = "+${habit.rewardCoins} 🪙",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                color = CoinGoldDark
                            )
                            Text(
                                text = "• ${habit.category}",
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldTextSecondary
                            )
                        }
                    }
                    if (habit.note.isNotEmpty()) {
                        Text(
                            text = habit.note,
                            style = MaterialTheme.typography.bodySmall,
                            color = BoldTextMuted,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (isCompletedToday) {
                val isPending = statusLog?.status == "PENDING_APPROVAL"
                val statusText = when (statusLog?.status) {
                    "PENDING_APPROVAL" -> "⏳ Under Parent Review"
                    "APPROVED" -> "Approved! +${habit.rewardCoins} 🪙"
                    "MISSED" -> "Missed ⚠️"
                    else -> "Done! ✅"
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isPending) CoinGoldBg else GardenGreenMint)
                        .border(1.dp, if (isPending) CoinGoldLight else GardenGreenLight, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = if (isPending) CoinGoldDark else GardenGreenDark
                    )
                }
            } else {
                Button(
                    onClick = onComplete,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (habit.isFamilyTeamwork) GiveHeartPinkDark else GardenGreenDark
                    ),
                    modifier = Modifier.testTag("complete_habit_${habit.id}")
                ) {
                    Text(
                        text = if (habit.confirmationMethod == "INSTANT") "I Did It! ✨" else "Submit for Review 🌟",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun RecoveryQuestCard(
    quest: RecoveryQuest,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = BoldSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GardenGreenLight),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GardenGreenMint),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = quest.icon, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = quest.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = quest.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = BoldTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onComplete,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                modifier = Modifier.testTag("complete_quest_${quest.id}")
            ) {
                Text(
                    text = "+${quest.rewardCoins} 🪙",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        }
    }
}
