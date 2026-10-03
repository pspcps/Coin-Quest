package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.GoalItem
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@Composable
fun TopAdventureBar(
    profile: UserProfile,
    allProfiles: List<UserProfile> = emptyList(),
    onParentClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onSwitchProfile: ((Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showQuickSwitchDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = BoldSurface,
        shadowElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BoldBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile & Level with bold typography & quick switch indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        if (allProfiles.size > 1 && onSwitchProfile != null) {
                            showQuickSwitchDialog = true
                        } else {
                            onAvatarClick()
                        }
                    }
                    .padding(4.dp)
                    .testTag("top_profile_avatar_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(CoinGoldLight, CoinGold)
                            )
                        )
                        .border(1.5.dp, CoinGoldDark.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = profile.avatar, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f, fill = false)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = profile.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        if (allProfiles.size > 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "▾",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = BoldTextSecondary
                            )
                        }
                    }
                    Text(
                        text = "AGE ${profile.childAge} • ${profile.levelTitle.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = GardenGreenDark,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Coins Total & Parent Lock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val totalInWallet = profile.unassignedCoins + profile.spendJarCoins + profile.saveJarCoins + profile.giveJarCoins + profile.safetyJarCoins + profile.bankCoins

                CoinCounterPill(
                    coins = totalInWallet,
                    label = "Total",
                    modifier = Modifier.testTag("top_coins_counter")
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BunnyPurpleBg)
                        .border(1.5.dp, BunnyPurpleLight, CircleShape)
                        .clickable(onClick = onParentClick)
                        .testTag("parent_dashboard_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Parent Dashboard",
                        tint = BunnyPurpleDark,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    if (showQuickSwitchDialog && onSwitchProfile != null) {
        QuickSwitchChildDialog(
            currentProfile = profile,
            allProfiles = allProfiles,
            onDismiss = { showQuickSwitchDialog = false },
            onSelectProfile = { selectedId ->
                onSwitchProfile(selectedId)
                showQuickSwitchDialog = false
            },
            onOpenParentHub = {
                showQuickSwitchDialog = false
                onParentClick()
            }
        )
    }
}

@Composable
fun QuickSwitchChildDialog(
    currentProfile: UserProfile,
    allProfiles: List<UserProfile>,
    onDismiss: () -> Unit,
    onSelectProfile: (Int) -> Unit,
    onOpenParentHub: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = BoldSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👨‍👩‍👧", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Switch Child Profile", fontWeight = FontWeight.Black, color = BoldTextPrimary)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                allProfiles.forEach { kid ->
                    val isCurrent = kid.id == currentProfile.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onSelectProfile(kid.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) BunnyPurpleLight else BoldSurfaceVariant
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isCurrent) 2.dp else 1.dp,
                            if (isCurrent) BunnyPurpleDark else BoldBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(kid.avatar, fontSize = 26.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(kid.name, fontWeight = FontWeight.Black, fontSize = 15.sp, color = BoldTextPrimary)
                                    Text("Age ${kid.childAge} • ${kid.totalCoinsEarned} lifetime coins", fontSize = 11.sp, color = BoldTextSecondary)
                                }
                            }
                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BunnyPurpleDark)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("ACTIVE", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onOpenParentHub,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark)
            ) {
                Text("Manage Profiles in Parent Hub ⚙️", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = BoldTextSecondary)
            }
        }
    )
}

@Composable
fun CoinCounterPill(
    coins: Int,
    label: String = "Coins",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(CoinGold, CoinGoldDark)
                )
            )
            .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "🪙", fontSize = 16.sp)
            Text(
                text = "$coins",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.2).sp
            )
        }
    }
}

