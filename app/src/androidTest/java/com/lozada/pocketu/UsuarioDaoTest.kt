package com.lozada.pocketu

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.data.Usuario
import com.lozada.pocketu.data.UsuarioDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class UsuarioDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var usuarioDao: UsuarioDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()

        usuarioDao = db.usuarioDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun validacionCompletaLogin_credencialesCorrectasEIncorrectas() = runBlocking {
        // ARRANGE: Registramos un usuario en la base de datos temporal
        val usuario = Usuario(
            id = 2,
            nombre = "Erick Lozada",
            correo = "erick@pocketu.com",
            contrasena = "claveSegura123"
        )
        usuarioDao.registrarUsuario(usuario)

        // ACT: Simulamos intentos de inicio de sesión
        val loginCorrecto = usuarioDao.iniciarSesion("erick@pocketu.com", "claveSegura123")
        val loginPassIncorrecta = usuarioDao.iniciarSesion("erick@pocketu.com", "12345")
        val loginCorreoIncorrecto = usuarioDao.iniciarSesion("falso@pocketu.com", "claveSegura123")

        // ASSERT: Verificamos los diferentes resultados
        assertNotNull("Con credenciales correctas, debe retornar el usuario", loginCorrecto)
        assertEquals("El nombre del usuario logueado debe coincidir", "Erick Lozada", loginCorrecto?.nombre)

        assertNull("Con contraseña incorrecta, debe denegar el acceso (null)", loginPassIncorrecta)
        assertNull("Con correo incorrecto, debe denegar el acceso (null)", loginCorreoIncorrecto)
    }
}