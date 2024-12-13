package com.utad.aplicacion_proyectos.Provider

import android.util.Log
import com.utad.aplicacion_proyectos.Data.Database.Entities.Language_Entitie
import com.utad.aplicacion_proyectos.Data.Database.ProjectsApplication
import com.utad.aplicacion_proyectos.Data.Model.Language

class Language_provider {

    companion object {
        suspend fun loadLanguages(): MutableList<Language> {
            val languages_entities = ProjectsApplication.database.languageDao().getAllLanguages()
            val languages = mutableListOf<Language>()
            for (language in languages_entities) {
                languages.add(Language(language.name))
            }
            return languages

        }

        suspend fun deleteLanguage(language: Language) {
            val language_entity = Language_Entitie(language.name)
            ProjectsApplication.database.languageDao().deleteLanguage(language_entity)
        }


    }
}
