package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BennyBankScreen(
    profile: UserProfile,
    onBack: () -> Unit,
    onDeposit: (Int) -> Unit,
    onWithdraw: (Int) -> Unit,
    onClaimInterest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Benny's Bank 🐰🏦",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("bank_back_btn")) {
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
            contentPadding = PaddingValues(top = 14.dp, bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mascot Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = BunnyPurpleBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, BunnyPurpleLight),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.benny_bank_bunny_1787856760820),
                            contentDescription = "Benny Bunny",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, BunnyPurpleLight, RoundedCornerShape(20.dp))
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Benny the Bank Bunny",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = BunnyPurpleDark
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "\"Hello! Leave your saved coins safe with me in the vault. For every coin you leave, I give you a special waiting reward!\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldTextPrimary,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Bank Vault Balance Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(containerColor = BoldSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, BoldBorder),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "COINS SAFE IN THE VAULT",
                            style = MaterialTheme.typography.labelSmall,
                            color = BoldTextSecondary,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${profile.bankCoins} 🪙",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = BunnyPurpleDark,
                            letterSpacing = (-0.5).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "In Save Jar ready to deposit: ${profile.saveJarCoins} 🪙",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SaveBlueDark,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // Claim Waiting Reward Button (Interest)
                        Button(
                            onClick = onClaimInterest,
                            enabled = profile.bankCoins > 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("claim_bank_reward_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                        ) {
                            Text(
                                text = "⭐ Claim Waiting Reward! (+1 Coin)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Deposit & Withdraw Controls
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp)),
                    colors = CardDefaults.cardColors(containerColor = SaveBlueBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SaveBlueLight),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "DEPOSIT SAVED COINS TO BANK",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = SaveBlueDark,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Moving coins from your Save Jar into Benny's Bank gives them protection and helps them grow.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = BoldTextPrimary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onDeposit(2) },
                                enabled = profile.saveJarCoins >= 2,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark),
                                modifier = Modifier.weight(1f).testTag("deposit_2_btn")
                            ) {
                                Text("Deposit 2 🪙", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color.White)
                            }

                            Button(
                                onClick = { onDeposit(5) },
                                enabled = profile.saveJarCoins >= 5,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark),
                                modifier = Modifier.weight(1f).testTag("deposit_5_btn")
                            ) {
                                Text("Deposit 5 🪙", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color.White)
                            }

                            Button(
                                onClick = { onWithdraw(2) },
                                enabled = profile.bankCoins >= 2,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                                modifier = Modifier.weight(1f).testTag("withdraw_2_btn")
                            ) {
                                Text("Withdraw 2", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, color = Color.White)
                            }
                        }
                    }
                }
            }

            // Compound Growth Concept Visualizer
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
                            text = "🌱 THE SECRET OF COMPOUND GROWTH",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "When you leave 10 coins with Benny: \n➡️ Benny gives you +1 coin reward!\n➡️ Now you have 11 coins!\n➡️ Next time, those 11 coins earn even more rewards!\n\nThis is called compound growth — your money making more money!",
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

