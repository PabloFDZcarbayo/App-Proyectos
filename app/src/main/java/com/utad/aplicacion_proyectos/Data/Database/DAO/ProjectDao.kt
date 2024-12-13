package com.utad.aplicacion_proyectos.Data.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.utad.aplicacion_proyectos.Data.Database.Entities.Project_Entitie
import com.utad.aplicacion_proyectos.Data.Model.Project

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    suspend fun getAllProjects(): MutableList<Project_Entitie>

    @Delete
    suspend fun deleteProject(project: Project_Entitie)

    @Insert
    suspend fun insertProject(project: Project_Entitie)

    @Update
    suspend fun updateProject(project: Project_Entitie)

}