package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GoalItem
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*
import com.example.ui.viewmodel.GameScreen

data class VillageLocation(
    val screen: GameScreen,
    val title: String,
    val subtitle: String,
    val icon: String,
    val bgColors: List<Color>,
    val borderColor: Color,
    val badge: String? = null
)

@Composable
fun HomeScreen(
    profile: UserProfile,
    goals: List<GoalItem>,
    onNavigate: (GameScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeGoal = goals.find { it.id == profile.activeGoalId } ?: goals.firstOrNull()

    val locations = listOf(
        VillageLocation(
            screen = GameScreen.ADVENTURES_HABITS,
            title = "Adventures & Habits",
            subtitle = "Daily routines & recovery quests",
            icon = "🎒",
            bgColors = listOf(Color(0xFFFFFBEB), Color(0xFFFEF3C7)),
            borderColor = Color(0xFFFDE68A),
            badge = "Earn 🪙"
        ),
        VillageLocation(
            screen = GameScreen.INVEST_SIP,
            title = "12% Wealth & SIP",
            subtitle = "Compounding & auto-invest",
            icon = "📈",
            bgColors = listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE)),
            borderColor = Color(0xFF93C5FD),
            badge = "12% APR ✨"
        ),
        VillageLocation(
            screen = GameScreen.TOY_SHOP,
            title = "Parent Toy Shop",
            subtitle = "Toys & smart alternatives",
            icon = "🧸",
            bgColors = listOf(Color(0xFFFFF1F2), Color(0xFFFFE4E6)),
            borderColor = Color(0xFFFECDD3),
            badge = "Wishlist 🛍️"
        ),
        VillageLocation(
            screen = GameScreen.DAILY_QUIZ,
            title = "Daily Brain Quiz",
            subtitle = "Age questions for +5 coins",
            icon = "🧠",
            bgColors = listOf(Color(0xFFFAF5FF), Color(0xFFF3E8FF)),
            borderColor = Color(0xFFE9D5FF),
            badge = "10 Daily 🪙"
        ),
        VillageLocation(
            screen = GameScreen.PUZZLE_ARCADE,
            title = "Puzzles & Arcade",
            subtitle = "Falling blocks & speed math",
            icon = "🎮",
            bgColors = listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A)),
            borderColor = Color(0xFFFCD34D),
            badge = "Games 🧱"
        ),
        VillageLocation(
            screen = GameScreen.AGE_SKILLS,
            title = "Age-Wise Skills",
            subtitle = "Craft, sell & micro-ventures",
            icon = "🎨",
            bgColors = listOf(Color(0xFFFFF7ED), Color(0xFFFFEDD5)),
            borderColor = Color(0xFFFED7AA),
            badge = "Profit 🍪"
        ),
        VillageLocation(
            screen = GameScreen.REPORTS_ANALYTICS,
            title = "Progress Reports",
            subtitle = "Daily, Monthly, 3M, 6M, Year",
            icon = "📊",
            bgColors = listOf(Color(0xFFF0FDF4), Color(0xFFDCFCE7)),
            borderColor = Color(0xFF86EFAC),
            badge = "Report 🏆"
        ),
        VillageLocation(
            screen = GameScreen.MAGIC_JARS,
            title = "4 Magic Jars",
            subtitle = "Spend, Save, Give, Safety",
            icon = "🐷",
            bgColors = listOf(Color(0xFFEFF6FF), Color(0xFFDBEAFE)),
            borderColor = Color(0xFFBFDBFE),
            badge = if (profile.unassignedCoins > 0) "${profile.unassignedCoins} to sort!" else null
        ),
        VillageLocation(
            screen = GameScreen.MAGIC_GARDEN,
            title = "Magic Garden",
            subtitle = "Grow & invest coins (1x/day)",
            icon = "🌱",
            bgColors = listOf(Color(0xFFECFDF5), Color(0xFFD1FAE5)),
            borderColor = Color(0xFFA7F3D0),
            badge = "Grow 🍎"
        ),
        VillageLocation(
            screen = GameScreen.BENNY_BANK,
            title = "Benny's Bank",
            subtitle = "Safe vault & bonus interest",
            icon = "🐰",
            bgColors = listOf(Color(0xFFF5F3FF), Color(0xFFEDE9FE)),
            borderColor = Color(0xFFDDD6FE),
            badge = "Bank 🏦"
        ),
        VillageLocation(
            screen = GameScreen.DREAM_GOALS,
            title = "Dream Goals",
            subtitle = "Save for big treasures",
            icon = "🎯",
            bgColors = listOf(Color(0xFFFEFCE8), Color(0xFFFEF08A)),
            borderColor = Color(0xFFFDE047),
            badge = "Goals 🏰"
        ),
        VillageLocation(
            screen = GameScreen.LIFE_ADVENTURE,
            title = "Life Adventure",
            subtitle = "Surprises & Safety Jar",
            icon = "🎁",
            bgColors = listOf(Color(0xFFF0FDFA), Color(0xFFCCFBF1)),
            borderColor = Color(0xFF99F6E4),
            badge = "Surprises 🛟"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BoldCanvas)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp)
    ) {
        // Hero Village Banner Card with bold typographic overlay
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .border(1.5.dp, BoldBorder, RoundedCornerShape(24.dp)),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(164.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_village_1787856746667),
                        contentDescription = "Village Map",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0xDD111827))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Welcome to your Village! 🏡",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = (-0.2).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Earn, decide, save, share & grow your coins!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFFDE68A),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Benny Speech Tip
        item {
            val tipText = when {
                profile.unassignedCoins > 0 -> "You have ${profile.unassignedCoins} coins waiting! Tap '4 Magic Jars' to decide where they go! 🪙"
                profile.saveJarCoins >= 20 -> "Your savings jar is growing so fast! You are practicing amazing patience! 🐷✨"
                else -> "Welcome, ${profile.name}! Complete helpful tasks at Home to earn shiny coins! 🌟"
            }
            BennySpeechBubble(text = tipText)
        }

        // 4 Jars Quick Summary Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp)),
                colors = CardDefaults.cardColors(containerColor = BoldSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "YOUR 4 MAGIC JARS",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextSecondary,
                            letterSpacing = 0.8.sp
                        )
                        TextButton(
                            onClick = { onNavigate(GameScreen.MAGIC_JARS) },
                            modifier = Modifier.testTag("manage_jars_btn")
                        ) {
                            Text(
                                text = "Open Jars →",
                                style = MaterialTheme.typography.labelLarge,
                                color = SaveBlue,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        JarMiniSummary(
                            title = "SPEND",
                            icon = "🎮",
                            coins = profile.spendJarCoins,
                            bgColor = SpendOrangeBg,
                            accentColor = SpendOrangeDark,
                            borderColor = SpendOrangeLight,
                            modifier = Modifier.weight(1f)
                        )
                        JarMiniSummary(
                            title = "SAVE",
                            icon = "🐷",
                            coins = profile.saveJarCoins,
                            bgColor = SaveBlueBg,
                            accentColor = SaveBlueDark,
                            borderColor = SaveBlueLight,
                            modifier = Modifier.weight(1f)
                        )
                        JarMiniSummary(
                            title = "GIVE",
                            icon = "❤️",
                            coins = profile.giveJarCoins,
                            bgColor = GiveHeartPinkBg,
                            accentColor = GiveHeartPinkDark,
                            borderColor = GiveHeartPinkLight,
                            modifier = Modifier.weight(1f)
                        )
                        JarMiniSummary(
                            title = "SAFETY",
                            icon = "🛟",
                            coins = profile.safetyJarCoins,
                            bgColor = SafetyTealBg,
                            accentColor = SafetyTealDark,
                            borderColor = SafetyTealLight,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Active Goal Progress Card
        if (activeGoal != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { onNavigate(GameScreen.DREAM_GOALS) }
                        .testTag("home_goal_card"),
                    colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFDE68A)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .border(1.dp, Color(0xFFFDE68A), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = activeGoal.icon, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "ACTIVE DREAM GOAL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = CoinGoldDark,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.8.sp
                                    )
                                    Text(
                                        text = activeGoal.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = BoldTextPrimary
                                    )
                                }
                            }

                            Text(
                                text = "${activeGoal.currentCoins}/${activeGoal.targetCost} 🪙",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = CoinGoldDark
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val progress = if (activeGoal.targetCost > 0) {
                            (activeGoal.currentCoins.toFloat() / activeGoal.targetCost.toFloat()).coerceIn(0f, 1f)
                        } else 0f

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = CoinGold,
                            trackColor = Color(0xFFFDE68A),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (activeGoal.isCompleted) "🎉 You achieved this goal! Tap to claim!" else "Saving patiently brings your dreams to life!",
                            style = MaterialTheme.typography.bodySmall,
                            color = BoldTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Section Title: Village Places
        item {
            Text(
                text = "Explore the Village",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = BoldTextPrimary,
                letterSpacing = (-0.2).sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Village Locations Grid
        items((locations.size + 1) / 2) { rowIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val firstIndex = rowIndex * 2
                val secondIndex = firstIndex + 1

                val loc1 = locations[firstIndex]
                VillageLocationCard(
                    location = loc1,
                    onClick = { onNavigate(loc1.screen) },
                    modifier = Modifier.weight(1f)
                )

                if (secondIndex < locations.size) {
                    val loc2 = locations[secondIndex]
                    VillageLocationCard(
                        location = loc2,
                        onClick = { onNavigate(loc2.screen) },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun JarMiniSummary(
    title: String,
    icon: String,
    coins: Int,
    bgColor: Color,
    accentColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "$coins 🪙",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = accentColor,
                letterSpacing = (-0.2).sp
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = BoldTextSecondary,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun VillageLocationCard(
    location: VillageLocation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(134.dp)
            .clip(RoundedCornerShape(22.dp))
            .border(1.5.dp, location.borderColor, RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .testTag("location_${location.screen.name}"),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(location.bgColors))
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, location.borderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = location.icon, fontSize = 24.sp)
                    }

                    if (location.badge != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CoinGoldDark)
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = location.badge,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.3.sp
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = location.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.1).sp
                    )
                    Text(
                        text = location.subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

