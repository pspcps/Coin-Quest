package com.example.data.local

import androidx.room.*
import com.example.data.model.SkillProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillProjectDao {

    @Query("SELECT * FROM skill_projects ORDER BY id ASC")
    fun getAllProjects(): Flow<List<SkillProjectEntity>>

    @Query("SELECT * FROM skill_projects WHERE isDismissed = 0 ORDER BY id ASC")
    fun getActiveProjects(): Flow<List<SkillProjectEntity>>

    @Query("SELECT * FROM skill_projects WHERE isCompleted = 1 ORDER BY completedAt DESC")
    fun getCompletedPortfolio(): Flow<List<SkillProjectEntity>>

    @Query("SELECT * FROM skill_projects WHERE skillId = :skillId LIMIT 1")
    suspend fun getProjectBySkillId(skillId: String): SkillProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: SkillProjectEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<SkillProjectEntity>)

    @Update
    suspend fun updateProject(project: SkillProjectEntity)

    @Query("UPDATE skill_projects SET isDismissed = 1 WHERE skillId = :skillId")
    suspend fun dismissSkill(skillId: String)
}
