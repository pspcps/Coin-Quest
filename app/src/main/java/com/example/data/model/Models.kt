package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    val name: String = "Little Hero",
    val avatar: String = "🦊",
    val dateOfBirth: String = "2018-06-15",
    val childAge: Int = 8,
    val unassignedCoins: Int = 10,
    val spendJarCoins: Int = 4,
    val saveJarCoins: Int = 6,
    val giveJarCoins: Int = 2,
    val safetyJarCoins: Int = 4,
    val bankCoins: Int = 5,
    val totalCoinsEarned: Int = 31,
    val level: Int = 1,
    val levelTitle: String = "My First Coins",
    val activeGoalId: Int? = 1,
    val bankLastInterestTime: Long = System.currentTimeMillis(),
    val streakDays: Int = 1,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val parentDeductionPolicy: String = "NONE", // NONE, DEDUCT
    val smartChoiceXp: Int = 20,
    val smartChoiceBadge: String = "Smart Spender 🥉",
    val sipAmount: Int = 2,
    val sipFrequency: String = "MONTHLY", // DAILY, MONTHLY, OFF
    val isSipActive: Boolean = true,
    val lastSipDateString: String = "",
    val lastMonthlyGrowthCalculated: Long = System.currentTimeMillis(),
    val quizDailyGoal: Int = 10,
    val quizRewardCoins: Int = 5,
    val isCurrentActive: Boolean = true
)

@Entity(tableName = "goals")
data class GoalItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val icon: String,
    val description: String,
    val targetCost: Int,
    val currentCoins: Int = 0,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)

@Entity(tableName = "habits")
data class HabitItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val icon: String,
    val rewardCoins: Int,
    val category: String = "Responsibility", // School, Morning, Evening, Health, Responsibility, Family, Demerit
    val frequency: String = "DAILY", // DAILY, WEEKDAYS, WEEKENDS, CUSTOM
    val confirmationMethod: String = "PARENT_CONFIRMS", // INSTANT, PARENT_CONFIRMS
    val isFamilyTeamwork: Boolean = false,
    val isNegative: Boolean = false,
    val deductionCoins: Int = 0,
    val isEnabled: Boolean = true,
    val note: String = ""
)

@Entity(tableName = "daily_habit_logs")
data class DailyHabitLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val habitId: Int,
    val dateString: String,
    val title: String,
    val icon: String,
    val rewardCoins: Int,
    val category: String,
    val isNegative: Boolean = false,
    val status: String = "PENDING_APPROVAL", // PENDING_APPROVAL, APPROVED, MISSED, RECOVERED, PENALTY
    val completedAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null
)

@Entity(tableName = "garden_plants")
data class GardenPlant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String,
    val coinsInvested: Int,
    val stage: Int = 0, // 0 = Seed, 1 = Sprout, 2 = Bush, 3 = Tree, 4 = Harvest Ready
    val plantedTime: Long = System.currentTimeMillis(),
    val waterCount: Int = 0,
    val targetReturnCoins: Int = 15,
    val seasonCondition: String = "Sunny Growth (+25%)",
    val isHarvested: Boolean = false,
    val compoundingCycle: Int = 1,
    val treeType: String = "Apple Orchard",
    val lastWateredDateString: String = ""
)

@Entity(tableName = "custom_toys")
data class CustomToyItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val icon: String,
    val price: Int,
    val category: String = "Toys",
    val description: String,
    val isCustomParentItem: Boolean = true,
    val isPurchased: Boolean = false,
    val smartAlternativeTitle: String? = null,
    val smartAlternativePrice: Int? = null,
    val smartXpReward: Int = 10
)

@Entity(tableName = "skill_projects")
data class SkillProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val skillId: String,
    val title: String,
    val icon: String,
    val skillName: String = "",
    val minAge: Int = 6,
    val maxAge: Int = 14,
    val craftCost: Int = 2,
    val sellPrice: Int = 6,
    val description: String,
    val category: String = "Crafts",
    val isDismissed: Boolean = false,
    val isCompleted: Boolean = false,
    val completedTimes: Int = 0,
    val completedAt: Long? = null,
    val photoProofBadge: String? = null,
    val proofPhotoUri: String? = null,
    val userNotes: String? = null
)

@Entity(tableName = "quiz_daily_history")
data class QuizDailyHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateString: String,
    val score: Int,
    val totalQuestions: Int = 10,
    val coinsEarned: Int = 5,
    val category: String = "Mixed",
    val completedAt: Long = System.currentTimeMillis(),
    val childAge: Int = 8
)

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String, // EARN, SPEND, SAVE, GIVE, SAFETY, INVEST, BANK, LIFE_EVENT, SKILL, SMART_CHOICE, RECOVERY, DEMERIT, QUIZ, SIP, GAME
    val title: String,
    val description: String,
    val coinsChanged: Int,
    val jarName: String,
    val lesson: String
)

@Entity(tableName = "mini_game_stats")
data class MiniGameStat(
    @PrimaryKey val gameType: String,
    val correctAnswers: Int = 0,
    val totalPlayed: Int = 0,
    val lastPlayed: Long = System.currentTimeMillis()
)

// Static UI & Gameplay Models
data class ChoreItem(
    val id: String,
    val title: String,
    val icon: String,
    val rewardCoins: Int,
    val isFamilyResponsibility: Boolean = false,
    val note: String,
    val category: String = "Home"
)

data class ShopProduct(
    val id: String,
    val title: String,
    val icon: String,
    val price: Int,
    val category: String,
    val description: String,
    val isLongTermWish: Boolean = false,
    val isCustomParentItem: Boolean = false,
    val smartAlternativeTitle: String? = null,
    val smartAlternativePrice: Int? = null,
    val smartXpReward: Int = 10
)

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val category: String, // Math, Financial Smarts, Logic & Puzzles, Brain Riddles, Science
    val minAge: Int,
    val maxAge: Int,
    val explanation: String,
    val funFact: String = ""
) {
    val questionText: String get() = question
    val correctOptionIndex: Int get() = correctAnswerIndex
}

data class RecoveryQuest(
    val id: String,
    val title: String,
    val icon: String,
    val rewardCoins: Int,
    val description: String,
    val category: String
)

data class GardenTreeOption(
    val name: String,
    val icon: String,
    val cost: Int,
    val treeType: String
)

data class NeedsVsWantsCard(
    val id: String,
    val title: String,
    val icon: String,
    val isNeed: Boolean,
    val explanation: String,
    val category: String
)

data class LifeEventCard(
    val id: String,
    val title: String,
    val icon: String,
    val description: String,
    val coinImpact: Int,
    val isEmergency: Boolean,
    val lesson: String
)

data class SkillBusinessItem(
    val id: String,
    val title: String,
    val icon: String,
    val skillName: String,
    val craftCost: Int,
    val sellPrice: Int,
    val description: String,
    val minAge: Int = 5,
    val maxAge: Int = 14,
    val category: String = "Art & Business"
)

data class GivingCause(
    val name: String,
    val icon: String,
    val description: String
)
