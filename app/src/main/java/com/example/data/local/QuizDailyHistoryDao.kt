package com.example.data.local

import androidx.room.*
import com.example.data.model.QuizDailyHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDailyHistoryDao {

    @Query("SELECT * FROM quiz_daily_history ORDER BY completedAt DESC")
    fun getAllHistory(): Flow<List<QuizDailyHistory>>

    @Query("SELECT * FROM quiz_daily_history WHERE dateString = :dateString LIMIT 1")
    suspend fun getHistoryForDate(dateString: String): QuizDailyHistory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: QuizDailyHistory): Long
}
