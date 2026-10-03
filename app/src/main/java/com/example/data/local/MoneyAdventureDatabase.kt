package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserProfile::class,
        GoalItem::class,
        GardenPlant::class,
        ActivityLog::class,
        MiniGameStat::class,
        HabitItem::class,
        DailyHabitLog::class,
        CustomToyItem::class,
        SkillProjectEntity::class,
        QuizDailyHistory::class
    ],
    version = 5,
    exportSchema = false
)
abstract class MoneyAdventureDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun goalDao(): GoalDao
    abstract fun gardenDao(): GardenDao
    abstract fun activityLogDao(): ActivityLogDao
    abstract fun miniGameStatDao(): MiniGameStatDao
    abstract fun habitDao(): HabitDao
    abstract fun customToyDao(): CustomToyDao
    abstract fun skillProjectDao(): SkillProjectDao
    abstract fun quizDailyHistoryDao(): QuizDailyHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: MoneyAdventureDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope = CoroutineScope(Dispatchers.IO)): MoneyAdventureDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyAdventureDatabase::class.java,
                    "money_adventure_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateInitialData(database)
                    }
                }
            }
        }

        suspend fun populateInitialData(db: MoneyAdventureDatabase) {
            val userDao = db.userDao()
            val goalDao = db.goalDao()
            val gardenDao = db.gardenDao()
            val logDao = db.activityLogDao()
            val habitDao = db.habitDao()
            val skillDao = db.skillProjectDao()

            if (userDao.getAllProfilesOnce().isEmpty()) {
                userDao.insertOrUpdate(
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
                userDao.insertOrUpdate(
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

                // Initial habits
                habitDao.insertHabits(SampleGameData.initialHabits)

                // Initial age-wise skills
                SampleGameData.ageWiseSkillsCatalog.forEach { skill ->
                    skillDao.insertProject(
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

                // Initial sample goals
                goalDao.insertGoals(
                    listOf(
                        GoalItem(
                            id = 1,
                            title = "Big Toy Castle",
                            icon = "🏰",
                            description = "A grand castle with towers and flags for our village toys!",
                            targetCost = 30,
                            currentCoins = 10,
                            isCompleted = false
                        ),
                        GoalItem(
                            id = 2,
                            title = "Super Bicycle",
                            icon = "🚲",
                            description = "A shiny red bike for riding through the sunny village park!",
                            targetCost = 50,
                            currentCoins = 0,
                            isCompleted = false
                        ),
                        GoalItem(
                            id = 3,
                            title = "Puppy Care Adventure",
                            icon = "🐶",
                            description = "Adopt and care for a cuddly village pet friend!",
                            targetCost = 25,
                            currentCoins = 0,
                            isCompleted = false
                        ),
                        GoalItem(
                            id = 4,
                            title = "Magic Art Studio",
                            icon = "🎨",
                            description = "Glitter pens, colorful paints, and canvas for giant masterworks!",
                            targetCost = 40,
                            currentCoins = 0,
                            isCompleted = false
                        )
                    )
                )

                // Initial garden plant
                gardenDao.insertPlant(
                    GardenPlant(
                        id = 1,
                        name = "Sweet Apple Orchard",
                        icon = "🍎",
                        coinsInvested = 10,
                        stage = 1, // Sprout
                        waterCount = 1,
                        targetReturnCoins = 14,
                        seasonCondition = "Sunny Growth (+25%)",
                        isHarvested = false,
                        compoundingCycle = 1,
                        treeType = "Apple Orchard (+25% Growth)"
                    )
                )

                // Initial logs
                logDao.insertLog(
                    ActivityLog(
                        type = "EARN",
                        title = "Completed Habit: Brush Teeth 🪥",
                        description = "Brushed teeth clean in the morning!",
                        coinsChanged = 1,
                        jarName = "UNASSIGNED",
                        lesson = "Creating Value / Positive Habit Discipline"
                    )
                )
                logDao.insertLog(
                    ActivityLog(
                        type = "SAVE",
                        title = "Saved in Piggy Jar",
                        description = "Put coins away for the Castle dream!",
                        coinsChanged = 5,
                        jarName = "SAVE",
                        lesson = "Delayed Gratification"
                    )
                )
            }
        }
    }
}
