package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*

enum class ReportPeriod {
    DAILY,
    MONTHLY,
    THREE_MONTHS,
    SIX_MONTHS,
    YEARLY
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    profile: UserProfile,
    logs: List<ActivityLog>,
    dailyHabitLogs: List<DailyHabitLog>,
    quizHistory: List<QuizDailyHistory>,
    completedSkills: List<SkillProjectEntity>,
    onBack: () -> Unit
) {
    var selectedPeriod by remember { mutableStateOf(ReportPeriod.DAILY) }

    val periodDays = when (selectedPeriod) {
        ReportPeriod.DAILY -> 1
        ReportPeriod.MONTHLY -> 30
        ReportPeriod.THREE_MONTHS -> 90
        ReportPeriod.SIX_MONTHS -> 180
        ReportPeriod.YEARLY -> 365
    }

    val cutoffTime = remember(selectedPeriod) {
        System.currentTimeMillis() - (periodDays.toLong() * 24L * 60L * 60L * 1000L)
    }

    val filteredLogs = remember(selectedPeriod, logs) {
        if (selectedPeriod == ReportPeriod.DAILY) {
            logs.take(20)
        } else {
            logs.filter { it.timestamp >= cutoffTime }
        }
    }

    val positiveHabitsApproved = remember(selectedPeriod, dailyHabitLogs) {
        dailyHabitLogs.filter { (it.status == "APPROVED") && !it.isNegative && (selectedPeriod == ReportPeriod.DAILY || it.completedAt >= cutoffTime) }.size
    }

    val demeritCount = remember(selectedPeriod, dailyHabitLogs) {
        dailyHabitLogs.filter { (it.isNegative || it.status == "DEMERIT") && (selectedPeriod == ReportPeriod.DAILY || it.completedAt >= cutoffTime) }.size
    }

    val totalEarnedInPeriod = remember(selectedPeriod, filteredLogs) {
        filteredLogs.filter { it.coinsChanged > 0 }.sumOf { it.coinsChanged }
    }

    val smartChoicesCount = remember(selectedPeriod, filteredLogs) {
        filteredLogs.count { it.type == "SMART_CHOICE" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📊 Progress & Growth Reports", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("reports_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Period selector tabs
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        ReportPeriod.DAILY to "Daily",
                        ReportPeriod.MONTHLY to "1 Month",
                        ReportPeriod.THREE_MONTHS to "3 Mo",
                        ReportPeriod.SIX_MONTHS to "6 Mo",
                        ReportPeriod.YEARLY to "1 Year"
                    ).forEach { (period, label) ->
                        val isSelected = selectedPeriod == period
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPeriod = period },
                            label = { Text(label, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Summary Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF334155))
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "✨ Report Card (${selectedPeriod.name.replace('_', ' ')})",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Surface(
                                    color = Color(0xFF10B981),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        profile.levelTitle,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Coins Gained", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                    Text("+$totalEarnedInPeriod 🪙", color = Color(0xFFFBBF24), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Column {
                                    Text("Habits Mastered", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                    Text("$positiveHabitsApproved ✅", color = Color(0xFF86EFAC), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                                }
                                Column {
                                    Text("Smart Choices", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                    Text("$smartChoicesCount ⭐", color = Color(0xFF67E8F9), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }

            // Key Pillar Metrics Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Comprehensive Performance Metrics", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReportMetricCard(
                            title = "Positive Habits",
                            value = "$positiveHabitsApproved Completed",
                            subtitle = "Discipline & daily consistency",
                            icon = "🎒",
                            color = Color(0xFFDCFCE7),
                            modifier = Modifier.weight(1f)
                        )
                        ReportMetricCard(
                            title = "Demerits Logged",
                            value = "$demeritCount Handled",
                            subtitle = "Negative habits addressed",
                            icon = "⚠️",
                            color = Color(0xFFFEE2E2),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ReportMetricCard(
                            title = "12% Savings Compounding",
                            value = "${profile.saveJarCoins + profile.bankCoins} Coins",
                            subtitle = "Growing at 12% per year",
                            icon = "📈",
                            color = Color(0xFFE0E7FF),
                            modifier = Modifier.weight(1f)
                        )
                        ReportMetricCard(
                            title = "Skills & Projects",
                            value = "${completedSkills.size} Accomplished",
                            subtitle = "Real entrepreneurial value",
                            icon = "🎨",
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Recent Activity Feed
            item {
                Text("Chronological Activity Feed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (filteredLogs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No logs recorded yet for this period.", color = Color.Gray)
                    }
                }
            } else {
                items(filteredLogs.take(15)) { log ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(
                                    when (log.type) {
                                        "EARN" -> "🪙"
                                        "SPEND" -> "🛍️"
                                        "SAVE" -> "🐷"
                                        "INVEST" -> "📈"
                                        "SMART_CHOICE" -> "⭐"
                                        "DEMERIT" -> "⚠️"
                                        "QUIZ" -> "🧠"
                                        else -> "📝"
                                    },
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(log.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(log.description, fontSize = 11.sp, color = Color.Gray, maxLines = 1)
                                }
                            }

                            if (log.coinsChanged != 0) {
                                Text(
                                    if (log.coinsChanged > 0) "+${log.coinsChanged}c" else "${log.coinsChanged}c",
                                    color = if (log.coinsChanged > 0) Color(0xFF15803D) else Color(0xFFDC2626),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Text(value, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            Text(subtitle, fontSize = 11.sp, color = Color.DarkGray.copy(alpha = 0.8f))
        }
    }
}
