package com.example.ui.screens

import androidx.compose.animation.core.*
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.GardenPlant
import com.example.data.model.GardenTreeOption
import com.example.data.model.SampleGameData
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    profile: UserProfile,
    activePlants: List<GardenPlant>,
    activeCompoundingPlant: GardenPlant?,
    todayDateString: String = "",
    onBack: () -> Unit,
    onPlantTree: (name: String, icon: String, cost: Int, treeType: String) -> Unit,
    onWaterPlant: (Int) -> Unit,
    onHarvestPlant: (Int) -> Unit,
    onCompoundReinvest: (Int) -> Unit,
    onDismissCompoundingDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val treeCatalog = SampleGameData.gardenSeedCatalog
    var showPlantDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Magic Garden 🌱",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("garden_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    Button(
                        onClick = { showPlantDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GardenGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 12.dp).testTag("plant_new_seed_btn")
                    ) {
                        Text("+ Plant Tree 🌳", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
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
                    text = "Investing is planting a money tree! Instead of spending coins today, give them a job in the soil. With patience, watering, and compounding, they grow into a bigger harvest! 🌱➡️🍎➡️✨"
                )
            }

            item {
                Text(
                    text = "YOUR GROWING TREES & CROPS (${activePlants.size})",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = BoldTextSecondary,
                    letterSpacing = 0.8.sp
                )
            }

            if (activePlants.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp)),
                        colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GardenGreenLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🌱", fontSize = 52.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Your orchard is ready for planting!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = GardenGreenDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap '+ Plant Tree' above to give your coins a job to compound and grow.",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = BoldTextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            } else {
                items(activePlants) { plant ->
                    PlantCard(
                        plant = plant,
                        todayDateString = todayDateString,
                        onWater = { onWaterPlant(plant.id) },
                        onHarvest = { onHarvestPlant(plant.id) }
                    )
                }
            }

            // Investment Lesson Card
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
                        Text(
                            text = "💡 WHAT WE LEARN IN THE COMPOUND GARDEN:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. Delayed Gratification: Waiting turns small seeds into fruit-bearing trees.\n2. Giving Coins a Job: Invested coins work day and night.\n3. Compounding Magic: If you leave the harvest to compound, your profits start earning their own profits!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = BoldTextPrimary,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Plant Selection Dialog
    if (showPlantDialog) {
        AlertDialog(
            onDismissRequest = { showPlantDialog = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = BoldSurface,
            title = {
                Text(
                    text = "🌱 Choose a Tree to Plant",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = BoldTextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Investing coins gives them a job to grow over time:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    treeCatalog.forEach { option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onPlantTree(option.name, option.icon, option.cost, option.treeType)
                                    showPlantDialog = false
                                },
                            colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                            border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreenLight),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = option.icon, fontSize = 26.sp)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = option.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                        Text(
                                            text = option.treeType,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = BoldTextSecondary
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(CoinGoldDark)
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                    Text(text = "${option.cost} 🪙", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPlantDialog = false }) {
                    Text("Close", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                }
            }
        )
    }

    // Compounding Decision Challenge Dialog
    activeCompoundingPlant?.let { plant ->
        CompoundingDecisionDialog(
            plant = plant,
            onHarvest = { onHarvestPlant(plant.id) },
            onCompound = { onCompoundReinvest(plant.id) },
            onDismiss = onDismissCompoundingDialog
        )
    }
}

@Composable
fun PlantCard(
    plant: GardenPlant,
    todayDateString: String = "",
    onWater: () -> Unit,
    onHarvest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWateredToday = plant.lastWateredDateString == todayDateString

    val stageVisual = when (plant.stage) {
        0 -> "🌱 Seed in Soil"
        1 -> "🌿 Sprout Growing"
        2 -> "🪴 Strong Sapling"
        3 -> "🌳 Blooming Tree"
        else -> "🍎 Fruit Harvest Ready!"
    }

    val stageEmoji = when (plant.stage) {
        0 -> "🌱"
        1 -> "🌿"
        2 -> "🪴"
        3 -> "🌳"
        else -> "🍎"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, GardenGreenLight),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, GardenGreenLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stageEmoji, fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = plant.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = GardenGreenDark
                        )
                        Text(
                            text = stageVisual,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = BoldTextPrimary
                        )
                        if (plant.compoundingCycle > 1) {
                            Text(
                                text = "✨ Cycle ${plant.compoundingCycle} (Compounding!)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = CoinGoldDark
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Invested: ${plant.coinsInvested} 🪙",
                        style = MaterialTheme.typography.labelSmall,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Target: ~${plant.targetReturnCoins} 🪙",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Black,
                        color = CoinGoldDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Growth Progress Bar
            val progress = (plant.waterCount.toFloat() / 4f).coerceIn(0f, 1f)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = GardenGreen,
                trackColor = Color.White
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (plant.stage < 4) {
                    Text(
                        text = if (isWateredToday) "☀️ Watered today! (1/day limit)" else "💧 1 water per day needed",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isWateredToday) Color(0xFF15803D) else BoldTextSecondary
                    )
                    Button(
                        onClick = onWater,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isWateredToday) Color(0xFF94A3B8) else SaveBlueDark
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("water_plant_${plant.id}")
                    ) {
                        Text(
                            text = if (isWateredToday) "✅ Watered Today" else "💧 Water (${plant.waterCount}/4)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = onHarvest,
                        colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("harvest_plant_${plant.id}")
                    ) {
                        Text("🍎 Ready! Harvest or Compound ✨", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CompoundingDecisionDialog(
    plant: GardenPlant,
    onHarvest: () -> Unit,
    onCompound: () -> Unit,
    onDismiss: () -> Unit
) {
    val harvestCoins = plant.targetReturnCoins
    val nextCycleEstimate = (harvestCoins * 1.25f).toInt()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .clip(RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = BoldSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, CoinGoldLight),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🌳 ✨ 🍎 ✨ 🌳", fontSize = 28.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "THE COMPOUNDING DECISION",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = CoinGoldDark,
                    letterSpacing = 0.8.sp
                )

                Text(
                    text = "What will you do with ${plant.name}?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = BoldTextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Your tree produced $harvestCoins coins from an initial ${plant.coinsInvested} coins! You have two magical choices:",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = BoldTextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Choice 1: Harvest Now
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHarvest() },
                    colors = CardDefaults.cardColors(containerColor = SpendOrangeBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SpendOrangeLight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🍎", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Harvest Now (+$harvestCoins 🪙)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = SpendOrangeDark)
                            Text("Take your coins to your wallet today to spend, save, or share.", style = MaterialTheme.typography.bodySmall, color = BoldTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Choice 2: Compound & Reinvest
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCompound() },
                    colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                    border = androidx.compose.foundation.BorderStroke(2.dp, GardenGreenLight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✨", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Compound Reinvest (Cycle ${plant.compoundingCycle + 1})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = GardenGreenDark)
                            Text("Reinvest all $harvestCoins coins into soil. Next cycle will yield ~$nextCycleEstimate coins!", style = MaterialTheme.typography.bodySmall, color = BoldTextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                TextButton(onClick = onDismiss) {
                    Text("Decide Later", style = MaterialTheme.typography.labelLarge, color = BoldTextMuted)
                }
            }
        }
    }
}
