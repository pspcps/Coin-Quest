package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SampleGameData
import com.example.data.model.SkillBusinessItem
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillSchoolScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onCraftAndSell: (SkillBusinessItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val skillItems = SampleGameData.skillBusinesses

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Skill School & Business 🏪",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("skill_back_btn")) {
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
                    text = "When you learn a new skill — like painting, baking, or gardening — you can make things other people love and appreciate! That is how businesses work! 🎨🍪"
                )
            }

            item {
                Text(
                    text = "CRAFT & SELL TO VILLAGE FRIENDS",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = BoldTextSecondary,
                    letterSpacing = 0.8.sp
                )
            }

            items(skillItems) { item ->
                SkillCraftCard(
                    item = item,
                    userSpendCoins = profile.spendJarCoins + profile.unassignedCoins,
                    onCraft = { onCraftAndSell(item) }
                )
            }

            // Skill Lesson Card
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
                            text = "💡 VALUE & PROFIT LESSON",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Money is an exchange of value. You buy ingredients (flour/paint), add your effort and skill, and sell something wonderful for more coins than you started with! That extra coin is called profit!",
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
}

@Composable
fun SkillCraftCard(
    item: SkillBusinessItem,
    userSpendCoins: Int,
    onCraft: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canAffordMaterials = userSpendCoins >= item.craftCost

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(containerColor = BoldSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(BoldSurfaceVariant)
                            .border(1.dp, BoldBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = item.icon, fontSize = 28.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary
                        )
                        Text(
                            text = "Skill: ${item.skillName}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = GardenGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = BoldTextSecondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SpendOrangeBg)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Cost: ${item.craftCost} 🪙",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = SpendOrangeDark
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(GardenGreenMint)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Sell: ${item.sellPrice} 🪙",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = GardenGreenDark
                        )
                    }
                }

                Button(
                    onClick = onCraft,
                    enabled = canAffordMaterials,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                    modifier = Modifier.testTag("craft_${item.id}")
                ) {
                    Text(
                        text = "Craft & Sell! ✨",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}

