package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
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
import com.example.data.model.SampleGameData
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JarsScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onDistribute: (Int, Int, Int, Int, (Boolean) -> Unit) -> Unit,
    onDonate: (Int, String, String, (Boolean) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var spendAlloc by remember { mutableIntStateOf(0) }
    var saveAlloc by remember { mutableIntStateOf(0) }
    var giveAlloc by remember { mutableIntStateOf(0) }
    var safetyAlloc by remember { mutableIntStateOf(0) }

    val unassigned = profile.unassignedCoins
    val totalAllocated = spendAlloc + saveAlloc + giveAlloc + safetyAlloc
    val remainingToAssign = unassigned - totalAllocated

    var showDonateDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "4 Magical Jars 🐷",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("jars_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
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
                    text = "Every time you earn coins, decide: What do you want your money to do? Spend a little, save for dreams, help a friend, and keep a safety shield! 🌟"
                )
            }

            // Coin Distribution Box if there are unassigned coins
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp)),
                    colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, CoinGoldLight),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "SORT YOUR NEW COINS",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = CoinGoldDark,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (unassigned > 0) "You have $unassigned coins to sort!" else "All coins sorted! (+ / - to adjust)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = BoldTextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (remainingToAssign >= 0) CoinGoldDark else Color.Red)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Left: $remainingToAssign 🪙",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Controls for each jar
                        JarAllocationRow(
                            name = "SPEND (Fun Now)",
                            icon = "🎮",
                            count = spendAlloc,
                            color = SpendOrangeDark,
                            bgColor = SpendOrangeBg,
                            borderColor = SpendOrangeLight,
                            onMinus = { if (spendAlloc > 0) spendAlloc-- },
                            onPlus = { if (remainingToAssign > 0) spendAlloc++ }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        JarAllocationRow(
                            name = "SAVE (Big Dreams)",
                            icon = "🐷",
                            count = saveAlloc,
                            color = SaveBlueDark,
                            bgColor = SaveBlueBg,
                            borderColor = SaveBlueLight,
                            onMinus = { if (saveAlloc > 0) saveAlloc-- },
                            onPlus = { if (remainingToAssign > 0) saveAlloc++ }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        JarAllocationRow(
                            name = "GIVE (Helping)",
                            icon = "❤️",
                            count = giveAlloc,
                            color = GiveHeartPinkDark,
                            bgColor = GiveHeartPinkBg,
                            borderColor = GiveHeartPinkLight,
                            onMinus = { if (giveAlloc > 0) giveAlloc-- },
                            onPlus = { if (remainingToAssign > 0) giveAlloc++ }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        JarAllocationRow(
                            name = "SAFETY (Shield)",
                            icon = "🛟",
                            count = safetyAlloc,
                            color = SafetyTealDark,
                            bgColor = SafetyTealBg,
                            borderColor = SafetyTealLight,
                            onMinus = { if (safetyAlloc > 0) safetyAlloc-- },
                            onPlus = { if (remainingToAssign > 0) safetyAlloc++ }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                onDistribute(spendAlloc, saveAlloc, giveAlloc, safetyAlloc) { success ->
                                    if (success) {
                                        spendAlloc = 0
                                        saveAlloc = 0
                                        giveAlloc = 0
                                        safetyAlloc = 0
                                    }
                                }
                            },
                            enabled = totalAllocated > 0 && remainingToAssign >= 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("confirm_jar_sort_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                        ) {
                            Text(
                                text = "Put Coins into Jars! 🪙✨",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Your Magic Jars in Detail",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = BoldTextPrimary,
                    letterSpacing = (-0.2).sp
                )
            }

            // Jar 1: SPEND
            item {
                DetailedJarCard(
                    title = "🎮 SPEND JAR",
                    subtitle = "For things you want to enjoy today (snacks, small toys, books).",
                    coins = profile.spendJarCoins,
                    accentColor = SpendOrangeDark,
                    bgColor = SpendOrangeBg,
                    borderColor = SpendOrangeLight,
                    consequence = "You have ${profile.spendJarCoins} coins ready to spend at the Toy Market!"
                )
            }

            // Jar 2: SAVE
            item {
                DetailedJarCard(
                    title = "🐷 SAVE JAR",
                    subtitle = "For big dreams (castle, bicycle, puppy adventure).",
                    coins = profile.saveJarCoins,
                    accentColor = SaveBlueDark,
                    bgColor = SaveBlueBg,
                    borderColor = SaveBlueLight,
                    consequence = "Your savings are growing! Delayed gratification turns coins into big dreams!"
                )
            }

            // Jar 3: GIVE
            item {
                DetailedJarCard(
                    title = "❤️ GIVE JAR",
                    subtitle = "For sharing with friends, animal rescue, and village kindness.",
                    coins = profile.giveJarCoins,
                    accentColor = GiveHeartPinkDark,
                    bgColor = GiveHeartPinkBg,
                    borderColor = GiveHeartPinkLight,
                    consequence = "Sharing creates happiness! Tap below to donate to a village cause.",
                    actionButton = {
                        Button(
                            onClick = { showDonateDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = GiveHeartPinkDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = "Share", tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share / Donate ❤️", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
                        }
                    }
                )
            }

            // Jar 4: SAFETY
            item {
                DetailedJarCard(
                    title = "🛟 SAFETY JAR",
                    subtitle = "The emergency fund shield! Keeps you calm when surprises happen.",
                    coins = profile.safetyJarCoins,
                    accentColor = SafetyTealDark,
                    bgColor = SafetyTealBg,
                    borderColor = SafetyTealLight,
                    consequence = "You have ${profile.safetyJarCoins} coins ready to protect against unexpected life events!"
                )
            }
        }
    }

    if (showDonateDialog) {
        AlertDialog(
            onDismissRequest = { showDonateDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = BoldSurface,
            title = {
                Text(
                    text = "❤️ Village Giving Center",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = BoldTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "You have ${profile.giveJarCoins} coins in your Give Jar. Choose where you want to help:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    SampleGameData.givingCauses.forEach { cause ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (profile.giveJarCoins >= 2) {
                                        onDonate(2, cause.name, cause.description) {
                                             showDonateDialog = false
                                        }
                                    }
                                },
                            colors = CardDefaults.cardColors(containerColor = GiveHeartPinkBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GiveHeartPinkLight),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = cause.icon, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = cause.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Black,
                                        color = GiveHeartPinkDark
                                    )
                                    Text(
                                        text = cause.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BoldTextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Give 2 Coins 🪙",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = CoinGoldDark
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDonateDialog = false }) {
                    Text("Close", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                }
            }
        )
    }
}

@Composable
fun JarAllocationRow(
    name: String,
    icon: String,
    count: Int,
    color: Color,
    bgColor: Color,
    borderColor: Color,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BoldSurface)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = color
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onMinus,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(BoldSurfaceVariant)
                    .border(1.dp, BoldBorder, CircleShape)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Minus", tint = BoldTextPrimary, modifier = Modifier.size(16.dp))
            }

            Text(
                text = "$count 🪙",
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = BoldTextPrimary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            IconButton(
                onClick = onPlus,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(bgColor)
                    .border(1.dp, borderColor, CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Plus", tint = color, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun DetailedJarCard(
    title: String,
    subtitle: String,
    coins: Int,
    accentColor: Color,
    bgColor: Color,
    borderColor: Color,
    consequence: String,
    actionButton: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = accentColor,
                    letterSpacing = (-0.1).sp
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor)
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "$coins Coins 🪙",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = BoldTextPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = consequence,
                    style = MaterialTheme.typography.bodySmall,
                    color = BoldTextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }

            if (actionButton != null) {
                Spacer(modifier = Modifier.height(12.dp))
                actionButton()
            }
        }
    }
}

