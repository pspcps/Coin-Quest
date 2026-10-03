package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.GoalItem
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Query("SELECT * FROM goals ORDER BY isCompleted ASC, targetCost ASC")
    fun getAllGoals(): Flow<List<GoalItem>>

    @Query("SELECT * FROM goals WHERE id = :id LIMIT 1")
    suspend fun getGoalById(id: Int): GoalItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoals(goals: List<GoalItem>)

    @Update
    suspend fun updateGoal(goal: GoalItem)

    @Query("DELETE FROM goals WHERE id = :id")
    suspend fun deleteGoal(id: Int)
}
