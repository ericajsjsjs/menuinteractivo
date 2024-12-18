package com.exafinal.menuinteractivo.data.daos

import androidx.room.*
import com.exafinal.menuinteractivo.data.model.Mesa

@Dao
interface MesaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMesa(mesa: Mesa)

    @Query("SELECT * FROM mesas")
    suspend fun obtenerMesas(): List<Mesa>

    @Delete
    suspend fun eliminarMesa(mesa: Mesa)

    @Update
    suspend fun actualizarMesa(mesa: Mesa)
}