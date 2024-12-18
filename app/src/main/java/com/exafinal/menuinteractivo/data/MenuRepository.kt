package com.exafinal.menuinteractivo.data

import com.exafinal.menuinteractivo.data.database.RestauranteDatabase
import com.exafinal.menuinteractivo.data.model.*

class MenuRepository(private val database: RestauranteDatabase) {

    // Métodos para Platos
    suspend fun obtenerPlatos(): List<Plato> = database.platoDao().obtenerPlatos()

    suspend fun obtenerPlatoPorId(platoId: Int): Plato? =
        database.platoDao().obtenerPlatoPorId(platoId)

    // Métodos para Ingredientes
    suspend fun obtenerIngredientesPorPlato(platoId: Int): List<Ingrediente> =
        database.ingredienteDao().obtenerIngredientesPorPlato(platoId)

    // Métodos para Detalle de Pedido
    suspend fun agregarDetallePedido(detallePedido: DetallePedido) {
        database.detallePedidoDao().insertarDetallePedido(detallePedido)
    }

    suspend fun obtenerDetallesPorPedido(pedidoId: Int): List<DetallePedido> =
        database.detallePedidoDao().obtenerDetallesPorPedido(pedidoId)

    suspend fun eliminarDetallePedido(detallePedido: DetallePedido) {
        database.detallePedidoDao().eliminarDetallePedido(detallePedido)
    }

    // Métodos para Pedido
    suspend fun registrarPedido(pedido: Pedido) {
        database.pedidoDao().insertarPedido(pedido)
    }

    suspend fun obtenerPedidoPorId(pedidoId: Int): Pedido? =
        database.pedidoDao().obtenerPedidoPorId(pedidoId)

    suspend fun insertarPedido(pedido: Pedido) {
        database.pedidoDao().insertarPedido(pedido)
    }

    suspend fun limpiarCarrito() {
        database.detallePedidoDao().eliminarTodosLosDetalles()
    }

    suspend fun obtenerDetallesConNombrePlato(pedidoId: Int): List<DetalleConNombrePlato> {
        return database.detallePedidoDao().obtenerDetallesConNombrePlato(pedidoId)
    }

}
