package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile ORDER BY isCurrentActive DESC, id ASC")
    fun getAllProfiles(): Flow<List<UserProfile>>

    @Query("SELECT * FROM user_profile ORDER BY isCurrentActive DESC, id ASC")
    suspend fun getAllProfilesOnce(): List<UserProfile>

    @Query("SELECT * FROM user_profile WHERE isCurrentActive = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE isCurrentActive = 1 LIMIT 1")
    suspend fun getUserProfileOnce(): UserProfile?

    @Query("SELECT * FROM user_profile WHERE id = :id LIMIT 1")
    suspend fun getUserProfileById(id: Int): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfile): Long

    @Update
    suspend fun update(profile: UserProfile)

    @Query("UPDATE user_profile SET isCurrentActive = 0")
    suspend fun clearActiveFlag()

    @Query("UPDATE user_profile SET isCurrentActive = 1 WHERE id = :id")
    suspend fun setActiveProfile(id: Int)

    @Query("DELETE FROM user_profile WHERE id = :id")
    suspend fun deleteProfile(id: Int)
}
