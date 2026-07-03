package com.lozada.pocketu

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.data.Usuario
import com.lozada.pocketu.databinding.ActivityRegistroBinding
import kotlinx.coroutines.launch

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var database: AppDatabase
    private var modoEditar = false

    private var usuarioId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(applicationContext)
        leerDatosEditar()
        configurarEventos()
    }

    private fun leerDatosEditar() {

        modoEditar = intent.getBooleanExtra(
            "EDITAR",
            false
        )

        if (!modoEditar) return

        usuarioId = intent.getIntExtra(
            "ID",
            0
        )

        binding.etNombre.setText(
            intent.getStringExtra("NOMBRE")
        )

        binding.etCorreoRegistro.setText(
            intent.getStringExtra("CORREO")
        )

        val contrasena =
            intent.getStringExtra("CONTRASENA")

        binding.etContrasenaRegistro.setText(contrasena)

        binding.etConfirmarContrasena.setText(contrasena)

        binding.btnRegistrarse.text = "Actualizar Usuario"

    }
    private fun configurarEventos() {

        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.tvYaTengoCuenta.setOnClickListener {
            finish()
        }

        binding.btnRegistrarse.setOnClickListener {
            validarFormulario()
        }
    }

    private fun validarFormulario() {

        limpiarErrores()

        val nombre = binding.etNombre.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val correo = binding.etCorreoRegistro.text
            ?.toString()
            ?.trim()
            ?.lowercase()
            .orEmpty()

        val contrasena = binding.etContrasenaRegistro.text
            ?.toString()
            ?.trim()
            .orEmpty()

        val confirmarContrasena = binding.etConfirmarContrasena.text
            ?.toString()
            ?.trim()
            .orEmpty()

        if (nombre.isEmpty()) {
            binding.tilNombre.error = "Ingresa tu nombre completo"
            binding.etNombre.requestFocus()
            return
        }

        if (nombre.length < 3) {
            binding.tilNombre.error =
                "El nombre debe tener al menos 3 caracteres"

            binding.etNombre.requestFocus()
            return
        }

        if (correo.isEmpty()) {
            binding.tilCorreoRegistro.error =
                "Ingresa tu correo electrónico"

            binding.etCorreoRegistro.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.tilCorreoRegistro.error =
                "Ingresa un correo electrónico válido"

            binding.etCorreoRegistro.requestFocus()
            return
        }

        if (contrasena.isEmpty()) {
            binding.tilContrasenaRegistro.error =
                "Ingresa una contraseña"

            binding.etContrasenaRegistro.requestFocus()
            return
        }

        if (contrasena.length < 6) {
            binding.tilContrasenaRegistro.error =
                "La contraseña debe tener al menos 6 caracteres"

            binding.etContrasenaRegistro.requestFocus()
            return
        }

        if (confirmarContrasena.isEmpty()) {
            binding.tilConfirmarContrasena.error =
                "Confirma tu contraseña"

            binding.etConfirmarContrasena.requestFocus()
            return
        }

        if (contrasena != confirmarContrasena) {
            binding.tilConfirmarContrasena.error =
                "Las contraseñas no coinciden"

            binding.etConfirmarContrasena.requestFocus()
            return
        }

        if (modoEditar) {

            actualizarUsuario(
                nombre,
                correo,
                contrasena
            )

        } else {

            registrarUsuario(
                nombre,
                correo,
                contrasena
            )

        }
    }

    private fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String
    ) {

        mostrarCarga(true)

        lifecycleScope.launch {

            try {

                val usuarioExistente = database
                    .usuarioDao()
                    .buscarUsuarioPorCorreo(correo)

                if (usuarioExistente != null) {

                    mostrarCarga(false)

                    binding.tilCorreoRegistro.error =
                        "Este correo ya está registrado"

                    binding.etCorreoRegistro.requestFocus()

                    return@launch
                }

                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena
                )

                val resultado = database
                    .usuarioDao()
                    .registrarUsuario(nuevoUsuario)

                mostrarCarga(false)

                if (resultado > 0) {

                    Toast.makeText(
                        this@RegistroActivity,
                        "Cuenta creada correctamente",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    Toast.makeText(
                        this@RegistroActivity,
                        "No se pudo crear la cuenta",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (error: Exception) {

                mostrarCarga(false)

                Toast.makeText(
                    this@RegistroActivity,
                    "Ocurrió un error al registrar el usuario",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun actualizarUsuario(
        nombre: String,
        correo: String,
        contrasena: String
    ) {

        mostrarCarga(true)

        lifecycleScope.launch {

            try {

                val usuarioExistente = database
                    .usuarioDao()
                    .buscarUsuarioPorCorreo(correo)

                if (
                    usuarioExistente != null &&
                    usuarioExistente.id != usuarioId
                ) {

                    mostrarCarga(false)

                    binding.tilCorreoRegistro.error =
                        "Este correo ya está registrado"

                    binding.etCorreoRegistro.requestFocus()

                    return@launch
                }

                val usuarioActualizado = Usuario(
                    id = usuarioId,
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena
                )

                database
                    .usuarioDao()
                    .update(usuarioActualizado)

                mostrarCarga(false)

                Toast.makeText(
                    this@RegistroActivity,
                    "Usuario actualizado correctamente",
                    Toast.LENGTH_LONG
                ).show()

                finish()

            } catch (error: Exception) {

                mostrarCarga(false)

                Toast.makeText(
                    this@RegistroActivity,
                    "No se pudo actualizar el usuario",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

    }
    private fun limpiarErrores() {
        binding.tilNombre.error = null
        binding.tilCorreoRegistro.error = null
        binding.tilContrasenaRegistro.error = null
        binding.tilConfirmarContrasena.error = null
    }

    private fun mostrarCarga(mostrar: Boolean) {

        binding.progressBarRegistro.visibility =
            if (mostrar) View.VISIBLE else View.GONE

        binding.btnRegistrarse.isEnabled = !mostrar
        binding.btnVolver.isEnabled = !mostrar
        binding.tvYaTengoCuenta.isEnabled = !mostrar

        binding.etNombre.isEnabled = !mostrar
        binding.etCorreoRegistro.isEnabled = !mostrar
        binding.etContrasenaRegistro.isEnabled = !mostrar
        binding.etConfirmarContrasena.isEnabled = !mostrar
    }
}