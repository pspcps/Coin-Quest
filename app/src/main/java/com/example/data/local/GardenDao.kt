package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.GardenPlant
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenDao {
    @Query("SELECT * FROM garden_plants WHERE isHarvested = 0 ORDER BY plantedTime DESC")
    fun getActivePlants(): Flow<List<GardenPlant>>

    @Query("SELECT * FROM garden_plants ORDER BY plantedTime DESC")
    fun getAllPlants(): Flow<List<GardenPlant>>

    @Query("SELECT * FROM garden_plants WHERE id = :id LIMIT 1")
    suspend fun getPlantById(id: Int): GardenPlant?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: GardenPlant): Long

    @Update
    suspend fun updatePlant(plant: GardenPlant)

    @Query("DELETE FROM garden_plants WHERE id = :id")
    suspend fun deletePlant(id: Int)
}
