package com.exafinal.menuinteractivo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class Pedido(
    @PrimaryKey(autoGenerate = true) val id_pedido: Int = 0,
    val id_mesa: Int,
    val fecha_pedido: String, // Puede ser una cadena o convertirlo a Date más adelante
    val total_pedido: Double,
    val estado: String
)