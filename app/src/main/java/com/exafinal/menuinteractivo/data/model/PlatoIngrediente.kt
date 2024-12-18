package com.exafinal.menuinteractivo.data.model

import androidx.room.Entity

@Entity(
    tableName = "plato_ingredientes",
    primaryKeys = ["id_plato", "id_ingrediente"]
)
data class PlatoIngrediente(
    val id_plato: Int,
    val id_ingrediente: Int
)