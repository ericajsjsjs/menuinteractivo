package com.exafinal.menuinteractivo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredientes")
data class Ingrediente(
    @PrimaryKey(autoGenerate = true) val id_ingrediente: Int = 0,
    val nombre_ingrediente: String,
    val precio_adicional: Double
)
