package com.lozada.pocketu


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(applicationContext)

        configurarEventos()
    }

    private fun configurarEventos() {

        binding.btnIniciarSesion.setOnClickListener {
            validarInicioSesion()
        }

        binding.tvRegistrate.setOnClickListener {
            val intent = Intent(
                this,
                RegistroActivity::class.java
            )

            startActivity(intent)
        }
    }

    private fun validarInicioSesion() {

        limpiarErrores()

        val correo = binding.etCorreo.text
            ?.toString()
            ?.trim()
            ?.lowercase()
            .orEmpty()

        val contrasena = binding.etContrasena.text
            ?.toString()
            ?.trim()
            .orEmpty()

        if (correo.isEmpty()) {
            binding.tilCorreo.error =
                "Ingresa tu correo electrónico"

            binding.etCorreo.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.tilCorreo.error =
                "Ingresa un correo válido"

            binding.etCorreo.requestFocus()
            return
        }

        if (contrasena.isEmpty()) {
            binding.tilContrasena.error =
                "Ingresa tu contraseña"

            binding.etContrasena.requestFocus()
            return
        }

        if (contrasena.length < 6) {
            binding.tilContrasena.error =
                "La contraseña debe tener al menos 6 caracteres"

            binding.etContrasena.requestFocus()
            return
        }

        iniciarSesion(correo, contrasena)
    }

    private fun iniciarSesion(
        correo: String,
        contrasena: String
    ) {

        mostrarCarga(true)

        lifecycleScope.launch {

            try {

                val usuario = database
                    .usuarioDao()
                    .iniciarSesion(
                        correo = correo,
                        contrasena = contrasena
                    )

                mostrarCarga(false)

                if (usuario != null) {

                    Toast.makeText(
                        this@MainActivity,
                        "Inicio de sesión correcto",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(
                        this@MainActivity,
                        BienvenidaActivity::class.java
                    )

                    intent.putExtra(
                        "NOMBRE_USUARIO",
                        usuario.nombre
                    )

                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "Correo o contraseña incorrectos",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (error: Exception) {

                mostrarCarga(false)

                Toast.makeText(
                    this@MainActivity,
                    "No se pudo iniciar sesión",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun limpiarErrores() {
        binding.tilCorreo.error = null
        binding.tilContrasena.error = null
    }

    private fun mostrarCarga(mostrar: Boolean) {

        binding.progressBar.visibility =
            if (mostrar) View.VISIBLE else View.GONE

        binding.btnIniciarSesion.isEnabled = !mostrar
        binding.tvRegistrate.isEnabled = !mostrar
    }
}