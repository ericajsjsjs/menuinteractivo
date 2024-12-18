package com.exafinal.menuinteractivo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "platos")
data class Plato(
    @PrimaryKey(autoGenerate = true) val id_plato: Int = 0,
    val nombre_plato: String,
    val descripcion: String,
    val precio_base: Double,
    val imagen: String
)