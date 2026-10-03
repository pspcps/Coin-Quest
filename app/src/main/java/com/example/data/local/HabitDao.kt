package com.example.data.local

import androidx.room.*
import com.example.data.model.DailyHabitLog
import com.example.data.model.HabitItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAllHabits(): Flow<List<HabitItem>>

    @Query("SELECT * FROM habits ORDER BY id ASC")
    suspend fun getAllHabitsOnce(): List<HabitItem>

    @Query("SELECT * FROM habits WHERE isEnabled = 1 ORDER BY id ASC")
    fun getEnabledHabits(): Flow<List<HabitItem>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Int): HabitItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitItem>)

    @Update
    suspend fun updateHabit(habit: HabitItem)

    @Delete
    suspend fun deleteHabit(habit: HabitItem)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Int)

    // Daily Habit Logs
    @Query("SELECT * FROM daily_habit_logs ORDER BY completedAt DESC")
    fun getAllDailyLogs(): Flow<List<DailyHabitLog>>

    @Query("SELECT * FROM daily_habit_logs WHERE dateString = :dateString")
    fun getLogsForDate(dateString: String): Flow<List<DailyHabitLog>>

    @Query("SELECT * FROM daily_habit_logs WHERE dateString = :dateString AND habitId = :habitId LIMIT 1")
    suspend fun getLogForDateAndHabit(dateString: String, habitId: Int): DailyHabitLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyLog(log: DailyHabitLog): Long

    @Update
    suspend fun updateDailyLog(log: DailyHabitLog)

    @Query("SELECT * FROM daily_habit_logs WHERE status = 'PENDING_APPROVAL'")
    fun getPendingApprovalLogs(): Flow<List<DailyHabitLog>>
}
