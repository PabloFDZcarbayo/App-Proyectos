package com.utad.aplicacion_proyectos.Data.Database.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.utad.aplicacion_proyectos.Data.Database.Entities.Language_Entitie

@Dao
interface LanguageDao {
    @Query("SELECT * FROM languages")
    suspend fun getAllLanguages(): MutableList<Language_Entitie>

    @Query ("SELECT * FROM languages WHERE name = :name")
    suspend fun getLanguageByName(name: String): MutableList<Language_Entitie>

    @Insert
    suspend fun insertLanguage(language: Language_Entitie)

    @Delete
    suspend fun deleteLanguage(language: Language_Entitie)

}