package com.lozada.pocketu

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.data.Movimiento
import com.lozada.pocketu.data.MovimientoDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MovimientoDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var movimientoDao: MovimientoDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // Creamos la base de datos temporal en memoria
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()

        movimientoDao = db.movimientoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertarYLeerMovimiento_datosSonCorrectos() = runBlocking {
        // ARRANGE: Preparamos el movimiento de prueba
        val nuevoMovimiento = Movimiento(
            id = 1,
            usuarioId = 1, // Pertenece al usuario con ID 1
            descripcion = "Almuerzo en la universidad",
            monto = 3.50,
            tipo = "Gasto",
            categoria = "Comida",
            fecha = "08/07/2026"
        )

        // ACT: Insertamos el movimiento en la base de datos temporal
        movimientoDao.insertarMovimiento(nuevoMovimiento)

        // Usamos la función de tu DAO que lee el último movimiento ingresado
        val movimientoRecuperado = movimientoDao.obtenerUltimoMovimiento()

        // ASSERT: Verificamos que se haya guardado y los datos coincidan
        assertNotNull("El movimiento recuperado no debe ser nulo", movimientoRecuperado)
        assertEquals("La descripción debe coincidir", "Almuerzo en la universidad", movimientoRecuperado?.descripcion)
        assertEquals("El monto debe ser exactamente el mismo", 3.50, movimientoRecuperado?.monto)
        assertEquals("El tipo de movimiento debe ser Gasto", "Gasto", movimientoRecuperado?.tipo)
        assertEquals("El usuarioId debe ser el correcto", 1, movimientoRecuperado?.usuarioId)
    }
}