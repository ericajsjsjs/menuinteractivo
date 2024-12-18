package com.exafinal.menuinteractivo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.exafinal.menuinteractivo.data.daos.* // Importa tus DAOs
import com.exafinal.menuinteractivo.data.model.* // Importa tus entidades
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Mesa::class, Plato::class, Ingrediente::class, PlatoIngrediente::class, Pedido::class, DetallePedido::class],
    version = 1,
    exportSchema = false
)
abstract class RestauranteDatabase : RoomDatabase() {

    abstract fun mesaDao(): MesaDao
    abstract fun platoDao(): PlatoDao
    abstract fun ingredienteDao(): IngredienteDao
    abstract fun platoIngredienteDao(): PlatoIngredienteDao
    abstract fun pedidoDao(): PedidoDao
    abstract fun detallePedidoDao(): DetallePedidoDao

    companion object {
        @Volatile
        private var INSTANCE: RestauranteDatabase? = null

        fun getDatabase(context: Context): RestauranteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RestauranteDatabase::class.java,
                    "restaurante_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Poblamos las tablas iniciales usando corrutinas
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        val mesaDao = database.mesaDao()
                        val platoDao = database.platoDao()
                        val ingredienteDao = database.ingredienteDao()
                        val platoIngredienteDao = database.platoIngredienteDao()

                        // Poblar datos en la tabla "Mesas"
                        mesaDao.insertarMesa(Mesa(1, "Mesa 1"))
                        mesaDao.insertarMesa(Mesa(2, "Mesa 2"))
                        mesaDao.insertarMesa(Mesa(3, "Mesa 3"))

                        // Poblar datos en la tabla "Platos"
                        platoDao.insertarPlato(
                            Plato(
                                1,
                                "Pizza Margarita",
                                "Pizza clásica con tomate y mozzarella",
                                12.5,
                                "pizza_margarita"
                            )
                        )
                        platoDao.insertarPlato(
                            Plato(
                                2,
                                "Hamburguesa Clásica",
                                "Carne de res con lechuga y tomate",
                                8.99,
                                "hamburguesa_clasica"
                            )
                        )
                        platoDao.insertarPlato(
                            Plato(
                                3,
                                "Ensalada César",
                                "Ensalada con pollo, lechuga y aderezo",
                                6.5,
                                "ensalada_cesar"
                            )
                        )

                        // Poblar datos en la tabla "Ingredientes"
                        ingredienteDao.insertarIngrediente(Ingrediente(1, "Queso Extra", 1.5))
                        ingredienteDao.insertarIngrediente(Ingrediente(2, "Pepinillos", 0.5))
                        ingredienteDao.insertarIngrediente(Ingrediente(3, "Pollo", 2.0))
                        ingredienteDao.insertarIngrediente(Ingrediente(4, "Tomate", 0.3))
                        ingredienteDao.insertarIngrediente(Ingrediente(5, "Cebolla", 0.4))
                        ingredienteDao.insertarIngrediente(Ingrediente(6, "Champiñones", 1.0))
                        ingredienteDao.insertarIngrediente(Ingrediente(7, "Jamón", 1.2))

                        // Poblar datos en la tabla "PlatoIngredientes" (relación muchos a muchos)
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                1,
                                1
                            )
                        ) // Pizza Margarita -> Queso Extra
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                1,
                                4
                            )
                        ) // Pizza Margarita -> Tomate
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                1,
                                6
                            )
                        ) // Pizza Margarita -> Champiñones
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                2,
                                2
                            )
                        ) // Hamburguesa -> Pepinillos
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                2,
                                5
                            )
                        ) // Hamburguesa -> Cebolla
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                3,
                                3
                            )
                        ) // Ensalada César -> Pollo
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                3,
                                4
                            )
                        ) // Ensalada César -> Tomate
                        platoIngredienteDao.insertarPlatoIngrediente(
                            PlatoIngrediente(
                                3,
                                5
                            )
                        ) // Ensalada César -> Cebolla
                    }
                }
            }
        }
    }
}
