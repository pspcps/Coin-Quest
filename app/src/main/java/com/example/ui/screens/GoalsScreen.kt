package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.data.model.GoalItem
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    profile: UserProfile,
    goals: List<GoalItem>,
    onBack: () -> Unit,
    onSetActiveGoal: (Int) -> Unit,
    onCreateGoal: (String, String, String, Int) -> Unit,
    onDeleteGoal: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var goalToDelete by remember { mutableStateOf<GoalItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dream Goals 🎯",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("goals_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    Button(
                        onClick = { showCreateDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 12.dp).testTag("add_custom_goal_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Goal", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Goal", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp)
        ) {
            item {
                BennySpeechBubble(
                    text = "A Goal is a special dream you choose to save for! Whenever you add coins to your 🐷 Save Jar, your goal gets closer and closer! 🏰🚲"
                )
            }

            item {
                Text(
                    text = "YOUR DREAM GOALS (${goals.size})",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = BoldTextSecondary,
                    letterSpacing = 0.8.sp
                )
            }

            if (goals.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BoldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text("🎯", fontSize = 38.sp)
                            Text("No Dream Goals Yet!", fontWeight = FontWeight.Black, fontSize = 16.sp, color = BoldTextPrimary)
                            Text("Add a dream goal like a new book, toy, bicycle, or gift to start saving coins towards it.", fontSize = 12.sp, color = BoldTextSecondary, textAlign = TextAlign.Center)
                            Button(
                                onClick = { showCreateDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                            ) {
                                Text("+ Create First Goal", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(goals) { goal ->
                    val isActive = profile.activeGoalId == goal.id
                    GoalCard(
                        goal = goal,
                        isActive = isActive,
                        onSelect = { onSetActiveGoal(goal.id) },
                        onDelete = { goalToDelete = goal }
                    )
                }
            }
        }
    }

    goalToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { goalToDelete = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Text("Delete Goal 🗑️", fontWeight = FontWeight.Black, color = SpendOrangeDark)
            },
            text = {
                Text(
                    "Are you sure you want to delete '${target.title}'? Any saved progress on this goal will be removed.",
                    color = Color(0xFF1C1B1F)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteGoal(target.id)
                        goalToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { goalToDelete = null }) {
                    Text("Cancel", color = BoldTextSecondary)
                }
            }
        )
    }

    if (showCreateDialog) {
        var goalTitle by remember { mutableStateOf("") }
        var goalCost by remember { mutableStateOf("25") }
        var selectedIcon by remember { mutableStateOf("🚀") }
        val iconOptions = listOf("🚀", "🏰", "🚲", "🐶", "🎨", "⛺", "🎸", "🧸")

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            title = {
                Text(
                    text = "🎯 Set a New Dream Goal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1C1B1F)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Choose an emoji symbol:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = BoldTextSecondary
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        iconOptions.forEach { icon ->
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedIcon == icon) CoinGoldLight else BoldSurfaceVariant)
                                    .border(1.5.dp, if (selectedIcon == icon) CoinGoldDark else BoldBorder, CircleShape)
                                    .clickable { selectedIcon = icon },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = icon, fontSize = 20.sp)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = goalTitle,
                        onValueChange = { goalTitle = it },
                        label = { Text("What are you saving for?") },
                        placeholder = { Text("e.g. Treehouse Tent") },
                        shape = RoundedCornerShape(14.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth().testTag("custom_goal_title_input")
                    )

                    OutlinedTextField(
                        value = goalCost,
                        onValueChange = { goalCost = it },
                        label = { Text("Target Coins 🪙") },
                        placeholder = { Text("e.g. 30") },
                        shape = RoundedCornerShape(14.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth().testTag("custom_goal_cost_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cost = goalCost.toIntOrNull() ?: 20
                        if (goalTitle.isNotBlank()) {
                            onCreateGoal(goalTitle.trim(), selectedIcon, "Custom family dream goal", cost)
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Dream Goal! 🌟", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                }
            }
        )
    }
}

@Composable
fun GoalCard(
    goal: GoalItem,
    isActive: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (goal.targetCost > 0) {
        (goal.currentCoins.toFloat() / goal.targetCost.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val cardBg = if (goal.isCompleted) GardenGreenMint else BoldSurface
    val borderCol = if (isActive) CoinGoldDark else if (goal.isCompleted) GardenGreenLight else BoldBorder

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onSelect() }
            .testTag("goal_card_${goal.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(if (isActive) 2.5.dp else 1.5.dp, borderCol),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (goal.isCompleted) Color.White else BoldSurfaceVariant)
                            .border(1.dp, BoldBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = goal.icon, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = goal.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = BoldTextPrimary
                            )
                            if (isActive) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(CoinGoldDark)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Text(
                            text = goal.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = BoldTextSecondary,
                            maxLines = 1
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${goal.currentCoins} / ${goal.targetCost} 🪙",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Black,
                        color = if (goal.isCompleted) GardenGreenDark else CoinGoldDark
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp).testTag("delete_goal_btn_${goal.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Goal",
                            tint = BoldTextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = if (goal.isCompleted) GardenGreen else CoinGoldDark,
                trackColor = BoldSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (goal.isCompleted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Completed", tint = GardenGreen)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🎉 Goal Achieved! You waited & succeeded!",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = GardenGreenDark
                        )
                    }
                } else {
                    Text(
                        text = "${(goal.targetCost - goal.currentCoins).coerceAtLeast(0)} more coins to reach!",
                        style = MaterialTheme.typography.labelSmall,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    if (!isActive) {
                        TextButton(onClick = onSelect) {
                            Text("Set as Active Goal 🎯", style = MaterialTheme.typography.labelMedium, color = SaveBlueDark, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

