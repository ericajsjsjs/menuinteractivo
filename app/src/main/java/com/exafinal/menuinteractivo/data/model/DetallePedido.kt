package com.exafinal.menuinteractivo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detalle_pedido")
data class DetallePedido(
    @PrimaryKey(autoGenerate = true) val id_detalle: Int = 0,
    val id_pedido: Int,
    val id_plato: Int,
    val cantidad: Int,
    val subtotal: Double,
    val notas: String? // Personalizaciones como ingredientes seleccionados
)
