package com.example.data.repository

import com.example.data.local.MoneyAdventureDatabase
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GameRepository(private val db: MoneyAdventureDatabase) {

    val allProfiles: Flow<List<UserProfile>> = db.userDao().getAllProfiles()
    val userProfile: Flow<UserProfile?> = db.userDao().getUserProfile()
    val allGoals: Flow<List<GoalItem>> = db.goalDao().getAllGoals()
    val activePlants: Flow<List<GardenPlant>> = db.gardenDao().getActivePlants()
    val allLogs: Flow<List<ActivityLog>> = db.activityLogDao().getAllLogs()
    val miniGameStats: Flow<List<MiniGameStat>> = db.miniGameStatDao().getAllStats()
    val allHabits: Flow<List<HabitItem>> = db.habitDao().getAllHabits()
    val enabledHabits: Flow<List<HabitItem>> = db.habitDao().getEnabledHabits()
    val allDailyHabitLogs: Flow<List<DailyHabitLog>> = db.habitDao().getAllDailyLogs()
    val allCustomToys: Flow<List<CustomToyItem>> = db.customToyDao().getAllToys()
    val activeSkillProjects: Flow<List<SkillProjectEntity>> = db.skillProjectDao().getActiveProjects()
    val completedSkillPortfolio: Flow<List<SkillProjectEntity>> = db.skillProjectDao().getCompletedPortfolio()
    val quizDailyHistory: Flow<List<QuizDailyHistory>> = db.quizDailyHistoryDao().getAllHistory()

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    suspend fun getProfileOnce(): UserProfile = withContext(Dispatchers.IO) {
        db.userDao().getUserProfileOnce() ?: UserProfile()
    }

    // -------------------------------------------------------------
    // INITIAL DATABASE SEEDING
    // -------------------------------------------------------------
    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val habits = db.habitDao().getAllHabitsOnce()
        if (habits.isEmpty()) {
            SampleGameData.initialHabits.forEach { db.habitDao().insertHabit(it) }
            SampleGameData.sampleDemeritHabits.forEach { db.habitDao().insertHabit(it) }
        }
        val profiles = db.userDao().getAllProfilesOnce()
        if (profiles.isEmpty()) {
            db.userDao().insertOrUpdate(
                UserProfile(
                    id = 1,
                    name = "Leo",
                    avatar = "🦊",
                    dateOfBirth = "2018-06-15",
                    childAge = 8,
                    unassignedCoins = 10,
                    spendJarCoins = 6,
                    saveJarCoins = 10,
                    giveJarCoins = 4,
                    safetyJarCoins = 5,
                    bankCoins = 5,
                    totalCoinsEarned = 35,
                    level = 1,
                    levelTitle = "Level 1: My First Coins",
                    activeGoalId = 1,
                    parentDeductionPolicy = "NONE",
                    smartChoiceXp = 20,
                    smartChoiceBadge = "Smart Spender 🥉",
                    isCurrentActive = true
                )
            )
            db.userDao().insertOrUpdate(
                UserProfile(
                    id = 2,
                    name = "Mia",
                    avatar = "🦄",
                    dateOfBirth = "2021-03-20",
                    childAge = 5,
                    unassignedCoins = 8,
                    spendJarCoins = 2,
                    saveJarCoins = 4,
                    giveJarCoins = 2,
                    safetyJarCoins = 2,
                    bankCoins = 0,
                    totalCoinsEarned = 16,
                    level = 1,
                    levelTitle = "Little Explorer",
                    activeGoalId = 1,
                    parentDeductionPolicy = "NONE",
                    smartChoiceXp = 10,
                    smartChoiceBadge = "Kind Giver 🌱",
                    isCurrentActive = false
                )
            )
        }
        // Seed default toys
        val defaultToys = SampleGameData.initialParentToys
        defaultToys.forEach { toy ->
            if (db.customToyDao().getToyById(toy.id) == null) {
                db.customToyDao().insertToy(toy)
            }
        }
        // Seed all age-based skills so catalog is always populated
        SampleGameData.ageWiseSkillsCatalog.forEach { skill ->
            val existing = db.skillProjectDao().getProjectBySkillId(skill.id)
            if (existing == null) {
                db.skillProjectDao().insertProject(
                    SkillProjectEntity(
                        skillId = skill.id,
                        title = skill.title,
                        icon = skill.icon,
                        skillName = skill.skillName,
                        category = skill.category,
                        craftCost = skill.craftCost,
                        sellPrice = skill.sellPrice,
                        minAge = skill.minAge,
                        maxAge = skill.maxAge,
                        description = skill.description,
                        isDismissed = false,
                        isCompleted = false,
                        completedTimes = 0
                    )
                )
            }
        }
    }

    suspend fun syncSkillsForAge(childAge: Int) = withContext(Dispatchers.IO) {
        // Ensure catalog skills are present in DB
        SampleGameData.ageWiseSkillsCatalog.forEach { skill ->
            val existing = db.skillProjectDao().getProjectBySkillId(skill.id)
            if (existing == null) {
                db.skillProjectDao().insertProject(
                    SkillProjectEntity(
                        skillId = skill.id,
                        title = skill.title,
                        icon = skill.icon,
                        skillName = skill.skillName,
                        category = skill.category,
                        craftCost = skill.craftCost,
                        sellPrice = skill.sellPrice,
                        minAge = skill.minAge,
                        maxAge = skill.maxAge,
                        description = skill.description,
                        isDismissed = false,
                        isCompleted = false,
                        completedTimes = 0
                    )
                )
            }
        }
    }

    suspend fun reloadAllPredefinedSkills() = withContext(Dispatchers.IO) {
        SampleGameData.ageWiseSkillsCatalog.forEach { skill ->
            val existing = db.skillProjectDao().getProjectBySkillId(skill.id)
            if (existing == null) {
                db.skillProjectDao().insertProject(
                    SkillProjectEntity(
                        skillId = skill.id,
                        title = skill.title,
                        icon = skill.icon,
                        skillName = skill.skillName,
                        category = skill.category,
                        craftCost = skill.craftCost,
                        sellPrice = skill.sellPrice,
                        minAge = skill.minAge,
                        maxAge = skill.maxAge,
                        description = skill.description,
                        isDismissed = false,
                        isCompleted = false,
                        completedTimes = 0
                    )
                )
            } else if (existing.isDismissed) {
                db.skillProjectDao().updateProject(existing.copy(isDismissed = false))
            }
        }
    }

    // -------------------------------------------------------------
    // MULTI-CHILD PROFILE MANAGEMENT
    // -------------------------------------------------------------
    suspend fun addKidProfile(name: String, avatar: String, dob: String, age: Int): Long = withContext(Dispatchers.IO) {
        val newProfile = UserProfile(
            id = 0,
            name = name.ifBlank { "Child" },
            avatar = avatar.ifBlank { "⭐" },
            dateOfBirth = dob,
            childAge = age.coerceIn(3, 18),
            unassignedCoins = 10,
            spendJarCoins = 4,
            saveJarCoins = 6,
            giveJarCoins = 2,
            safetyJarCoins = 2,
            bankCoins = 0,
            totalCoinsEarned = 24,
            level = 1,
            levelTitle = "Level 1: My First Coins",
            smartChoiceXp = 10,
            smartChoiceBadge = "Smart Spender 🥉",
            isCurrentActive = false
        )
        val id = db.userDao().insertOrUpdate(newProfile)
        id
    }

    suspend fun switchKidProfile(profileId: Int) = withContext(Dispatchers.IO) {
        db.userDao().clearActiveFlag()
        db.userDao().setActiveProfile(profileId)
        val profile = db.userDao().getUserProfileById(profileId)
        if (profile != null) {
            syncSkillsForAge(profile.childAge)
        }
    }

    suspend fun updateKidProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        db.userDao().update(profile)
        syncSkillsForAge(profile.childAge)
    }

    suspend fun deleteKidProfile(profileId: Int): Boolean = withContext(Dispatchers.IO) {
        val all = db.userDao().getAllProfilesOnce()
        if (all.size <= 1) return@withContext false // Always preserve at least 1 child profile
        val target = db.userDao().getUserProfileById(profileId)
        db.userDao().deleteProfile(profileId)
        if (target?.isCurrentActive == true) {
            val remaining = db.userDao().getAllProfilesOnce()
            remaining.firstOrNull()?.let { first ->
                db.userDao().setActiveProfile(first.id)
            }
        }
        true
    }

    // -------------------------------------------------------------
    // HABITS & PARENT-POWERED SYSTEM
    // -------------------------------------------------------------
    suspend fun addHabit(
        title: String,
        icon: String,
        rewardCoins: Int,
        category: String,
        frequency: String,
        confirmationMethod: String,
        isFamilyTeamwork: Boolean,
        deductionCoins: Int,
        note: String,
        isNegative: Boolean = false
    ) = withContext(Dispatchers.IO) {
        val habit = HabitItem(
            title = title,
            icon = icon,
            rewardCoins = if (isFamilyTeamwork || isNegative) 0 else rewardCoins,
            category = category,
            frequency = frequency,
            confirmationMethod = confirmationMethod,
            isFamilyTeamwork = isFamilyTeamwork,
            isNegative = isNegative,
            deductionCoins = deductionCoins,
            isEnabled = true,
            note = note
        )
        db.habitDao().insertHabit(habit)
    }

    suspend fun toggleHabit(habit: HabitItem) = withContext(Dispatchers.IO) {
        val updated = habit.copy(isEnabled = !habit.isEnabled)
        db.habitDao().updateHabit(updated)
    }

    suspend fun deleteHabit(habitId: Int) = withContext(Dispatchers.IO) {
        db.habitDao().deleteHabitById(habitId)
    }

    suspend fun logHabitCompletion(habit: HabitItem): Boolean = withContext(Dispatchers.IO) {
        val today = getTodayDateString()
        val existing = db.habitDao().getLogForDateAndHabit(today, habit.id)
        if (existing != null && (existing.status == "APPROVED" || existing.status == "PENDING_APPROVAL" || existing.status == "DEMERIT")) {
            return@withContext false
        }

        if (habit.isNegative) {
            // Negative habit / demerit log
            val log = DailyHabitLog(
                habitId = habit.id,
                dateString = today,
                title = habit.title,
                icon = habit.icon,
                rewardCoins = 0,
                category = habit.category,
                status = "DEMERIT",
                isNegative = true,
                completedAt = System.currentTimeMillis(),
                approvedAt = System.currentTimeMillis()
            )
            db.habitDao().insertDailyLog(log)

            val current = getProfileOnce()
            val deduction = habit.deductionCoins.coerceAtLeast(1)
            val updated = current.copy(
                unassignedCoins = (current.unassignedCoins - deduction).coerceAtLeast(0)
            )
            db.userDao().insertOrUpdate(updated)

            db.activityLogDao().insertLog(
                ActivityLog(
                    type = "DEMERIT",
                    title = "Negative Habit Logged: ${habit.title} ${habit.icon}",
                    description = "Deducted -$deduction coin(s). Complete a Recovery Quest or healthy habit to bounce back!",
                    coinsChanged = -deduction,
                    jarName = "UNASSIGNED",
                    lesson = "Discipline and healthy choices protect our energy and coins!"
                )
            )
            return@withContext true
        }

        val status = if (habit.confirmationMethod == "INSTANT" || habit.isFamilyTeamwork) {
            "APPROVED"
        } else {
            "PENDING_APPROVAL"
        }

        val log = DailyHabitLog(
            habitId = habit.id,
            dateString = today,
            title = habit.title,
            icon = habit.icon,
            rewardCoins = habit.rewardCoins,
            category = habit.category,
            status = status,
            isNegative = false,
            completedAt = System.currentTimeMillis(),
            approvedAt = if (status == "APPROVED") System.currentTimeMillis() else null
        )
        db.habitDao().insertDailyLog(log)

        if (status == "APPROVED" && habit.rewardCoins > 0) {
            val current = getProfileOnce()
            val updated = current.copy(
                unassignedCoins = current.unassignedCoins + habit.rewardCoins,
                totalCoinsEarned = current.totalCoinsEarned + habit.rewardCoins
            )
            val leveled = recalculateLevel(updated)
            db.userDao().insertOrUpdate(leveled)

            db.activityLogDao().insertLog(
                ActivityLog(
                    type = "EARN",
                    title = "Completed Habit: ${habit.title} ${habit.icon}",
                    description = "Earned +${habit.rewardCoins} coins for positive routine discipline.",
                    coinsChanged = habit.rewardCoins,
                    jarName = "UNASSIGNED",
                    lesson = "I did something useful ➡️ I earned something ➡️ Now I decide where it goes!"
                )
            )
        }
        return@withContext true
    }

    suspend fun approveHabitLog(log: DailyHabitLog) = withContext(Dispatchers.IO) {
        val updatedLog = log.copy(status = "APPROVED", approvedAt = System.currentTimeMillis())
        db.habitDao().updateDailyLog(updatedLog)

        if (log.rewardCoins > 0) {
            val current = getProfileOnce()
            val updated = current.copy(
                unassignedCoins = current.unassignedCoins + log.rewardCoins,
                totalCoinsEarned = current.totalCoinsEarned + log.rewardCoins
            )
            val leveled = recalculateLevel(updated)
            db.userDao().insertOrUpdate(leveled)

            db.activityLogDao().insertLog(
                ActivityLog(
                    type = "EARN",
                    title = "Parent Approved: ${log.title} ${log.icon}",
                    description = "Earned +${log.rewardCoins} coins from verified daily habit completion.",
                    coinsChanged = log.rewardCoins,
                    jarName = "UNASSIGNED",
                    lesson = "Consistency and high-fives create value!"
                )
            )
        }
    }

    suspend fun approveAllHabitLogs(logs: List<DailyHabitLog>) = withContext(Dispatchers.IO) {
        if (logs.isEmpty()) return@withContext
        val now = System.currentTimeMillis()
        var totalCoinsAwarded = 0
        for (log in logs) {
            val updatedLog = log.copy(status = "APPROVED", approvedAt = now)
            db.habitDao().updateDailyLog(updatedLog)
            if (log.rewardCoins > 0) {
                totalCoinsAwarded += log.rewardCoins
                db.activityLogDao().insertLog(
                    ActivityLog(
                        type = "EARN",
                        title = "Parent Approved: ${log.title} ${log.icon}",
                        description = "Earned +${log.rewardCoins} coins from verified daily habit completion.",
                        coinsChanged = log.rewardCoins,
                        jarName = "UNASSIGNED",
                        lesson = "Consistency and high-fives create value!"
                    )
                )
            }
        }
        if (totalCoinsAwarded > 0) {
            val current = getProfileOnce()
            val updated = current.copy(
                unassignedCoins = current.unassignedCoins + totalCoinsAwarded,
                totalCoinsEarned = current.totalCoinsEarned + totalCoinsAwarded
            )
            val leveled = recalculateLevel(updated)
            db.userDao().insertOrUpdate(leveled)
        }
    }

    suspend fun giveRewardPoints(amount: Int, reason: String, childId: Int? = null) = withContext(Dispatchers.IO) {
        if (amount <= 0) return@withContext
        val targetProfile = if (childId != null) {
            db.userDao().getUserProfileById(childId) ?: getProfileOnce()
        } else {
            getProfileOnce()
        }
        val updated = targetProfile.copy(
            unassignedCoins = targetProfile.unassignedCoins + amount,
            totalCoinsEarned = targetProfile.totalCoinsEarned + amount
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "EARN",
                title = "Parent Awarded Points: +$amount 🪙",
                description = "Parent reward: $reason",
                coinsChanged = amount,
                jarName = "UNASSIGNED",
                lesson = "Hard work, kindness, and great effort are recognized!"
            )
        )
    }

    suspend fun markHabitMissed(log: DailyHabitLog) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updatedLog = log.copy(status = "MISSED")
        db.habitDao().updateDailyLog(updatedLog)

        if (current.parentDeductionPolicy == "DEDUCT") {
            val deduction = 1
            val updated = current.copy(
                unassignedCoins = (current.unassignedCoins - deduction).coerceAtLeast(0)
            )
            db.userDao().insertOrUpdate(updated)

            db.activityLogDao().insertLog(
                ActivityLog(
                    type = "RECOVERY",
                    title = "Missed Routine: ${log.title}",
                    description = "Small $deduction coin adjustment. You can bounce back with a Recovery Quest!",
                    coinsChanged = -deduction,
                    jarName = "UNASSIGNED",
                    lesson = "Mistakes are okay! We learn, adjust, and complete Recovery Quests."
                )
            )
        }
    }

    suspend fun updateParentDeductionPolicy(policy: String) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updated = current.copy(parentDeductionPolicy = policy)
        db.userDao().insertOrUpdate(updated)
    }

    suspend fun revokeCoins(
        amount: Int,
        reason: String,
        targetJar: String = "UNASSIGNED"
    ): Boolean = withContext(Dispatchers.IO) {
        if (amount <= 0) return@withContext false
        val current = getProfileOnce()

        var remainingToDeduct = amount
        var unassigned = current.unassignedCoins
        var spend = current.spendJarCoins
        var save = current.saveJarCoins
        var give = current.giveJarCoins
        var safety = current.safetyJarCoins

        when (targetJar) {
            "SPEND" -> {
                val ded = remainingToDeduct.coerceAtMost(spend)
                spend -= ded
                remainingToDeduct -= ded
            }
            "SAVE" -> {
                val ded = remainingToDeduct.coerceAtMost(save)
                save -= ded
                remainingToDeduct -= ded
            }
            "GIVE" -> {
                val ded = remainingToDeduct.coerceAtMost(give)
                give -= ded
                remainingToDeduct -= ded
            }
            "SAFETY" -> {
                val ded = remainingToDeduct.coerceAtMost(safety)
                safety -= ded
                remainingToDeduct -= ded
            }
            else -> {
                val ded = remainingToDeduct.coerceAtMost(unassigned)
                unassigned -= ded
                remainingToDeduct -= ded
            }
        }

        // Cascade to other balances if needed
        if (remainingToDeduct > 0 && unassigned > 0) {
            val ded = remainingToDeduct.coerceAtMost(unassigned)
            unassigned -= ded
            remainingToDeduct -= ded
        }
        if (remainingToDeduct > 0 && spend > 0) {
            val ded = remainingToDeduct.coerceAtMost(spend)
            spend -= ded
            remainingToDeduct -= ded
        }
        if (remainingToDeduct > 0 && save > 0) {
            val ded = remainingToDeduct.coerceAtMost(save)
            save -= ded
            remainingToDeduct -= ded
        }

        val actualDeducted = amount - remainingToDeduct
        val updated = current.copy(
            unassignedCoins = unassigned,
            spendJarCoins = spend,
            saveJarCoins = save,
            giveJarCoins = give,
            safetyJarCoins = safety,
            totalCoinsEarned = (current.totalCoinsEarned - actualDeducted).coerceAtLeast(0)
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "REVOKE",
                title = "Parent Revoked Coins: -$amount 🪙",
                description = "Parent adjustment: $reason",
                coinsChanged = -actualDeducted,
                jarName = targetJar,
                lesson = "Integrity is true wealth. We earn real value by actually completing our responsibilities."
            )
        )
        return@withContext true
    }

    suspend fun completeRecoveryQuest(quest: RecoveryQuest) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updated = current.copy(
            unassignedCoins = current.unassignedCoins + quest.rewardCoins,
            totalCoinsEarned = current.totalCoinsEarned + quest.rewardCoins
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "RECOVERY",
                title = "Heroic Recovery: ${quest.title} ${quest.icon}",
                description = "Earned +${quest.rewardCoins} coins for solving a challenge and helping home!",
                coinsChanged = quest.rewardCoins,
                jarName = "UNASSIGNED",
                lesson = "Resilience: when things don't go as planned, we can always bounce back!"
            )
        )
    }

    // -------------------------------------------------------------
    // PARENT CUSTOM TOY SHOP
    // -------------------------------------------------------------
    suspend fun addCustomToy(
        title: String,
        icon: String,
        price: Int,
        category: String,
        description: String,
        smartAlternativeTitle: String,
        smartAlternativePrice: Int,
        smartXpReward: Int
    ) = withContext(Dispatchers.IO) {
        val toy = CustomToyItem(
            title = title,
            icon = icon,
            price = price,
            category = category,
            description = description,
            smartAlternativeTitle = smartAlternativeTitle,
            smartAlternativePrice = smartAlternativePrice,
            smartXpReward = smartXpReward
        )
        db.customToyDao().insertToy(toy)
    }

    suspend fun updateCustomToy(toy: CustomToyItem) = withContext(Dispatchers.IO) {
        db.customToyDao().updateToy(toy)
    }

    suspend fun deleteCustomToy(toyId: Int) = withContext(Dispatchers.IO) {
        db.customToyDao().deleteToyById(toyId)
    }

    suspend fun buyCustomToy(toy: CustomToyItem, isSmartAlternative: Boolean): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val cost = if (isSmartAlternative && toy.smartAlternativePrice != null) toy.smartAlternativePrice else toy.price
        val itemTitle = if (isSmartAlternative && !toy.smartAlternativeTitle.isNullOrBlank()) toy.smartAlternativeTitle else toy.title

        if (current.spendJarCoins < cost) return@withContext false

        val smartXpGain = if (isSmartAlternative) toy.smartXpReward else 0
        val newXp = current.smartChoiceXp + smartXpGain
        val newBadge = when {
            newXp >= 100 -> "Master of Value 👑"
            newXp >= 60 -> "Wise Decider 🥇"
            newXp >= 30 -> "Smart Spender 🥈"
            else -> "Smart Spender 🥉"
        }

        val updated = current.copy(
            spendJarCoins = current.spendJarCoins - cost,
            smartChoiceXp = newXp,
            smartChoiceBadge = newBadge
        )
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = if (isSmartAlternative) "SMART_CHOICE" else "SPEND",
                title = if (isSmartAlternative) "Smart Toy Choice: $itemTitle" else "Toy Purchased: $itemTitle",
                description = if (isSmartAlternative) {
                    "Saved ${toy.price - cost} coins by picking the smart alternative and gained +$smartXpGain XP!"
                } else {
                    "Redeemed $cost coins from Spend Jar for $itemTitle."
                },
                coinsChanged = -cost,
                jarName = "SPEND",
                lesson = "Choosing thoughtfully lets us afford our biggest dreams!"
            )
        )
        return@withContext true
    }

    suspend fun buyShopProduct(product: ShopProduct, isSmartAlternative: Boolean): Boolean = withContext(Dispatchers.IO) {
        val cost = if (isSmartAlternative && product.smartAlternativePrice != null) product.smartAlternativePrice else product.price
        val itemTitle = if (isSmartAlternative && !product.smartAlternativeTitle.isNullOrBlank()) product.smartAlternativeTitle else product.title
        val current = getProfileOnce()

        if (current.spendJarCoins < cost) return@withContext false

        val smartXpGain = if (isSmartAlternative) product.smartXpReward else 0
        val newXp = current.smartChoiceXp + smartXpGain
        val newBadge = when {
            newXp >= 100 -> "Master of Value 👑"
            newXp >= 60 -> "Wise Decider 🥇"
            newXp >= 30 -> "Smart Spender 🥈"
            else -> "Smart Spender 🥉"
        }

        val updated = current.copy(
            spendJarCoins = current.spendJarCoins - cost,
            smartChoiceXp = newXp,
            smartChoiceBadge = newBadge
        )
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = if (isSmartAlternative) "SMART_CHOICE" else "SPEND",
                title = if (isSmartAlternative) "Smart Choice: $itemTitle" else "Purchased: $itemTitle",
                description = if (isSmartAlternative) {
                    "Saved ${product.price - cost} coins by choosing smart alternative and gained +$smartXpGain XP!"
                } else {
                    "Spent $cost coins from Spend Jar for $itemTitle."
                },
                coinsChanged = -cost,
                jarName = "SPEND",
                lesson = "Spend with intention and value what you choose!"
            )
        )
        return@withContext true
    }

    // -------------------------------------------------------------
    // SKILLS & MICRO-VENTURE SYSTEM
    // -------------------------------------------------------------
    suspend fun dismissSkillProject(skillId: String) = withContext(Dispatchers.IO) {
        db.skillProjectDao().dismissSkill(skillId)
    }

    suspend fun addCustomSkillProject(
        title: String,
        icon: String,
        skillName: String,
        category: String,
        craftCost: Int,
        sellPrice: Int,
        description: String
    ) = withContext(Dispatchers.IO) {
        val id = "custom_skill_${System.currentTimeMillis()}"
        val project = SkillProjectEntity(
            skillId = id,
            title = title,
            icon = icon,
            skillName = skillName,
            category = category,
            craftCost = craftCost,
            sellPrice = sellPrice,
            minAge = 3,
            maxAge = 18,
            description = description,
            isDismissed = false,
            isCompleted = false,
            completedTimes = 0
        )
        db.skillProjectDao().insertProject(project)
    }

    suspend fun completeSkillProject(project: SkillProjectEntity, notes: String? = null, photoUri: String? = null): Int = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val netEarned = (project.sellPrice - project.craftCost).coerceAtLeast(1)

        val updatedProject = project.copy(
            isCompleted = true,
            completedTimes = project.completedTimes + 1,
            completedAt = System.currentTimeMillis(),
            proofPhotoUri = photoUri ?: project.proofPhotoUri,
            userNotes = notes ?: project.userNotes
        )
        db.skillProjectDao().updateProject(updatedProject)

        val updatedProfile = current.copy(
            unassignedCoins = current.unassignedCoins + netEarned,
            totalCoinsEarned = current.totalCoinsEarned + netEarned
        )
        val leveled = recalculateLevel(updatedProfile)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "SKILL",
                title = "Crafted & Sold: ${project.title} ${project.icon}",
                description = "Craft Cost: ${project.craftCost}c | Sold for: ${project.sellPrice}c | Net Profit: +${netEarned}c!",
                coinsChanged = netEarned,
                jarName = "UNASSIGNED",
                lesson = "Real Skill = Real Value! Turning raw effort into goods people love builds entrepreneurial confidence."
            )
        )
        return@withContext netEarned
    }

    // -------------------------------------------------------------
    // DAILY QUIZ SYSTEM (10 DAILY, AGE-MAPPED)
    // -------------------------------------------------------------
    suspend fun getTodayQuizHistory(): QuizDailyHistory? = withContext(Dispatchers.IO) {
        db.quizDailyHistoryDao().getHistoryForDate(getTodayDateString())
    }

    suspend fun submitDailyQuiz(score: Int, totalQuestions: Int): Int = withContext(Dispatchers.IO) {
        val today = getTodayDateString()
        val current = getProfileOnce()
        val rewardAmount = if (score >= (totalQuestions / 2)) current.quizRewardCoins else (current.quizRewardCoins / 2).coerceAtLeast(1)

        val history = QuizDailyHistory(
            dateString = today,
            score = score,
            totalQuestions = totalQuestions,
            coinsEarned = rewardAmount,
            completedAt = System.currentTimeMillis(),
            childAge = current.childAge
        )
        db.quizDailyHistoryDao().insertHistory(history)

        val updated = current.copy(
            unassignedCoins = current.unassignedCoins + rewardAmount,
            totalCoinsEarned = current.totalCoinsEarned + rewardAmount
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "QUIZ",
                title = "Daily Brain Quiz Completed ($score/$totalQuestions) 🧠",
                description = "Scored $score out of $totalQuestions. Rewarded +$rewardAmount shiny coins!",
                coinsChanged = rewardAmount,
                jarName = "UNASSIGNED",
                lesson = "Exercising your brain with daily quizzes expands wisdom and financial knowledge!"
            )
        )
        return@withContext rewardAmount
    }

    suspend fun updateQuizConfig(childAge: Int, dailyGoal: Int, rewardCoins: Int) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updated = current.copy(
            childAge = childAge,
            quizDailyGoal = dailyGoal,
            quizRewardCoins = rewardCoins
        )
        db.userDao().insertOrUpdate(updated)
        syncSkillsForAge(childAge)
    }

    // -------------------------------------------------------------
    // 12% ANNUAL SAVINGS GROWTH & SIP AUTOMATION
    // -------------------------------------------------------------
    suspend fun updateSIPSettings(sipAmount: Int, sipFrequency: String, isSipActive: Boolean) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updated = current.copy(
            sipAmount = sipAmount,
            sipFrequency = sipFrequency,
            isSipActive = isSipActive
        )
        db.userDao().insertOrUpdate(updated)
    }

    suspend fun processMonthlySavingsGrowthAndSIP(): String = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val principal = current.saveJarCoins + current.bankCoins
        // 12% Annual = 1% Monthly interest
        val monthlyInterest = (principal * 0.01f).toInt().coerceAtLeast(if (principal >= 10) 1 else 0)

        // Process SIP if active
        var sipTransferred = 0
        if (current.isSipActive && current.sipAmount > 0) {
            val available = current.unassignedCoins
            sipTransferred = current.sipAmount.coerceAtMost(available)
        }

        val updated = current.copy(
            saveJarCoins = current.saveJarCoins + monthlyInterest + sipTransferred,
            unassignedCoins = (current.unassignedCoins - sipTransferred).coerceAtLeast(0),
            totalCoinsEarned = current.totalCoinsEarned + monthlyInterest,
            lastMonthlyGrowthCalculated = System.currentTimeMillis()
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        if (monthlyInterest > 0 || sipTransferred > 0) {
            db.activityLogDao().insertLog(
                ActivityLog(
                    type = "INVEST",
                    title = "12% Annual Growth & SIP Applied 📈",
                    description = "Monthly 1% Compound Interest: +$monthlyInterest coins | SIP Added: +$sipTransferred coins into Save Jar!",
                    coinsChanged = monthlyInterest,
                    jarName = "SAVE",
                    lesson = "Compounding Growth (12% per year): When your savings sit patiently, they work for you automatically every single month!"
                )
            )
        }

        return@withContext "Grew +$monthlyInterest interest (12% APR) and auto-invested $sipTransferred SIP coins into your Save Jar!"
    }

    // -------------------------------------------------------------
    // 4 MAGIC JARS & WALLET
    // -------------------------------------------------------------
    suspend fun addEarnedCoins(
        coins: Int,
        title: String,
        description: String,
        lesson: String,
        directToSpend: Boolean = false
    ) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updated = if (directToSpend) {
            current.copy(
                spendJarCoins = current.spendJarCoins + coins,
                totalCoinsEarned = current.totalCoinsEarned + coins
            )
        } else {
            current.copy(
                unassignedCoins = current.unassignedCoins + coins,
                totalCoinsEarned = current.totalCoinsEarned + coins
            )
        }
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "EARN",
                title = title,
                description = description,
                coinsChanged = coins,
                jarName = if (directToSpend) "SPEND" else "UNASSIGNED",
                lesson = lesson
            )
        )
    }

    suspend fun distributeCoinsToJars(spend: Int, save: Int, give: Int, safety: Int): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val totalToDistribute = spend + save + give + safety
        if (totalToDistribute <= 0 || totalToDistribute > current.unassignedCoins) {
            return@withContext false
        }

        var newSaveTotal = current.saveJarCoins + save

        // Sync with active goal if active
        current.activeGoalId?.let { goalId ->
            val goal = db.goalDao().getGoalById(goalId)
            if (goal != null && !goal.isCompleted && save > 0) {
                val updatedCurrent = (goal.currentCoins + save).coerceAtMost(goal.targetCost)
                val isDone = updatedCurrent >= goal.targetCost
                db.goalDao().updateGoal(
                    goal.copy(
                        currentCoins = updatedCurrent,
                        isCompleted = isDone,
                        completedAt = if (isDone) System.currentTimeMillis() else null
                    )
                )
            }
        }

        val updated = current.copy(
            unassignedCoins = current.unassignedCoins - totalToDistribute,
            spendJarCoins = current.spendJarCoins + spend,
            saveJarCoins = newSaveTotal,
            giveJarCoins = current.giveJarCoins + give,
            safetyJarCoins = current.safetyJarCoins + safety
        )
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "SAVE",
                title = "Allocated $totalToDistribute Coins to Jars",
                description = "🎮 Spend: +$spend, 🐷 Save: +$save, ❤️ Give: +$give, 🛟 Safety: +$safety",
                coinsChanged = 0,
                jarName = "ALL_JARS",
                lesson = "I make choices with my money! Allocating purposefully builds financial independence."
            )
        )
        return@withContext true
    }

    suspend fun spendCoins(amount: Int, itemTitle: String, lesson: String): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        if (current.spendJarCoins < amount) return@withContext false

        val updated = current.copy(spendJarCoins = current.spendJarCoins - amount)
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "SPEND",
                title = "Bought $itemTitle",
                description = "Spent $amount coins from the Spend Jar.",
                coinsChanged = -amount,
                jarName = "SPEND",
                lesson = lesson
            )
        )
        return@withContext true
    }

    suspend fun giveCoins(amount: Int, causeTitle: String, description: String): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        if (current.giveJarCoins < amount) return@withContext false

        val updated = current.copy(giveJarCoins = current.giveJarCoins - amount)
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "GIVE",
                title = "Gave to $causeTitle ❤️",
                description = description,
                coinsChanged = -amount,
                jarName = "GIVE",
                lesson = "Sharing money and helping others makes our community warm and happy."
            )
        )
        return@withContext true
    }

    // -------------------------------------------------------------
    // BENNY'S BANK & VAULT
    // -------------------------------------------------------------
    suspend fun depositToBank(amount: Int): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        if (current.saveJarCoins < amount) return@withContext false

        val updated = current.copy(
            saveJarCoins = current.saveJarCoins - amount,
            bankCoins = current.bankCoins + amount
        )
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "BANK",
                title = "Deposited $amount Coins into Benny's Vault 🏦",
                description = "Kept safe in the bank vault where it can earn 12% compound growth.",
                coinsChanged = 0,
                jarName = "BANK",
                lesson = "Banks keep our savings secure from being accidentally spent."
            )
        )
        return@withContext true
    }

    suspend fun withdrawFromBank(amount: Int): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        if (current.bankCoins < amount) return@withContext false

        val updated = current.copy(
            bankCoins = current.bankCoins - amount,
            saveJarCoins = current.saveJarCoins + amount
        )
        db.userDao().insertOrUpdate(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "BANK",
                title = "Withdrew $amount Coins from Bank 🏦",
                description = "Returned coins from the vault to the Save Jar.",
                coinsChanged = 0,
                jarName = "BANK",
                lesson = "You have the freedom to access your savings whenever you need them."
            )
        )
        return@withContext true
    }

    suspend fun claimBankInterest(): Int = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        if (current.bankCoins <= 0) return@withContext 0

        val bonusInterest = (current.bankCoins / 8).coerceAtLeast(1)
        val updated = current.copy(
            bankCoins = current.bankCoins + bonusInterest,
            totalCoinsEarned = current.totalCoinsEarned + bonusInterest,
            bankLastInterestTime = System.currentTimeMillis()
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "BANK",
                title = "Earned +$bonusInterest Interest Reward! 🪙",
                description = "Benny's Bank rewarded you for letting money grow in the vault.",
                coinsChanged = bonusInterest,
                jarName = "BANK",
                lesson = "Interest is money earned simply by letting savings rest in a safe bank!"
            )
        )
        return@withContext bonusInterest
    }

    // -------------------------------------------------------------
    // MAGIC GARDEN & 1-WATER-PER-DAY CONSTRAINT
    // -------------------------------------------------------------
    suspend fun plantInvestmentTree(name: String, icon: String, cost: Int, treeType: String): Boolean = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val available = current.spendJarCoins + current.unassignedCoins
        if (available < cost) return@withContext false

        val returnEstimate = (cost * 1.25f).toInt()
        val spendDeduct = cost.coerceAtMost(current.spendJarCoins)
        val unassignedDeduct = cost - spendDeduct

        val updated = current.copy(
            spendJarCoins = current.spendJarCoins - spendDeduct,
            unassignedCoins = current.unassignedCoins - unassignedDeduct
        )
        db.userDao().insertOrUpdate(updated)

        val plant = GardenPlant(
            name = name,
            icon = icon,
            coinsInvested = cost,
            stage = 0,
            waterCount = 0,
            targetReturnCoins = returnEstimate,
            seasonCondition = "Sunny Growth (+25%)",
            isHarvested = false,
            compoundingCycle = 1,
            treeType = treeType,
            lastWateredDateString = ""
        )
        db.gardenDao().insertPlant(plant)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "INVEST",
                title = "Planted Tree: $name $icon",
                description = "Invested $cost coins into soil to grow compound returns.",
                coinsChanged = -cost,
                jarName = "GARDEN",
                lesson = "Investing means giving your coins a job to produce more coins over time!"
            )
        )
        return@withContext true
    }

    sealed class WaterResult {
        data class Success(val plant: GardenPlant) : WaterResult()
        object AlreadyWateredToday : WaterResult()
        object NotFound : WaterResult()
    }

    suspend fun waterPlant(plantId: Int): WaterResult = withContext(Dispatchers.IO) {
        val plant = db.gardenDao().getPlantById(plantId) ?: return@withContext WaterResult.NotFound
        val today = getTodayDateString()

        // Restrict to once per day!
        if (plant.lastWateredDateString == today) {
            return@withContext WaterResult.AlreadyWateredToday
        }

        val nextWater = plant.waterCount + 1
        val nextStage = (nextWater).coerceAtMost(4)

        val updated = plant.copy(
            waterCount = nextWater,
            stage = nextStage,
            lastWateredDateString = today
        )
        db.gardenDao().updatePlant(updated)
        return@withContext WaterResult.Success(updated)
    }

    suspend fun harvestPlant(plantId: Int): Int = withContext(Dispatchers.IO) {
        val plant = db.gardenDao().getPlantById(plantId) ?: return@withContext 0
        val returnCoins = plant.targetReturnCoins

        db.gardenDao().deletePlant(plant.id)

        val current = getProfileOnce()
        val updated = current.copy(
            unassignedCoins = current.unassignedCoins + returnCoins,
            totalCoinsEarned = current.totalCoinsEarned + (returnCoins - plant.coinsInvested).coerceAtLeast(0)
        )
        val leveled = recalculateLevel(updated)
        db.userDao().insertOrUpdate(leveled)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "INVEST",
                title = "Harvested ${plant.name} $returnCoins Coins! 🍎",
                description = "Invested ${plant.coinsInvested} ➡️ Harvested $returnCoins coins.",
                coinsChanged = returnCoins,
                jarName = "UNASSIGNED",
                lesson = "Patience and care made your investment grow!"
            )
        )
        return@withContext returnCoins
    }

    suspend fun compoundPlant(plantId: Int): GardenPlant? = withContext(Dispatchers.IO) {
        val plant = db.gardenDao().getPlantById(plantId) ?: return@withContext null
        val newInvested = plant.targetReturnCoins
        val newTarget = (newInvested * 1.18f).toInt()
        val nextCycle = plant.compoundingCycle + 1

        val updated = plant.copy(
            coinsInvested = newInvested,
            targetReturnCoins = newTarget,
            stage = 1,
            waterCount = 0,
            compoundingCycle = nextCycle,
            lastWateredDateString = ""
        )
        db.gardenDao().updatePlant(updated)

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "INVEST",
                title = "Compounded ${plant.name} (Year $nextCycle) ✨",
                description = "Reinvested all $newInvested coins! Next cycle target is ~$newTarget coins!",
                coinsChanged = 0,
                jarName = "GARDEN",
                lesson = "Compounding Magic: Earning growth on your previous growth turns small coins into fortunes!"
            )
        )
        return@withContext updated
    }

    // -------------------------------------------------------------
    // LIFE EVENTS & SAFETY JAR
    // -------------------------------------------------------------
    suspend fun triggerLifeEvent(event: LifeEventCard): String = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val impact = event.coinImpact
        val message: String

        if (impact < 0) {
            val cost = -impact
            val safetyCovered = cost.coerceAtMost(current.safetyJarCoins)
            val spendCovered = (cost - safetyCovered).coerceAtMost(current.spendJarCoins)
            val unassignedCovered = cost - safetyCovered - spendCovered

            val updated = current.copy(
                safetyJarCoins = current.safetyJarCoins - safetyCovered,
                spendJarCoins = current.spendJarCoins - spendCovered,
                unassignedCoins = (current.unassignedCoins - unassignedCovered).coerceAtLeast(0)
            )
            db.userDao().insertOrUpdate(updated)

            message = if (safetyCovered >= cost) {
                "Your 🛟 Safety Jar handled the entire cost of $cost coins! You were fully prepared!"
            } else {
                "Your 🛟 Safety Jar covered $safetyCovered coins, and remaining $spendCovered coins came from your Spend Jar."
            }
        } else {
            val updated = current.copy(
                unassignedCoins = current.unassignedCoins + impact,
                totalCoinsEarned = current.totalCoinsEarned + impact
            )
            val leveled = recalculateLevel(updated)
            db.userDao().insertOrUpdate(leveled)
            message = "You received a surprise gift of +$impact coins! Decide where to sort them!"
        }

        db.activityLogDao().insertLog(
            ActivityLog(
                type = "LIFE_EVENT",
                title = event.title,
                description = event.description,
                coinsChanged = impact,
                jarName = if (impact < 0) "SAFETY" else "UNASSIGNED",
                lesson = event.lesson
            )
        )
        return@withContext message
    }

    suspend fun recordMiniGameResult(gameType: String, isCorrect: Boolean) = withContext(Dispatchers.IO) {
        val stat = db.miniGameStatDao().getStat(gameType) ?: MiniGameStat(gameType = gameType)
        val updated = stat.copy(
            correctAnswers = stat.correctAnswers + if (isCorrect) 1 else 0,
            totalPlayed = stat.totalPlayed + 1,
            lastPlayed = System.currentTimeMillis()
        )
        db.miniGameStatDao().insertOrUpdate(updated)
    }

    suspend fun addNewGoal(title: String, icon: String, description: String, targetCost: Int) = withContext(Dispatchers.IO) {
        db.goalDao().insertGoal(
            GoalItem(
                title = title,
                icon = icon,
                description = description,
                targetCost = targetCost,
                currentCoins = 0,
                isCompleted = false
            )
        )
    }

    suspend fun setActiveGoal(goalId: Int) = withContext(Dispatchers.IO) {
        val current = getProfileOnce()
        val updated = current.copy(activeGoalId = goalId)
        db.userDao().insertOrUpdate(updated)
    }

    suspend fun deleteGoal(goalId: Int) = withContext(Dispatchers.IO) {
        db.goalDao().deleteGoal(goalId)
    }

    private fun recalculateLevel(profile: UserProfile): UserProfile {
        val total = profile.totalCoinsEarned
        val (level, title) = when {
            total < 25 -> 1 to "Level 1: My First Coins"
            total < 55 -> 2 to "Level 2: The Three Jars"
            total < 95 -> 3 to "Level 3: Treasure Island & Goals"
            total < 150 -> 4 to "Level 4: Magic Garden Grower"
            total < 230 -> 5 to "Level 5: Little Shopkeeper"
            else -> 6 to "Level 6: Coin Quest Master"
        }
        return profile.copy(level = level, levelTitle = title)
    }
}
