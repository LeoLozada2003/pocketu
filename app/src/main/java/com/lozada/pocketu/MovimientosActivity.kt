package com.lozada.pocketu

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovimientosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

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
                Toast.makeText(this, "Categoría: ${movimiento.categoria}", Toast.LENGTH_SHORT).show()
            },
            onLongClick = { movimiento ->
                confirmarEliminar(movimiento)
            }
        )

        binding.recyclerMovimientos.layoutManager = LinearLayoutManager(this)
        binding.recyclerMovimientos.adapter = adapter
    }

    private fun observarMovimientos() {
        database.movimientoDao().obtenerMovimientos().observe(this) { lista ->
            adapter.actualizarLista(lista)

            // Opcional: Mostrar un texto si la lista está vacía
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