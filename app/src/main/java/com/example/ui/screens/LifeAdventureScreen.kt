package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LifeEventCard
import com.example.data.model.UserProfile
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeAdventureScreen(
    profile: UserProfile,
    activeEvent: LifeEventCard?,
    eventResult: String?,
    onBack: () -> Unit,
    onDrawCard: () -> Unit,
    onResolveEvent: () -> Unit,
    onDismissEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Life Adventure 🎁🛟",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("life_back_btn")) {
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
            item {
                BennySpeechBubble(
                    text = "In real life, surprises happen! Sometimes we get unexpected gifts, and sometimes things need quick repairs. That's why having a 🛟 Safety Jar is so helpful!"
                )
            }

            // Safety Jar Status Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp)),
                    colors = CardDefaults.cardColors(containerColor = SafetyTealBg),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, SafetyTealLight),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, SafetyTealLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🛟", fontSize = 28.sp)
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "YOUR SAFETY SHIELD",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Black,
                                    color = SafetyTealDark,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Ready for unexpected surprises",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = BoldTextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Text(
                            text = "${profile.safetyJarCoins} 🪙",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = SafetyTealDark
                        )
                    }
                }
            }

            // Surprise Wheel / Card Drawer
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
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(BoldSurfaceVariant)
                                .border(1.5.dp, BoldBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🎴", fontSize = 48.sp)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Draw a Surprise Life Card!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary,
                            letterSpacing = (-0.3).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Will it be a surprise reward or an unexpected emergency?",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = BoldTextSecondary,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onDrawCard,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("draw_life_card_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                        ) {
                            Text(
                                text = "✨ Draw Surprise Card! ✨",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Active Drawn Card
            activeEvent?.let { event ->
                item {
                    val eventBg = if (event.isEmergency) Color(0xFFFFF3E0) else GardenGreenMint
                    val eventBorder = if (event.isEmergency) Color(0xFFFFCC80) else GardenGreenLight
                    val eventAccent = if (event.isEmergency) SpendOrangeDark else GardenGreenDark

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(containerColor = eventBg),
                        border = androidx.compose.foundation.BorderStroke(2.dp, eventBorder),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.5.dp, eventBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = event.icon, fontSize = 38.sp)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = eventAccent,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = event.description,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = BoldTextPrimary,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = if (event.isEmergency) "Cost: ${-event.coinImpact} Coins 🪙" else "Gift: +${event.coinImpact} Coins 🪙",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = eventAccent
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            if (eventResult == null) {
                                Button(
                                    onClick = onResolveEvent,
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (event.isEmergency) SafetyTealDark else GardenGreen
                                    ),
                                    modifier = Modifier.fillMaxWidth().height(52.dp)
                                ) {
                                    Text(
                                        text = if (event.isEmergency) "🛟 Handle with Safety Jar!" else "🎉 Accept Coins!",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(BoldSurface)
                                        .border(1.dp, BoldBorder, RoundedCornerShape(16.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = eventResult,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = BoldTextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "💡 Lesson: ${event.lesson}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = BoldTextSecondary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = onDismissEvent,
                                    colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Text("Got It! 🌟", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

