package com.lozada.pocketu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lozada.pocketu.adapter.MovimientoAdapter
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.data.Movimiento
import com.lozada.pocketu.databinding.ActivityMovimientosBinding
import kotlinx.coroutines.launch

class MovimientosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovimientosBinding
    private lateinit var adapter: MovimientoAdapter
    private lateinit var database: AppDatabase

    private var usuarioId = 0 // <-- NUEVO: Variable para el ID del usuario activo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovimientosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        // <-- NUEVO: Obtenemos el ID del usuario que viene de la pantalla de Bienvenida
        usuarioId = intent.getIntExtra("USUARIO_ID", 0)

        configurarEventos()
        configurarRecyclerView()
        observarMovimientos()
    }

    private fun configurarEventos() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun configurarRecyclerView() {
        adapter = MovimientoAdapter(
            movimientos = emptyList(),
            onClick = { movimiento ->
                // --- NAVEGACIÓN PARA EDITAR ---
                val intent = Intent(this, AgregarMovimientoActivity::class.java)
                intent.putExtra("EDITAR", true)
                intent.putExtra("ID", movimiento.id)
                intent.putExtra("USUARIO_ID", usuarioId) // <-- NUEVO: Pasamos el ID del usuario a la pantalla de edición
                intent.putExtra("DESCRIPCION", movimiento.descripcion)
                intent.putExtra("MONTO", movimiento.monto)
                intent.putExtra("TIPO", movimiento.tipo)
                intent.putExtra("CATEGORIA", movimiento.categoria)
                intent.putExtra("FECHA", movimiento.fecha)
                startActivity(intent)
            },
            onLongClick = { movimiento ->
                confirmarEliminar(movimiento)
            }
        )

        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovimientos.adapter = adapter
    }

    private fun observarMovimientos() {
        // <-- NUEVO: Pasamos el usuarioId a la consulta para traer solo los de esta cuenta
        database.movimientoDao().obtenerMovimientos(usuarioId).observe(this) { lista ->
            adapter.actualizarLista(lista)

            // --- CÁLCULO DEL BALANCE ---
            var balance = 0.0
            for (movimiento in lista) {
                if (movimiento.tipo == "Ingreso") {
                    balance += movimiento.monto
                } else if (movimiento.tipo == "Gasto") {
                    balance -= movimiento.monto
                }
            }

            // --- CAMBIO DE COLOR DINÁMICO ---
            if (balance <= 5.0) {
                // Color rojo para balances bajos
                binding.tvBalance.setTextColor(Color.parseColor("#D32F2F"))
            } else {
                // Color verde para balances saludables
                binding.tvBalance.setTextColor(Color.parseColor("#2E7D32"))
            }

            // Muestra el balance
            binding.tvBalance.text = "Balance Total: $$balance"

            if (lista.isEmpty()) {
                Toast.makeText(this, "Aún no tienes movimientos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun confirmarEliminar(movimiento: Movimiento) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar registro")
            .setMessage("¿Deseas eliminar '${movimiento.descripcion}' por $${movimiento.monto}?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarMovimiento(movimiento)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarMovimiento(movimiento: Movimiento) {
        lifecycleScope.launch {
            database.movimientoDao().eliminarMovimiento(movimiento)
            Toast.makeText(this@MovimientosActivity, "Movimiento eliminado", Toast.LENGTH_SHORT).show()
        }
    }
}