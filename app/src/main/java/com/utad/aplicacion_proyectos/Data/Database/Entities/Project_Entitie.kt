package com.utad.aplicacion_proyectos.Data.Database.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "projects")
data class Project_Entitie(
    @PrimaryKey (autoGenerate = true) var id: Int = 0,
    var name: String,
    var description: String,
    var date: String,
    var time:String,
    var language: String,
    var priority: String,
    var details: String
) {
}
