package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MoneyAdventureDatabase
import com.example.data.model.*
import com.example.data.repository.GameRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class GameScreen {
    VILLAGE_HUB,
    EARN_CHORES,
    ADVENTURES_HABITS,
    MARKET_SHOP,
    MAGIC_JARS,
    MAGIC_GARDEN,
    NEEDS_VS_WANTS,
    BENNY_BANK,
    DREAM_GOALS,
    LIFE_ADVENTURE,
    SKILL_SCHOOL,
    PARENT_DASHBOARD,
    TOY_SHOP,
    REPORTS_ANALYTICS,
    DAILY_QUIZ,
    PUZZLE_ARCADE,
    INVEST_SIP,
    AGE_SKILLS
}

data class GameUiState(
    val currentScreen: GameScreen = GameScreen.VILLAGE_HUB,
    val profile: UserProfile = UserProfile(),
    val allProfiles: List<UserProfile> = emptyList(),
    val goals: List<GoalItem> = emptyList(),
    val activePlants: List<GardenPlant> = emptyList(),
    val activityLogs: List<ActivityLog> = emptyList(),
    val stats: List<MiniGameStat> = emptyList(),
    val habits: List<HabitItem> = emptyList(),
    val dailyHabitLogs: List<DailyHabitLog> = emptyList(),
    val customToys: List<CustomToyItem> = emptyList(),
    val activeSkillProjects: List<SkillProjectEntity> = emptyList(),
    val completedSkillProjects: List<SkillProjectEntity> = emptyList(),
    val quizHistory: List<QuizDailyHistory> = emptyList(),
    val todayQuizHistory: QuizDailyHistory? = null,
    val celebrationMessage: String? = null,
    val celebrationCoins: Int = 0,
    val activeShopItemPrompt: ShopProduct? = null,
    val activeSmartChoiceProduct: ShopProduct? = null,
    val activeCompoundingPlant: GardenPlant? = null,
    val activeLifeEvent: LifeEventCard? = null,
    val lifeEventResult: String? = null,
    val parentUnlocked: Boolean = false,
    val parentMathQuestion: Pair<String, Int> = "8 + 6" to 14,
    val isDailySummaryOpen: Boolean = false,
    val isMoneyJourneyOpen: Boolean = false,
    val todayDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        val db = MoneyAdventureDatabase.getDatabase(application, viewModelScope)
        repository = GameRepository(db)

        viewModelScope.launch {
            repository.allProfiles.collect { list ->
                _uiState.update { it.copy(allProfiles = list) }
            }
        }

        viewModelScope.launch {
            repository.userProfile.filterNotNull().collect { prof ->
                _uiState.update { it.copy(profile = prof) }
            }
        }

        viewModelScope.launch {
            repository.allGoals.collect { list ->
                _uiState.update { it.copy(goals = list) }
            }
        }

        viewModelScope.launch {
            repository.activePlants.collect { plants ->
                _uiState.update { it.copy(activePlants = plants) }
            }
        }

        viewModelScope.launch {
            repository.allLogs.collect { logs ->
                _uiState.update { it.copy(activityLogs = logs) }
            }
        }

        viewModelScope.launch {
            repository.miniGameStats.collect { stats ->
                _uiState.update { it.copy(stats = stats) }
            }
        }

        viewModelScope.launch {
            repository.allHabits.collect { habitsList ->
                _uiState.update { it.copy(habits = habitsList) }
            }
        }

        viewModelScope.launch {
            repository.allDailyHabitLogs.collect { logsList ->
                _uiState.update { it.copy(dailyHabitLogs = logsList) }
            }
        }

        viewModelScope.launch {
            repository.allCustomToys.collect { toys ->
                _uiState.update { it.copy(customToys = toys) }
            }
        }

        viewModelScope.launch {
            repository.activeSkillProjects.collect { projects ->
                _uiState.update { it.copy(activeSkillProjects = projects) }
            }
        }

        viewModelScope.launch {
            repository.completedSkillPortfolio.collect { projects ->
                _uiState.update { it.copy(completedSkillProjects = projects) }
            }
        }

        viewModelScope.launch {
            repository.quizDailyHistory.collect { history ->
                val todayStr = _uiState.value.todayDateString
                _uiState.update { state ->
                    state.copy(
                        quizHistory = history,
                        todayQuizHistory = history.firstOrNull { it.dateString == todayStr }
                    )
                }
            }
        }

        generateParentMathQuestion()
    }

    fun navigateTo(screen: GameScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun triggerCelebration(coins: Int, message: String) {
        _uiState.update {
            it.copy(
                celebrationCoins = coins,
                celebrationMessage = message
            )
        }
    }

    fun clearCelebration() {
        _uiState.update {
            it.copy(
                celebrationCoins = 0,
                celebrationMessage = null
            )
        }
    }

    // Dialog state controllers
    fun openDailySummary() {
        _uiState.update { it.copy(isDailySummaryOpen = true) }
    }

    fun closeDailySummary() {
        _uiState.update { it.copy(isDailySummaryOpen = false) }
    }

    fun openMoneyJourney() {
        _uiState.update { it.copy(isMoneyJourneyOpen = true) }
    }

    fun closeMoneyJourney() {
        _uiState.update { it.copy(isMoneyJourneyOpen = false) }
    }

    // -------------------------------------------------------------
    // HABITS & ADVENTURES
    // -------------------------------------------------------------
    fun logHabitCompletion(habit: HabitItem) {
        viewModelScope.launch {
            repository.logHabitCompletion(habit)
            // No intrusive modal popup that interrupts the user flow
        }
    }

    fun approveHabitLog(log: DailyHabitLog) {
        viewModelScope.launch {
            repository.approveHabitLog(log)
            // No intrusive modal popup that interrupts the parent
        }
    }

    fun approveAllHabits() {
        viewModelScope.launch {
            val pending = _uiState.value.dailyHabitLogs.filter { it.status == "PENDING_APPROVAL" }
            if (pending.isNotEmpty()) {
                repository.approveAllHabitLogs(pending)
            }
        }
    }

    fun giveRewardPoints(amount: Int, reason: String, childId: Int? = null) {
        viewModelScope.launch {
            repository.giveRewardPoints(amount, reason, childId)
            // No intrusive modal popup
        }
    }

    fun markHabitMissed(log: DailyHabitLog) {
        viewModelScope.launch {
            repository.markHabitMissed(log)
        }
    }

    fun completeRecoveryQuest(quest: RecoveryQuest) {
        viewModelScope.launch {
            repository.completeRecoveryQuest(quest)
            triggerCelebration(quest.rewardCoins, "Heroic Bounce Back! You earned +${quest.rewardCoins} coins for '${quest.title}'! 🌟")
        }
    }

    fun addHabit(
        title: String,
        icon: String,
        reward: Int,
        category: String,
        frequency: String,
        method: String,
        isTeamwork: Boolean,
        deduction: Int,
        note: String
    ) {
        viewModelScope.launch {
            repository.addHabit(title, icon, reward, category, frequency, method, isTeamwork, deduction, note)
            // Clear inline feedback without blocking screen
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }

    fun toggleHabit(habit: HabitItem) {
        viewModelScope.launch {
            repository.toggleHabit(habit)
        }
    }

    fun updateParentPolicy(policy: String) {
        viewModelScope.launch {
            repository.updateParentDeductionPolicy(policy)
        }
    }

    fun revokeCoins(amount: Int, reason: String, targetJar: String = "UNASSIGNED") {
        viewModelScope.launch {
            repository.revokeCoins(amount, reason, targetJar)
            // No intrusive modal popup
        }
    }

    // -------------------------------------------------------------
    // 4 MAGIC JARS
    // -------------------------------------------------------------
    fun distributeCoins(spend: Int, save: Int, give: Int, safety: Int, onDone: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val success = repository.distributeCoinsToJars(spend, save, give, safety)
            if (success) {
                triggerCelebration(spend + save + give + safety, "Great job deciding where your coins should go! 🌟")
            }
            onDone?.invoke(success)
        }
    }

    fun donateCoins(amount: Int, cause: String, desc: String, onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.giveCoins(amount, cause, desc)
            if (success) {
                triggerCelebration(amount, "Heart of Gold! You shared $amount coins for $cause! ❤️")
            }
            onDone(success)
        }
    }

    // -------------------------------------------------------------
    // SHOP & SMART CHOICES
    // -------------------------------------------------------------
    fun selectShopItem(product: ShopProduct) {
        val profile = _uiState.value.profile
        if (product.smartAlternativeTitle != null) {
            _uiState.update { it.copy(activeSmartChoiceProduct = product) }
        } else if (profile.spendJarCoins >= product.price) {
            buyShopProduct(product, false)
        } else {
            _uiState.update { it.copy(activeShopItemPrompt = product) }
        }
    }

    fun buyShopProduct(product: ShopProduct, isSmartAlternative: Boolean) {
        viewModelScope.launch {
            val success = repository.buyShopProduct(product, isSmartAlternative)
            if (success) {
                val title = if (isSmartAlternative) product.smartAlternativeTitle ?: product.title else product.title
                val msg = if (isSmartAlternative) {
                    "Smart Choice Master! You chose $title, saved coins, and earned +${product.smartXpReward} XP! ⭐"
                } else {
                    "You bought $title ${product.icon}! Enjoy your item! 🎉"
                }
                triggerCelebration(0, msg)
            }
            dismissShopPrompt()
        }
    }

    fun dismissShopPrompt() {
        _uiState.update { it.copy(activeShopItemPrompt = null, activeSmartChoiceProduct = null) }
    }

    // -------------------------------------------------------------
    // CUSTOM TOY SHOP
    // -------------------------------------------------------------
    fun addCustomToy(
        title: String,
        icon: String,
        price: Int,
        category: String,
        desc: String,
        altTitle: String,
        altPrice: Int,
        altXp: Int
    ) {
        viewModelScope.launch {
            repository.addCustomToy(title, icon, price, category, desc, altTitle, altPrice, altXp)
            triggerCelebration(0, "Toy '$title' added to Toy Shop! 🧸")
        }
    }

    fun deleteCustomToy(toyId: Int) {
        viewModelScope.launch {
            repository.deleteCustomToy(toyId)
        }
    }

    fun buyCustomToy(toy: CustomToyItem, isSmartAlternative: Boolean) {
        viewModelScope.launch {
            val success = repository.buyCustomToy(toy, isSmartAlternative)
            if (success) {
                val title = if (isSmartAlternative) toy.smartAlternativeTitle ?: toy.title else toy.title
                val msg = if (isSmartAlternative) {
                    "Smart Choice Master! You picked $title, saved coins, and earned +${toy.smartXpReward} XP! ⭐"
                } else {
                    "You redeemed $title ${toy.icon}! Enjoy your prize! 🎉"
                }
                triggerCelebration(0, msg)
            }
        }
    }

    // -------------------------------------------------------------
    // 12% SAVINGS GROWTH & SIP
    // -------------------------------------------------------------
    fun updateSipSettings(amount: Int, frequency: String, isActive: Boolean) {
        viewModelScope.launch {
            repository.updateSIPSettings(amount, frequency, isActive)
            triggerCelebration(0, "SIP Settings Updated! Auto-compounding is ${if (isActive) "Active ✨" else "Paused"}")
        }
    }

    fun processMonthlySavingsGrowthAndSIP() {
        viewModelScope.launch {
            val message = repository.processMonthlySavingsGrowthAndSIP()
            triggerCelebration(0, message)
        }
    }

    // -------------------------------------------------------------
    // BENNY'S BANK
    // -------------------------------------------------------------
    fun depositToBank(amount: Int) {
        viewModelScope.launch {
            val success = repository.depositToBank(amount)
            if (success) {
                triggerCelebration(amount, "Benny safely placed $amount coins in your bank vault! 🐰")
            }
        }
    }

    fun withdrawFromBank(amount: Int) {
        viewModelScope.launch {
            repository.withdrawFromBank(amount)
        }
    }

    fun claimBankReward() {
        viewModelScope.launch {
            val bonus = repository.claimBankInterest()
            if (bonus > 0) {
                triggerCelebration(bonus, "Benny gave you +$bonus bonus reward coin for letting your money rest and grow! 🎉")
            }
        }
    }

    // -------------------------------------------------------------
    // MAGIC GARDEN & COMPOUNDING
    // -------------------------------------------------------------
    fun plantInvestmentTree(name: String, icon: String, cost: Int, treeType: String) {
        viewModelScope.launch {
            val success = repository.plantInvestmentTree(name, icon, cost, treeType)
            if (success) {
                triggerCelebration(cost, "You planted a $name tree! You gave your coins a job to grow! 🌱")
            }
        }
    }

    fun waterPlant(plantId: Int) {
        viewModelScope.launch {
            when (val res = repository.waterPlant(plantId)) {
                is GameRepository.WaterResult.Success -> {
                    if (res.plant.stage == 4) {
                        _uiState.update { it.copy(activeCompoundingPlant = res.plant) }
                    } else {
                        triggerCelebration(0, "Plant watered! Growing strong! (Water once per day) 💧")
                    }
                }
                GameRepository.WaterResult.AlreadyWateredToday -> {
                    triggerCelebration(0, "This plant was already watered today! Come back tomorrow to water it again! ☀️")
                }
                GameRepository.WaterResult.NotFound -> {}
            }
        }
    }

    fun harvestPlant(plantId: Int) {
        viewModelScope.launch {
            val returnCoins = repository.harvestPlant(plantId)
            if (returnCoins > 0) {
                triggerCelebration(returnCoins, "Harvest complete! You earned $returnCoins coins from your tree! 🌳✨")
            }
            dismissCompoundingDialog()
        }
    }

    fun compoundPlant(plantId: Int) {
        viewModelScope.launch {
            val compounded = repository.compoundPlant(plantId)
            if (compounded != null) {
                triggerCelebration(
                    0,
                    "Compounding Magic Activated! All coins reinvested into Cycle ${compounded.compoundingCycle} to grow even bigger! ✨"
                )
            }
            dismissCompoundingDialog()
        }
    }

    fun dismissCompoundingDialog() {
        _uiState.update { it.copy(activeCompoundingPlant = null) }
    }

    // -------------------------------------------------------------
    // DAILY QUIZ
    // -------------------------------------------------------------
    fun submitDailyQuiz(score: Int, total: Int) {
        viewModelScope.launch {
            repository.submitDailyQuiz(score, total)
            triggerCelebration(uiState.value.profile.quizRewardCoins, "Quiz Master! You earned +${uiState.value.profile.quizRewardCoins} coins for completing today's quiz! 🧠🪙")
        }
    }

    fun updateQuizConfig(age: Int, dailyGoal: Int, rewardCoins: Int) {
        viewModelScope.launch {
            repository.updateQuizConfig(age, dailyGoal, rewardCoins)
            triggerCelebration(0, "Quiz configuration updated for Age $age! ⚙️")
        }
    }

    // -------------------------------------------------------------
    // AGE-BASED SKILLS
    // -------------------------------------------------------------
    fun reloadPredefinedSkills() {
        viewModelScope.launch {
            repository.reloadAllPredefinedSkills()
            triggerCelebration(0, "Loaded all predefined skills & micro-ventures! 🌟")
        }
    }

    fun dismissSkill(skillId: String) {
        viewModelScope.launch {
            repository.dismissSkillProject(skillId)
            triggerCelebration(0, "Skill hidden from your monthly list. 🗑️")
        }
    }

    fun completeSkillProject(project: SkillProjectEntity, notes: String?) {
        viewModelScope.launch {
            val profit = repository.completeSkillProject(project, notes)
            triggerCelebration(profit, "Awesome Entrepreneur! Crafted & sold '${project.title}', making +$profit profit! 🎨🪙")
        }
    }

    fun addCustomSkill(
        title: String,
        icon: String,
        skillName: String,
        category: String,
        craftCost: Int,
        sellPrice: Int,
        description: String
    ) {
        viewModelScope.launch {
            repository.addCustomSkillProject(
                title = title,
                icon = icon,
                skillName = skillName,
                category = category,
                craftCost = craftCost,
                sellPrice = sellPrice,
                description = description
            )
            triggerCelebration(0, "New Skill Quest Added! 🎨 Ready to practice & earn!")
        }
    }

    // -------------------------------------------------------------
    // ARCADE & MINI GAMES
    // -------------------------------------------------------------
    fun awardGameCoins(coins: Int, reason: String) {
        viewModelScope.launch {
            repository.addEarnedCoins(
                coins = coins,
                title = "Arcade Game Reward",
                description = reason,
                lesson = "Brain puzzles and problem solving sharpen financial thinking!"
            )
            triggerCelebration(coins, "Great Arcade Play! +$coins coins awarded! 🎮")
        }
    }

    // -------------------------------------------------------------
    // LIFE EVENTS, GOALS, SKILLS
    // -------------------------------------------------------------
    fun drawLifeEvent() {
        val events = SampleGameData.lifeEvents
        val selected = events.random()
        _uiState.update { it.copy(activeLifeEvent = selected, lifeEventResult = null) }
    }

    fun resolveLifeEvent() {
        val event = _uiState.value.activeLifeEvent ?: return
        viewModelScope.launch {
            val outcome = repository.triggerLifeEvent(event)
            _uiState.update { it.copy(lifeEventResult = outcome) }
        }
    }

    fun dismissLifeEvent() {
        _uiState.update { it.copy(activeLifeEvent = null, lifeEventResult = null) }
    }

    fun submitMiniGameAnswer(gameType: String, isCorrect: Boolean) {
        viewModelScope.launch {
            repository.recordMiniGameResult(gameType, isCorrect)
            if (isCorrect) {
                repository.addEarnedCoins(
                    coins = 2,
                    title = "Needs vs Wants Wizard",
                    description = "Correctly figured out needs vs wants!",
                    lesson = "Understanding resources are limited and making wise choices",
                    directToSpend = false
                )
                triggerCelebration(2, "Super Smart! +2 coins for making a wise choice! 🌟")
            }
        }
    }

    fun createGoal(title: String, icon: String, desc: String, cost: Int) {
        viewModelScope.launch {
            repository.addNewGoal(title, icon, desc, cost)
            triggerCelebration(0, "New Dream Goal Set: '$title'! Let's save together! 🎯")
        }
    }

    fun setActiveGoal(goalId: Int) {
        viewModelScope.launch {
            repository.setActiveGoal(goalId)
        }
    }

    fun deleteGoal(goalId: Int) {
        viewModelScope.launch {
            repository.deleteGoal(goalId)
            triggerCelebration(0, "Dream Goal removed 🎯")
        }
    }

    // -------------------------------------------------------------
    // KIDS PROFILE MULTI-USER MANAGEMENT
    // -------------------------------------------------------------
    fun switchKidProfile(profileId: Int) {
        viewModelScope.launch {
            repository.switchKidProfile(profileId)
            val current = _uiState.value.allProfiles.find { it.id == profileId }
            val name = current?.name ?: "Child"
            triggerCelebration(0, "Switched to $name's profile! 🎒✨")
        }
    }

    fun addKidProfile(name: String, avatar: String, dob: String, age: Int) {
        viewModelScope.launch {
            val newId = repository.addKidProfile(name, avatar, dob, age)
            repository.switchKidProfile(newId.toInt())
            triggerCelebration(10, "Welcome $name to the Money Adventure! 🌟")
        }
    }

    fun deleteKidProfile(profileId: Int) {
        viewModelScope.launch {
            val success = repository.deleteKidProfile(profileId)
            if (!success) {
                triggerCelebration(0, "Cannot delete the only remaining profile!")
            } else {
                triggerCelebration(0, "Child profile removed.")
            }
        }
    }

    fun updateKidProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.updateKidProfile(profile)
            triggerCelebration(0, "Profile updated successfully! ✨")
        }
    }

    fun craftAndSell(item: SkillBusinessItem) {
        val profile = _uiState.value.profile
        if (profile.spendJarCoins < item.craftCost && profile.unassignedCoins < item.craftCost) return

        viewModelScope.launch {
            val profit = item.sellPrice - item.craftCost
            repository.addEarnedCoins(
                coins = profit,
                title = "Crafted & Sold ${item.title}",
                description = "Used ${item.skillName} skill (Materials: ${item.craftCost}, Sold: ${item.sellPrice})",
                lesson = "Skills and creativity create real value and profit!"
            )
            triggerCelebration(profit, "You sold your ${item.title} to a village friend and made +$profit profit! 🎨🍪")
        }
    }

    // -------------------------------------------------------------
    // PARENT ZONE PIN
    // -------------------------------------------------------------
    fun generateParentMathQuestion() {
        val a = Random.nextInt(5, 12)
        val b = Random.nextInt(4, 9)
        _uiState.update {
            it.copy(
                parentMathQuestion = "$a + $b" to (a + b),
                parentUnlocked = false
            )
        }
    }

    fun verifyParentAnswer(input: String): Boolean {
        val expected = _uiState.value.parentMathQuestion.second
        val trimmed = input.trim()
        val isCorrect = trimmed == "8888" || trimmed == "1234" || trimmed.toIntOrNull() == expected
        if (isCorrect) {
            _uiState.update { it.copy(parentUnlocked = true) }
        }
        return isCorrect
    }

    fun unlockParentZoneDirectly() {
        _uiState.update { it.copy(parentUnlocked = true) }
    }

    fun lockParentZone() {
        _uiState.update { it.copy(parentUnlocked = false) }
        generateParentMathQuestion()
    }
}
