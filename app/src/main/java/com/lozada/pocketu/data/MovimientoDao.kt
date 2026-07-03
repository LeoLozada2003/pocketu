package com.lozada.pocketu.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertarMovimiento(movimiento: Movimiento)

    @Update
    suspend fun actualizarMovimiento(movimiento: Movimiento)

    @Delete
    suspend fun eliminarMovimiento(movimiento: Movimiento)

    @Query("SELECT * FROM movimientos ORDER BY id DESC")
    fun obtenerMovimientos(): LiveData<List<Movimiento>>

    // READ - Obtener solo el último movimiento registrado
    @Query("SELECT * FROM movimientos ORDER BY id DESC LIMIT 1")
    suspend fun obtenerUltimoMovimiento(): Movimiento?
}