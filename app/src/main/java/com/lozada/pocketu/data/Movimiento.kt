package com.lozada.pocketu.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class Movimiento(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val descripcion: String,

    val monto: Double,

    val tipo: String, // Ingreso o Gasto

    val categoria: String,

    val fecha: String
)