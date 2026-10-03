package com.example

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.TopAdventureBar
import com.example.ui.screens.*
import com.example.ui.theme.MoneyAdventureTheme
import com.example.ui.viewmodel.GameScreen
import com.example.ui.viewmodel.GameViewModel

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyAdventureTheme {
                MoneyAdventureApp()
            }
        }
    }
}

@Composable
fun MoneyAdventureApp(
    viewModel: GameViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (uiState.currentScreen == GameScreen.VILLAGE_HUB) {
                TopAdventureBar(
                    profile = uiState.profile,
                    allProfiles = uiState.allProfiles,
                    onParentClick = { viewModel.navigateTo(GameScreen.PARENT_DASHBOARD) },
                    onAvatarClick = { viewModel.navigateTo(GameScreen.DREAM_GOALS) },
                    onSwitchProfile = { viewModel.switchKidProfile(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    GameScreen.VILLAGE_HUB -> {
                        HomeScreen(
                            profile = uiState.profile,
                            goals = uiState.goals,
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                    GameScreen.ADVENTURES_HABITS, GameScreen.EARN_CHORES -> {
                        AdventuresHabitsScreen(
                            profile = uiState.profile,
                            habits = uiState.habits,
                            dailyLogs = uiState.dailyHabitLogs,
                            todayDateString = uiState.todayDateString,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onLogHabit = { viewModel.logHabitCompletion(it) },
                            onCompleteRecoveryQuest = { viewModel.completeRecoveryQuest(it) }
                        )
                    }
                    GameScreen.MARKET_SHOP -> {
                        ShopScreen(
                            profile = uiState.profile,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onSelectItem = { viewModel.selectShopItem(it) },
                            onConfirmPurchase = { product, isSmart -> viewModel.buyShopProduct(product, isSmart) },
                            activePromptProduct = uiState.activeShopItemPrompt,
                            activeSmartChoiceProduct = uiState.activeSmartChoiceProduct,
                            onDismissPrompt = { viewModel.dismissShopPrompt() },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                    GameScreen.MAGIC_JARS -> {
                        JarsScreen(
                            profile = uiState.profile,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onDistribute = { spend, save, give, safety, callback ->
                                viewModel.distributeCoins(spend, save, give, safety, callback)
                            },
                            onDonate = { amount, cause, desc, callback ->
                                viewModel.donateCoins(amount, cause, desc, callback)
                            }
                        )
                    }
                    GameScreen.MAGIC_GARDEN -> {
                        GardenScreen(
                            profile = uiState.profile,
                            activePlants = uiState.activePlants,
                            activeCompoundingPlant = uiState.activeCompoundingPlant,
                            todayDateString = uiState.todayDateString,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onPlantTree = { name, icon, cost, type -> viewModel.plantInvestmentTree(name, icon, cost, type) },
                            onWaterPlant = { plantId -> viewModel.waterPlant(plantId) },
                            onHarvestPlant = { plantId -> viewModel.harvestPlant(plantId) },
                            onCompoundReinvest = { plantId -> viewModel.compoundPlant(plantId) },
                            onDismissCompoundingDialog = { viewModel.dismissCompoundingDialog() }
                        )
                    }
                    GameScreen.INVEST_SIP -> {
                        InvestSipScreen(
                            profile = uiState.profile,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onUpdateSip = { amount, freq, active -> viewModel.updateSipSettings(amount, freq, active) },
                            onProcessMonthlyGrowth = { viewModel.processMonthlySavingsGrowthAndSIP() }
                        )
                    }
                    GameScreen.TOY_SHOP -> {
                        ToyShopScreen(
                            profile = uiState.profile,
                            customToys = uiState.customToys,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onBuyToy = { toy, isSmart -> viewModel.buyCustomToy(toy, isSmart) },
                            onAddToy = { title, icon, price, cat, desc, altT, altP, altXp ->
                                viewModel.addCustomToy(title, icon, price, cat, desc, altT, altP, altXp)
                            },
                            onDeleteToy = { viewModel.deleteCustomToy(it) }
                        )
                    }
                    GameScreen.DAILY_QUIZ -> {
                        DailyQuizScreen(
                            profile = uiState.profile,
                            todayHistory = uiState.todayQuizHistory,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onSubmitQuiz = { score, total -> viewModel.submitDailyQuiz(score, total) },
                            onUpdateQuizConfig = { age, goal, coins -> viewModel.updateQuizConfig(age, goal, coins) }
                        )
                    }
                    GameScreen.PUZZLE_ARCADE -> {
                        PuzzleArcadeScreen(
                            profile = uiState.profile,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onAwardCoins = { coins, reason -> viewModel.awardGameCoins(coins, reason) }
                        )
                    }
                    GameScreen.AGE_SKILLS -> {
                        AgeSkillsScreen(
                            profile = uiState.profile,
                            activeProjects = uiState.activeSkillProjects,
                            completedProjects = uiState.completedSkillProjects,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onDismissSkill = { viewModel.dismissSkill(it) },
                            onCompleteProject = { project, notes -> viewModel.completeSkillProject(project, notes) },
                            onReloadSkills = { viewModel.reloadPredefinedSkills() },
                            onAddSkill = { title, icon, skillName, category, craftCost, sellPrice, desc ->
                                viewModel.addCustomSkill(title, icon, skillName, category, craftCost, sellPrice, desc)
                            }
                        )
                    }
                    GameScreen.REPORTS_ANALYTICS -> {
                        ReportsScreen(
                            profile = uiState.profile,
                            logs = uiState.activityLogs,
                            dailyHabitLogs = uiState.dailyHabitLogs,
                            quizHistory = uiState.quizHistory,
                            completedSkills = uiState.completedSkillProjects,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) }
                        )
                    }
                    GameScreen.NEEDS_VS_WANTS -> {
                        NeedsVsWantsScreen(
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onSubmitResult = { gameType, isCorrect ->
                                viewModel.submitMiniGameAnswer(gameType, isCorrect)
                            }
                        )
                    }
                    GameScreen.BENNY_BANK -> {
                        BennyBankScreen(
                            profile = uiState.profile,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onDeposit = { viewModel.depositToBank(it) },
                            onWithdraw = { viewModel.withdrawFromBank(it) },
                            onClaimInterest = { viewModel.claimBankReward() }
                        )
                    }
                    GameScreen.DREAM_GOALS -> {
                        GoalsScreen(
                            profile = uiState.profile,
                            goals = uiState.goals,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onSetActiveGoal = { viewModel.setActiveGoal(it) },
                            onCreateGoal = { title, icon, desc, cost ->
                                viewModel.createGoal(title, icon, desc, cost)
                            },
                            onDeleteGoal = { viewModel.deleteGoal(it) }
                        )
                    }
                    GameScreen.LIFE_ADVENTURE -> {
                        LifeAdventureScreen(
                            profile = uiState.profile,
                            activeEvent = uiState.activeLifeEvent,
                            eventResult = uiState.lifeEventResult,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onDrawCard = { viewModel.drawLifeEvent() },
                            onResolveEvent = { viewModel.resolveLifeEvent() },
                            onDismissEvent = { viewModel.dismissLifeEvent() }
                        )
                    }
                    GameScreen.SKILL_SCHOOL -> {
                        SkillSchoolScreen(
                            profile = uiState.profile,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onCraftAndSell = { viewModel.craftAndSell(it) }
                        )
                    }
                    GameScreen.PARENT_DASHBOARD -> {
                        ParentDashboardScreen(
                            profile = uiState.profile,
                            allProfiles = uiState.allProfiles,
                            goals = uiState.goals,
                            logs = uiState.activityLogs,
                            stats = uiState.stats,
                            habits = uiState.habits,
                            dailyHabitLogs = uiState.dailyHabitLogs,
                            isUnlocked = uiState.parentUnlocked,
                            mathQuestion = uiState.parentMathQuestion,
                            onBack = { viewModel.navigateTo(GameScreen.VILLAGE_HUB) },
                            onVerifyAnswer = { viewModel.verifyParentAnswer(it) },
                            onUnlockDirectly = { viewModel.unlockParentZoneDirectly() },
                            onLockZone = { viewModel.lockParentZone() },
                            onSwitchProfile = { viewModel.switchKidProfile(it) },
                            onAddProfile = { name, avatar, dob, age ->
                                viewModel.addKidProfile(name, avatar, dob, age)
                            },
                            onDeleteProfile = { viewModel.deleteKidProfile(it) },
                            onUpdateProfile = { viewModel.updateKidProfile(it) },
                            onApproveHabit = { viewModel.approveHabitLog(it) },
                            onApproveAllHabits = { viewModel.approveAllHabits() },
                            onGivePoints = { amount, reason, childId -> viewModel.giveRewardPoints(amount, reason, childId) },
                            onMarkMissed = { viewModel.markHabitMissed(it) },
                            onAddHabit = { title, icon, reward, category, freq, method, isTeamwork, deduction, note ->
                                viewModel.addHabit(title, icon, reward, category, freq, method, isTeamwork, deduction, note)
                            },
                            onToggleHabit = { viewModel.toggleHabit(it) },
                            onDeleteHabit = { viewModel.deleteHabit(it) },
                            onUpdatePolicy = { viewModel.updateParentPolicy(it) },
                            onRevokeCoins = { amount, reason, targetJar -> viewModel.revokeCoins(amount, reason, targetJar) }
                        )
                    }
                }
            }
        }
    }

    // Celebration Pop-up Dialog
    uiState.celebrationMessage?.let { msg ->
        CelebrationDialog(
            coins = uiState.celebrationCoins,
            message = msg,
            onDismiss = { viewModel.clearCelebration() }
        )
    }
}
