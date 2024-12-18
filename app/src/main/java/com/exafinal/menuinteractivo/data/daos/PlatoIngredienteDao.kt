package com.exafinal.menuinteractivo.data.daos

import androidx.room.*
import com.exafinal.menuinteractivo.data.model.PlatoIngrediente

@Dao
interface PlatoIngredienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPlatoIngrediente(platoIngrediente: PlatoIngrediente)

    @Query("SELECT * FROM plato_ingredientes WHERE id_plato = :idPlato")
    suspend fun obtenerIngredientesPorPlato(idPlato: Int): List<PlatoIngrediente>

    @Delete
    suspend fun eliminarPlatoIngrediente(platoIngrediente: PlatoIngrediente)

    @Update
    suspend fun actualizarPlatoIngrediente(platoIngrediente: PlatoIngrediente)
}