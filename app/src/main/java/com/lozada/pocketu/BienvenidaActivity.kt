package com.lozada.pocketu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lozada.pocketu.databinding.ActivityBienvenidaBinding
import java.util.concurrent.TimeUnit

class BienvenidaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBienvenidaBinding
    private var usuarioId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Capturamos el ID que viene del Login
        usuarioId = intent.getIntExtra("USUARIO_ID", 0)

        mostrarMensajeBienvenida()
        configurarBotonVerMovimientos()
        configurarBotonAgregarMovimiento()

        // <-- NUEVO: Llamamos a la configuración del botón de cerrar sesión
        configurarBotonCerrarSesion()

        // Llamamos a la función que arranca el Worker en segundo plano
        programarRevisionDeInactividad()
    }

    private fun mostrarMensajeBienvenida() {
        val nombreUsuario = intent.getStringExtra("NOMBRE_USUARIO")

        binding.tvMensajeBienvenida.text = if (!nombreUsuario.isNullOrBlank()) {
            "¡Bienvenido, $nombreUsuario!"
        } else {
            "¡Bienvenido a PocketU!"
        }
    }

    private fun configurarBotonVerMovimientos() {
        binding.btnVerMovimientos.setOnClickListener {
            val intent = Intent(this, MovimientosActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
        }
    }

    private fun configurarBotonAgregarMovimiento() {
        binding.btnAgregarMovimiento.setOnClickListener {
            val intent = Intent(this, AgregarMovimientoActivity::class.java)
            intent.putExtra("USUARIO_ID", usuarioId)
            startActivity(intent)
        }
    }

    // --- NUEVA FUNCIÓN: Cerrar sesión ---
    private fun configurarBotonCerrarSesion() {
        binding.btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // Estas flags borran el historial de pantallas.
            // Así el usuario no puede presionar "Atrás" en el celular y volver a esta pantalla.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun programarRevisionDeInactividad() {
        val workRequest = PeriodicWorkRequestBuilder<InactividadWorker>(24, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RevisionInactividad",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}