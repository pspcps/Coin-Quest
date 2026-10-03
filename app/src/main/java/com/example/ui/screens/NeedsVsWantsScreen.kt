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
import com.example.data.model.NeedsVsWantsCard
import com.example.data.model.SampleGameData
import com.example.ui.components.BennySpeechBubble
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeedsVsWantsScreen(
    onBack: () -> Unit,
    onSubmitResult: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = SampleGameData.needsVsWantsCards
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Boolean?>(null) } // true for Need, false for Want
    var showFeedback by remember { mutableStateOf(false) }

    val currentCard = questions[currentIndex % questions.size]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Needs vs Wants 🎲",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = BoldTextPrimary,
                        letterSpacing = (-0.2).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("nvw_back_btn")) {
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
                    text = "A NEED is something we must have to stay alive, healthy, and safe (like food, water, a warm coat). A WANT is something fun we like to have (like toys, candy, video games)!"
                )
            }

            item {
                Text(
                    text = "QUESTION ${currentIndex + 1} OF ${questions.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = CoinGoldDark,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.8.sp
                )
            }

            // Big Item Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(26.dp)),
                    colors = CardDefaults.cardColors(containerColor = BoldSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, BoldBorder),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(CircleShape)
                                .background(BoldSurfaceVariant)
                                .border(2.dp, BoldBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = currentCard.icon, fontSize = 52.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentCard.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary,
                            textAlign = TextAlign.Center,
                            letterSpacing = (-0.3).sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Is this a NEED or a WANT?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BoldTextSecondary
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        // Choice Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Button(
                                onClick = {
                                    selectedAnswer = true
                                    showFeedback = true
                                    val isCorrect = currentCard.isNeed == true
                                    onSubmitResult("NEEDS_VS_WANTS", isCorrect)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp)
                                    .testTag("choose_need_btn"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showFeedback && currentCard.isNeed) GardenGreen else GardenGreen.copy(alpha = 0.9f)
                                )
                            ) {
                                Text(
                                    text = "🥗 NEED",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Button(
                                onClick = {
                                    selectedAnswer = false
                                    showFeedback = true
                                    val isCorrect = currentCard.isNeed == false
                                    onSubmitResult("NEEDS_VS_WANTS", isCorrect)
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp)
                                    .testTag("choose_want_btn"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (showFeedback && !currentCard.isNeed) SpendOrangeDark else SpendOrangeDark.copy(alpha = 0.9f)
                                )
                            ) {
                                Text(
                                    text = "🎮 WANT",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }

            // Explanation & Feedback Box
            if (showFeedback && selectedAnswer != null) {
                item {
                    val isCorrect = selectedAnswer == currentCard.isNeed
                    val feedbackBg = if (isCorrect) GardenGreenMint else Color(0xFFFFF3E0)
                    val feedbackBorder = if (isCorrect) GardenGreenLight else Color(0xFFFFCC80)
                    val feedbackAccent = if (isCorrect) GardenGreenDark else SpendOrangeDark

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp)),
                        colors = CardDefaults.cardColors(containerColor = feedbackBg),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, feedbackBorder),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = if (isCorrect) "🌟 You Got It!" else "💡 Good Learning Moment!", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (currentCard.isNeed) "This is a NEED! 🥗" else "This is a WANT! 🎮",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = feedbackAccent
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = currentCard.explanation,
                                style = MaterialTheme.typography.bodyLarge,
                                color = BoldTextPrimary,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    showFeedback = false
                                    selectedAnswer = null
                                    currentIndex++
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("next_question_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark)
                            ) {
                                Text(
                                    text = "Next Card →",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

