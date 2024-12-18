package com.exafinal.menuinteractivo.data.daos

import androidx.room.*
import com.exafinal.menuinteractivo.data.model.DetalleConNombrePlato
import com.exafinal.menuinteractivo.data.model.DetallePedido

@Dao
interface DetallePedidoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarDetallePedido(detallePedido: DetallePedido)

    @Query("SELECT * FROM detalle_pedido WHERE id_pedido = :idPedido")
    suspend fun obtenerDetallesPorPedido(idPedido: Int): List<DetallePedido>

    @Delete
    suspend fun eliminarDetallePedido(detallePedido: DetallePedido)

    @Update
    suspend fun actualizarDetallePedido(detallePedido: DetallePedido)

    @Query("DELETE FROM detalle_pedido")
    suspend fun eliminarTodosLosDetalles()

    @Query("""
    SELECT dp.id_detalle, dp.id_pedido, dp.id_plato, dp.cantidad, dp.subtotal, dp.notas, p.nombre_plato 
    FROM detalle_pedido dp
    INNER JOIN platos p ON dp.id_plato = p.id_plato
    WHERE dp.id_pedido = :pedidoId
""")
    suspend fun obtenerDetallesConNombrePlato(pedidoId: Int): List<DetalleConNombrePlato>


}


