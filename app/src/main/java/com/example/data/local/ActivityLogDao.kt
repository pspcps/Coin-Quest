package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ActivityLog
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAllLogs(): Flow<List<ActivityLog>>

    @Query("SELECT * FROM activity_logs WHERE type = :type ORDER BY timestamp DESC")
    fun getLogsByType(type: String): Flow<List<ActivityLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLog): Long

    @Query("DELETE FROM activity_logs")
    suspend fun clearLogs()
}
