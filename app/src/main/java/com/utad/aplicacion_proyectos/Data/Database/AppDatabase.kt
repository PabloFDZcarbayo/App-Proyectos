package com.utad.aplicacion_proyectos.Data.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.utad.aplicacion_proyectos.Data.Database.DAO.LanguageDao
import com.utad.aplicacion_proyectos.Data.Database.DAO.ProjectDao
import com.utad.aplicacion_proyectos.Data.Database.Entities.Language_Entitie
import com.utad.aplicacion_proyectos.Data.Database.Entities.Project_Entitie


@Database (entities = [Language_Entitie::class,Project_Entitie::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
    abstract fun projectDao(): ProjectDao

}