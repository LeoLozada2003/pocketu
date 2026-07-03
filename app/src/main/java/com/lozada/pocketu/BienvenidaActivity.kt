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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mostrarMensajeBienvenida()
        configurarBotonVerMovimientos()
        configurarBotonAgregarMovimiento()

        // 1. Llamamos a la función que arranca el Worker en segundo plano
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

    // Abre la actividad para listar el historial de ingresos/gastos
    private fun configurarBotonVerMovimientos() {
        binding.btnVerMovimientos.setOnClickListener {
            val intent = Intent(this, MovimientosActivity::class.java)
            startActivity(intent)
        }
    }

    // Abre la actividad para registrar un nuevo movimiento
    private fun configurarBotonAgregarMovimiento() {
        binding.btnAgregarMovimiento.setOnClickListener {
            val intent = Intent(this, AgregarMovimientoActivity::class.java)
            startActivity(intent)
        }
    }

    // 2. Función encargada de agendar el recordatorio
    private fun programarRevisionDeInactividad() {
        // Configura el Worker para que se ejecute una vez cada 24 horas
        val workRequest = PeriodicWorkRequestBuilder<InactividadWorker>(24, TimeUnit.HOURS)
            .build()

        // Encola el trabajo asegurando que si ya existe uno, se reemplace para no duplicar
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "RevisionInactividad",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}