package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MiniGameStat
import kotlinx.coroutines.flow.Flow

@Dao
interface MiniGameStatDao {
    @Query("SELECT * FROM mini_game_stats")
    fun getAllStats(): Flow<List<MiniGameStat>>

    @Query("SELECT * FROM mini_game_stats WHERE gameType = :gameType LIMIT 1")
    suspend fun getStat(gameType: String): MiniGameStat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stat: MiniGameStat)
}
