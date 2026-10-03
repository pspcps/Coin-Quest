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
import com.example.data.model.SampleGameData
import com.example.data.model.ShopProduct
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*
import com.example.ui.viewmodel.GameScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onSelectItem: (ShopProduct) -> Unit,
    onConfirmPurchase: (product: ShopProduct, isSmart: Boolean) -> Unit,
    activePromptProduct: ShopProduct?,
    activeSmartChoiceProduct: ShopProduct?,
    onDismissPrompt: () -> Unit,
    onNavigate: (GameScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val products = SampleGameData.shopProducts

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Toy Market 🛒",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("shop_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 14.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SpendOrangeBg)
                            .border(1.5.dp, SpendOrangeLight, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "🎮 SPEND: ${profile.spendJarCoins} 🪙",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = SpendOrangeDark,
                            letterSpacing = 0.5.sp
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
                    text = "Spend Less on wants leaves more coins for big dreams! When you make smart choices, you save coins and earn Smart XP! 💡⭐"
                )
            }

            // Smart Badge Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Smart Spender Status", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = CoinGoldDark)
                                Text(profile.smartChoiceBadge, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = BoldTextPrimary)
                            }
                        }
                        Text("${profile.smartChoiceXp} XP", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = CoinGoldDark)
                    }
                }
            }

            item {
                Text(
                    text = "ITEMS IN THE VILLAGE SHOP",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = BoldTextSecondary,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            items(products) { product ->
                ShopItemCard(
                    product = product,
                    userSpendCoins = profile.spendJarCoins,
                    onSelect = { onSelectItem(product) }
                )
            }
        }
    }

    // Smart Choice Opportunity Dialog (Spend Less Mechanic)
    activeSmartChoiceProduct?.let { product ->
        val altTitle = product.smartAlternativeTitle ?: "Standard Edition"
        val altPrice = product.smartAlternativePrice ?: (product.price / 2)
        val coinsSaved = product.price - altPrice

        Dialog(onDismissRequest = onDismissPrompt) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .clip(RoundedCornerShape(26.dp)),
                colors = CardDefaults.cardColors(containerColor = BoldSurface),
                border = androidx.compose.foundation.BorderStroke(2.dp, CoinGoldLight),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "💡 🛍️ 💡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "SMART CHOICE CHALLENGE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = CoinGoldDark,
                        letterSpacing = 0.8.sp
                    )

                    Text(
                        text = "Compare Your Options!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Both bring awesome joy! Choosing the smart alternative saves coins for your other goals and earns +${product.smartXpReward} Smart XP!",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = BoldTextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Option A: Deluxe Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onConfirmPurchase(product, false) },
                        colors = CardDefaults.cardColors(containerColor = SpendOrangeBg),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SpendOrangeLight),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(product.icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(product.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = SpendOrangeDark)
                                    Text("Deluxe Edition", style = MaterialTheme.typography.bodySmall, color = BoldTextSecondary)
                                }
                            }
                            Text("${product.price} 🪙", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = SpendOrangeDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Option B: Smart Value Option
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onConfirmPurchase(product, true) },
                        colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                        border = androidx.compose.foundation.BorderStroke(2.dp, GardenGreenLight),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text("⭐", fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(altTitle, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                    Text("Save $coinsSaved 🪙 + ${product.smartXpReward} XP", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = GardenGreenDark)
                                }
                            }
                            Text("$altPrice 🪙", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = GardenGreenDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = onDismissPrompt) {
                        Text("Think About It More", style = MaterialTheme.typography.labelLarge, color = BoldTextMuted)
                    }
                }
            }
        }
    }

    // Educational Choice Dialog when child cannot afford an item
    activePromptProduct?.let { product ->
        Dialog(onDismissRequest = onDismissPrompt) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(26.dp)),
                colors = CardDefaults.cardColors(containerColor = BoldSurface),
                border = androidx.compose.foundation.BorderStroke(2.dp, BoldBorder),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(SpendOrangeBg)
                            .border(1.5.dp, SpendOrangeLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = product.icon, fontSize = 40.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You have ${profile.spendJarCoins} coins in your Spend Jar.\nThe ${product.title} costs ${product.price} coins.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "WHAT WOULD YOU LIKE TO DO?",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = CoinGoldDark,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 1: Buy something smaller
                    Button(
                        onClick = onDismissPrompt,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark)
                    ) {
                        Text("🛒 Look for something smaller", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Option 2: Save for it
                    Button(
                        onClick = {
                            onDismissPrompt()
                            onNavigate(GameScreen.MAGIC_JARS)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark)
                    ) {
                        Text("🐷 Save coins in Piggy Jar", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Black)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Option 3: Earn more coins
                    OutlinedButton(
                        onClick = {
                            onDismissPrompt()
                            onNavigate(GameScreen.ADVENTURES_HABITS)
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GardenGreen)
                    ) {
                        Text("💰 Complete Adventures & Habits", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = GardenGreenDark)
                    }
                }
            }
        }
    }
}

@Composable
fun ShopItemCard(
    product: ShopProduct,
    userSpendCoins: Int,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val canAfford = userSpendCoins >= product.price
    val containerBg = if (product.isLongTermWish) Color(0xFFFEFCE8) else BoldSurface
    val borderCol = if (product.isLongTermWish) Color(0xFFFDE047) else BoldBorder

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderCol),
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
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(BoldSurfaceVariant)
                        .border(1.dp, borderCol, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = product.icon, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = BoldTextSecondary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2
                    )
                    if (product.smartAlternativeTitle != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "💡 Smart Option Available",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = GardenGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onSelect,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canAfford) SpendOrangeDark else Color(0xFF9E9E9E)
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                modifier = Modifier.testTag("select_${product.id}")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${product.price} 🪙",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = if (product.smartAlternativeTitle != null) "CHOICE" else if (canAfford) "BUY" else "CHECK",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
