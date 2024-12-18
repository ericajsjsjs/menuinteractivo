package com.exafinal.menuinteractivo.data.daos

import androidx.room.*
import com.exafinal.menuinteractivo.data.model.Pedido

@Dao
interface PedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPedido(pedido: Pedido)

    @Query("SELECT * FROM pedidos")
    suspend fun obtenerPedidos(): List<Pedido>

    @Delete
    suspend fun eliminarPedido(pedido: Pedido)

    @Update
    suspend fun actualizarPedido(pedido: Pedido)

    @Query("SELECT * FROM pedidos WHERE id_mesa = :idMesa")
    suspend fun obtenerPedidosPorMesa(idMesa: Int): List<Pedido>

    @Query("SELECT * FROM pedidos WHERE id_pedido = :pedidoId")
    suspend fun obtenerPedidoPorId(pedidoId: Int): Pedido?

}