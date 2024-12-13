package com.utad.aplicacion_proyectos.Data.Database.Entities

import androidx.room.Entity

@Entity(tableName = "languages", primaryKeys = ["name"])
data class Language_Entitie(
    var name: String,

) {
}