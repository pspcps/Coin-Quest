package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.data.model.QuizDailyHistory
import com.example.data.model.QuizQuestion
import com.example.data.model.SampleGameData
import com.example.data.model.UserProfile
import com.example.ui.theme.standardTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyQuizScreen(
    profile: UserProfile,
    todayHistory: QuizDailyHistory?,
    onBack: () -> Unit,
    onSubmitQuiz: (score: Int, total: Int) -> Unit,
    onUpdateQuizConfig: (age: Int, dailyGoal: Int, reward: Int) -> Unit
) {
    var showConfigDialog by remember { mutableStateOf(false) }
    var ageInput by remember(profile.childAge) { mutableStateOf(profile.childAge.toString()) }
    var goalInput by remember(profile.quizDailyGoal) { mutableStateOf(profile.quizDailyGoal.toString()) }
    var rewardInput by remember(profile.quizRewardCoins) { mutableStateOf(profile.quizRewardCoins.toString()) }

    // Filter questions matching child age
    val suitableQuestions = remember(profile.childAge) {
        val matching = SampleGameData.quizQuestionBank.filter { q ->
            profile.childAge in q.minAge..q.maxAge || (profile.childAge >= 13 && q.minAge >= 13)
        }
        if (matching.isEmpty()) SampleGameData.quizQuestionBank.take(10) else matching.shuffled().take(profile.quizDailyGoal.coerceIn(5, 15))
    }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf<Map<Int, Int>>(emptyMap()) }
    var hasAnsweredCurrent by remember { mutableStateOf(false) }
    var quizCompleted by remember { mutableStateOf(todayHistory != null) }
    var finalScore by remember { mutableStateOf(todayHistory?.score ?: 0) }

    val currentQ = if (currentQuestionIndex < suitableQuestions.size) suitableQuestions[currentQuestionIndex] else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🧠 Daily Brain Quiz", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("quiz_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showConfigDialog = true },
                        modifier = Modifier.testTag("quiz_config_btn")
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = "Configure Quiz", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Age & Reward info banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Target Age: ${profile.childAge} Years Old", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Complete today's quiz to earn +${profile.quizRewardCoins} coins!", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                    }
                    Surface(
                        color = Color(0xFFF59E0B),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "+${profile.quizRewardCoins} 🪙",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            if (quizCompleted || todayHistory != null) {
                // Completed Card
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("🎉", fontSize = 52.sp)
                        Text(
                            "Today's Brain Quiz Completed!",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "You scored ${todayHistory?.score ?: finalScore} out of ${todayHistory?.totalQuestions ?: suitableQuestions.size} correct!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF15803D)
                        )
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFDCFCE7),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "🪙 +${todayHistory?.coinsEarned ?: profile.quizRewardCoins} Coins added to your wallet! Come back tomorrow for a new set of 10 questions!",
                                color = Color(0xFF166534),
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(14.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Button(
                            onClick = {
                                // Retake for practice
                                currentQuestionIndex = 0
                                selectedAnswers = emptyMap()
                                hasAnsweredCurrent = false
                                quizCompleted = false
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Practice Again")
                        }
                    }
                }
            } else if (currentQ != null) {
                // Quiz Question Card
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Question ${currentQuestionIndex + 1} of ${suitableQuestions.size}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp
                            )
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    currentQ.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        LinearProgressIndicator(
                            progress = { (currentQuestionIndex + 1).toFloat() / suitableQuestions.size },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Text(
                            currentQ.questionText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            lineHeight = 22.sp
                        )

                        // Options
                        currentQ.options.forEachIndexed { optIndex, optionText ->
                            val isSelected = selectedAnswers[currentQuestionIndex] == optIndex
                            val isCorrectOpt = optIndex == currentQ.correctOptionIndex

                            val backgroundColor by animateColorAsState(
                                targetValue = when {
                                    !hasAnsweredCurrent -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    isCorrectOpt -> Color(0xFFDCFCE7)
                                    isSelected -> Color(0xFFFEE2E2)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                }, label = "opt_color"
                            )

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = backgroundColor,
                                border = BorderStroke(
                                    1.dp,
                                    if (hasAnsweredCurrent && isCorrectOpt) Color(0xFF16A34A) else Color.Transparent
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !hasAnsweredCurrent) {
                                        selectedAnswers = selectedAnswers + (currentQuestionIndex to optIndex)
                                        hasAnsweredCurrent = true
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                ('A'.code + optIndex).toChar().toString(),
                                                color = if (isSelected) Color.White else Color.Black,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        optionText,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Explanation
                        AnimatedVisibility(visible = hasAnsweredCurrent) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFEF3C7),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("💡", fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        currentQ.explanation,
                                        fontSize = 12.sp,
                                        color = Color(0xFF78350F),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        // Next button
                        if (hasAnsweredCurrent) {
                            Button(
                                onClick = {
                                    if (currentQuestionIndex < suitableQuestions.size - 1) {
                                        currentQuestionIndex++
                                        hasAnsweredCurrent = false
                                    } else {
                                        // Calculate score
                                        var correctCount = 0
                                        suitableQuestions.forEachIndexed { idx, q ->
                                            if (selectedAnswers[idx] == q.correctOptionIndex) {
                                                correctCount++
                                            }
                                        }
                                        finalScore = correctCount
                                        quizCompleted = true
                                        onSubmitQuiz(correctCount, suitableQuestions.size)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    if (currentQuestionIndex < suitableQuestions.size - 1) "Next Question ➡️" else "Finish & Collect Coins 🏆",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Config Dialog (Age mapping, daily goal, coins)
    if (showConfigDialog) {
        AlertDialog(
            onDismissRequest = { showConfigDialog = false },
            containerColor = Color.White,
            title = { Text("⚙️ Configure Daily Quiz", fontWeight = FontWeight.Bold, color = Color(0xFF1C1B1F)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Customize quiz difficulty by child age, daily question count, and earned coins:",
                        fontSize = 13.sp,
                        color = Color(0xFF49454F)
                    )

                    OutlinedTextField(
                        value = ageInput,
                        onValueChange = { ageInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Child's Age (e.g. 5, 8, 11, 14)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = goalInput,
                        onValueChange = { goalInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Questions per Day (e.g. 10)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = rewardInput,
                        onValueChange = { rewardInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Completion Reward (Coins e.g. 5)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val age = ageInput.toIntOrNull() ?: 8
                        val goal = goalInput.toIntOrNull() ?: 10
                        val reward = rewardInput.toIntOrNull() ?: 5
                        onUpdateQuizConfig(age, goal, reward)
                        showConfigDialog = false
                    }
                ) {
                    Text("Save Quiz Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfigDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
