package com.exafinal.menuinteractivo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mesas")
data class Mesa(
    @PrimaryKey(autoGenerate = true) val id_mesa: Int = 0,
    val nombre_mesa: String
)