@Composable
fun BennySpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    bunnyEmoji: String = "🐰"
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BunnyPurpleBg),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BunnyPurpleLight),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, BunnyPurple.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = bunnyEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "BENNY THE BANK BUNNY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = BunnyPurpleDark,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BoldTextPrimary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun CelebrationDialog(
    coins: Int,
    message: String,
    onDismiss: () -> Unit
) {
    val scale = remember { Animatable(0.4f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .scale(scale.value)
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(28.dp)),
            colors = CardDefaults.cardColors(containerColor = BoldSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, CoinGoldLight),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(CoinGoldBg, BoldSurface)
                        )
                    )
                    .padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 ⭐ 🪙 ⭐ 🎉",
                    fontSize = 26.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "HOORAY!",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = CoinGoldDark,
                    letterSpacing = (-0.5).sp
                )

                if (coins > 0) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(CoinGoldLight, CoinGoldDark)
                                )
                            )
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+$coins 🪙",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = BoldTextPrimary,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("celebration_continue_btn"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                ) {
                    Text(
                        text = "Awesome! 🌟",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun DailyCoinSummaryDialog(
    unassignedCoins: Int,
    earnedToday: Int,
    onDismiss: () -> Unit,
    onDistribute: (spend: Int, save: Int, give: Int, safety: Int) -> Unit
) {
    var spendVal by remember { mutableIntStateOf(0) }
    var saveVal by remember { mutableIntStateOf(0) }
    var giveVal by remember { mutableIntStateOf(0) }
    var safetyVal by remember { mutableIntStateOf(0) }

    val remainingToAssign = unassignedCoins - (spendVal + saveVal + giveVal + safetyVal)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = BoldSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "DAILY COIN SUMMARY",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "4-Jar Magic Allocation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreenLight)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Approved Today", style = MaterialTheme.typography.labelSmall, color = GardenGreenDark)
                            Text("+$earnedToday 🪙", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = GardenGreenDark)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ready to Sort", style = MaterialTheme.typography.labelSmall, color = CoinGoldDark)
                            Text("$remainingToAssign 🪙", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = CoinGoldDark)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You earned these coins! Now you decide where they go:",
                    style = MaterialTheme.typography.bodySmall,
                    color = BoldTextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Jar Sliders / Steppers
                JarAllocationRow(
                    emoji = "🎮",
                    title = "Spend Jar",
                    coins = spendVal,
                    onAdd = { if (remainingToAssign > 0) spendVal++ },
                    onSub = { if (spendVal > 0) spendVal-- },
                    color = SpendOrangeDark,
                    bgColor = SpendOrangeBg
                )

                JarAllocationRow(
                    emoji = "🐷",
                    title = "Save Jar",
                    coins = saveVal,
                    onAdd = { if (remainingToAssign > 0) saveVal++ },
                    onSub = { if (saveVal > 0) saveVal-- },
                    color = SaveBlueDark,
                    bgColor = SaveBlueBg
                )

                JarAllocationRow(
                    emoji = "❤️",
                    title = "Give Jar",
                    coins = giveVal,
                    onAdd = { if (remainingToAssign > 0) giveVal++ },
                    onSub = { if (giveVal > 0) giveVal-- },
                    color = GiveHeartPinkDark,
                    bgColor = GiveHeartPinkBg
                )

                JarAllocationRow(
                    emoji = "🛟",
                    title = "Safety Jar",
                    coins = safetyVal,
                    onAdd = { if (remainingToAssign > 0) safetyVal++ },
                    onSub = { if (safetyVal > 0) safetyVal-- },
                    color = SafetyTealDark,
                    bgColor = SafetyTealBg
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val total = spendVal + saveVal + giveVal + safetyVal
                        if (total > 0) {
                            onDistribute(spendVal, saveVal, giveVal, safetyVal)
                        }
                    },
                    enabled = (spendVal + saveVal + giveVal + safetyVal) > 0,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_jar_allocation_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                ) {
                    Text(
                        text = "Seal Coins in Jars! ✨",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun JarAllocationRow(
    emoji: String,
    title: String,
    coins: Int,
    onAdd: () -> Unit,
    onSub: () -> Unit,
    color: Color,
    bgColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onSub,
                    modifier = Modifier.size(32.dp),
                    enabled = coins > 0
                ) {
                    Text("-", fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
                }

                Text(
                    text = "$coins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = color,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )

                IconButton(
                    onClick = onAdd,
                    modifier = Modifier.size(32.dp)
                ) {
                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
                }
            }
        }
    }
}

@Composable
fun MoneyJourneyDialog(
    profile: UserProfile,
    goals: List<GoalItem>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = BoldSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(profile.avatar, fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "MONEY JOURNEY MAP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = GardenGreenDark
                            )
                            Text(
                                text = "${profile.name}'s World",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = BoldTextPrimary
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("🏆 Adventure Stats", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = CoinGoldDark)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Total Coins Earned Lifetime: ${profile.totalCoinsEarned} 🪙", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = BoldTextPrimary)
                                Text("Smart Choice Badge: ${profile.smartChoiceBadge}", style = MaterialTheme.typography.bodyMedium, color = BoldTextSecondary)
                                Text("Smart XP: ${profile.smartChoiceXp} XP", style = MaterialTheme.typography.bodySmall, color = BoldTextMuted)
                            }
                        }
                    }

                    item {
                        Text(
                            text = "🎯 Dream Goals Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary
                        )
                    }

                    items(goals) { goal ->
                        val pct = if (goal.targetCost > 0) (goal.currentCoins.toFloat() / goal.targetCost).coerceIn(0f, 1f) else 0f
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = BoldSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BoldBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(goal.icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(goal.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Black, color = BoldTextPrimary)
                                    Text("${goal.currentCoins} / ${goal.targetCost} coins", style = MaterialTheme.typography.bodySmall, color = BoldTextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { pct },
                                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                        color = SaveBlueDark,
                                        trackColor = BoldBorder
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreenLight)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("💡 Golden Money Rules", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Money is limited — we make choices with it.", style = MaterialTheme.typography.bodySmall, color = BoldTextPrimary)
                                Text("• Spend Less on wants leaves more for big dreams.", style = MaterialTheme.typography.bodySmall, color = BoldTextPrimary)
                                Text("• Planting seeds in the garden compounds your wealth over time.", style = MaterialTheme.typography.bodySmall, color = BoldTextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}
