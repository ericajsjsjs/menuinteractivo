package com.exafinal.menuinteractivo.data.daos

import androidx.room.*
import com.exafinal.menuinteractivo.data.model.Plato

@Dao
interface PlatoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPlato(plato: Plato)

    @Query("SELECT * FROM platos")
    suspend fun obtenerPlatos(): List<Plato>

    @Query("SELECT * FROM platos WHERE id_plato = :platoId")
    suspend fun obtenerPlatoPorId(platoId: Int): Plato?

    @Delete
    suspend fun eliminarPlato(plato: Plato)

    @Update
    suspend fun actualizarPlato(plato: Plato)
}
