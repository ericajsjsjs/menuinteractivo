package com.exafinal.menuinteractivo.data.daos

import androidx.room.*
import com.exafinal.menuinteractivo.data.model.Ingrediente

@Dao
interface IngredienteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarIngrediente(ingrediente: Ingrediente)

    @Query("SELECT * FROM ingredientes")
    suspend fun obtenerIngredientes(): List<Ingrediente>

    @Delete
    suspend fun eliminarIngrediente(ingrediente: Ingrediente)

    @Update
    suspend fun actualizarIngrediente(ingrediente: Ingrediente)

    @Query("""
        SELECT ingredientes.* FROM ingredientes
        INNER JOIN plato_ingredientes ON ingredientes.id_ingrediente = plato_ingredientes.id_ingrediente
        WHERE plato_ingredientes.id_plato = :platoId
    """)
    suspend fun obtenerIngredientesPorPlato(platoId: Int): List<Ingrediente>

}

