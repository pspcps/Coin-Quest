package com.example.data.local

import androidx.room.*
import com.example.data.model.CustomToyItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomToyDao {

    @Query("SELECT * FROM custom_toys ORDER BY id DESC")
    fun getAllToys(): Flow<List<CustomToyItem>>

    @Query("SELECT * FROM custom_toys WHERE id = :id LIMIT 1")
    suspend fun getToyById(id: Int): CustomToyItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToy(toy: CustomToyItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToys(toys: List<CustomToyItem>)

    @Update
    suspend fun updateToy(toy: CustomToyItem)

    @Delete
    suspend fun deleteToy(toy: CustomToyItem)

    @Query("DELETE FROM custom_toys WHERE id = :id")
    suspend fun deleteToyById(id: Int)
}
