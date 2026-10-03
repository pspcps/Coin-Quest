package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.standardTextFieldColors
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestSipScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onUpdateSip: (amount: Int, frequency: String, isActive: Boolean) -> Unit,
    onProcessMonthlyGrowth: () -> Unit
) {
    var showSipDialog by remember { mutableStateOf(false) }
    var sipAmountInput by remember(profile.sipAmount) { mutableStateOf(profile.sipAmount.toString()) }
    var sipFrequency by remember(profile.sipFrequency) { mutableStateOf(profile.sipFrequency) }
    var isSipActive by remember(profile.isSipActive) { mutableStateOf(profile.isSipActive) }

    val totalSavings = profile.saveJarCoins + profile.bankCoins
    val monthlyGrowthEst = (totalSavings * 0.01f).toInt().coerceAtLeast(if (totalSavings >= 10) 1 else 0)
    val yearlyGrowthEst = (totalSavings * 0.12f).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📈 12% Wealth & SIP Growth", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("invest_sip_back_btn")
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
            // Hero Growth Card
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E3A8A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF1E3A8A), Color(0xFF2563EB), Color(0xFF0D9488))
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
                                    "✨ 12% Annual Growth Engine",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "1% Every Month",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text(
                                        "Total Compounding Base",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        "$totalSavings Coins",
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        "🐷 Save Jar: ${profile.saveJarCoins}c | 🏦 Vault: ${profile.bankCoins}c",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 12.sp
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        "Next Month Est.",
                                        color = Color(0xFF86EFAC),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        "+$monthlyGrowthEst coins",
                                        color = Color(0xFF86EFAC),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Yearly ~ +$yearlyGrowthEst coins",
                                        color = Color.White.copy(alpha = 0.75f),
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Button(
                                onClick = onProcessMonthlyGrowth,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF10B981)
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("apply_monthly_growth_btn")
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("⚡ Compound 1-Month Growth Now!", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // SIP Section
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🔄", fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        "Systematic Investment Plan (SIP)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        if (profile.isSipActive) "Active: ${profile.sipAmount} coins ${profile.sipFrequency.lowercase()}"
                                        else "SIP is currently Paused",
                                        color = if (profile.isSipActive) Color(0xFF15803D) else Color.Gray,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showSipDialog = true },
                                modifier = Modifier.testTag("edit_sip_config_btn")
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = "Config SIP", tint = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Text(
                            "A SIP automatically puts a fixed number of earned coins into your Save Jar on a regular schedule. It builds lifelong saving habits effortlessly without needing to think about it!",
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showSipDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(if (profile.isSipActive) "Adjust SIP" else "Start SIP")
                            }

                            if (profile.isSipActive) {
                                Button(
                                    onClick = {
                                        onUpdateSip(profile.sipAmount, profile.sipFrequency, false)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Pause")
                                }
                            }
                        }
                    }
                }
            }

            // 1 Year Compounding Growth Visualizer Table
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "📊 12-Month Compounding Projection",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Watch how 12% yearly compounding turns consistent savings into great wealth month by month:",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        val months = listOf(1, 3, 6, 9, 12)
                        months.forEach { m ->
                            val compoundedVal = (totalSavings * (1 + 0.01).pow(m.toDouble())).toInt() +
                                    (if (profile.isSipActive) (profile.sipAmount * m) else 0)
                            val gain = compoundedVal - totalSavings

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (m == 12) Color(0xFFDCFCE7)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Month $m",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    if (m == 12) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("🏆 1 Full Year", fontSize = 11.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "$compoundedVal coins",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        "(+$gain)",
                                        color = Color(0xFF15803D),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Financial Wisdom Nugget
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Benny's Compounding Rule",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                "12% per year means your money earns money, and then THAT money earns more money! The longer you wait, the faster it snowballs!",
                                fontSize = 12.sp,
                                color = Color(0xFF78350F),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

    // Configure SIP Dialog
    if (showSipDialog) {
        AlertDialog(
            onDismissRequest = { showSipDialog = false },
            containerColor = Color.White,
            title = { Text("⚙️ Configure SIP & Auto-Save", fontWeight = FontWeight.Bold, color = Color(0xFF1C1B1F)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Set how many coins you want automatically allocated to your Save Jar periodically:",
                        fontSize = 13.sp,
                        color = Color(0xFF49454F)
                    )

                    OutlinedTextField(
                        value = sipAmountInput,
                        onValueChange = { sipAmountInput = it.filter { c -> c.isDigit() } },
                        label = { Text("SIP Amount (Coins)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Frequency:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("MONTHLY", "WEEKLY").forEach { freq ->
                            FilterChip(
                                selected = sipFrequency == freq,
                                onClick = { sipFrequency = freq },
                                label = { Text(if (freq == "MONTHLY") "🗓️ Monthly" else "📅 Weekly") }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Active Status:", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Switch(
                            checked = isSipActive,
                            onCheckedChange = { isSipActive = it }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = sipAmountInput.toIntOrNull() ?: 5
                        onUpdateSip(amt, sipFrequency, isSipActive)
                        showSipDialog = false
                    }
                ) {
                    Text("Save Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSipDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
