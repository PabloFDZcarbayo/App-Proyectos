package com.utad.aplicacion_proyectos.Data.Database

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.File

class ProjectsApplication :Application() {


    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
       /*
        // Eliminar la base de datos si existe, usar para resetear app
        val dbFile = File(applicationContext.getDatabasePath("projects_database").absolutePath)
        if (dbFile.exists()) {
            dbFile.delete()
        }
        */


        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "projects_database"
        )

            .build()



    }
}