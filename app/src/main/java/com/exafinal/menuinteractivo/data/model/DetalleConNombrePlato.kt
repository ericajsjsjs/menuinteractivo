package com.exafinal.menuinteractivo.data.model

data class DetalleConNombrePlato(
    val id_detalle: Int,
    val id_pedido: Int,
    val id_plato: Int,
    val cantidad: Int,
    val subtotal: Double,
    val nombre_plato: String,
    val notas: String? // Campo opcional para personalizaciones
) {

}
