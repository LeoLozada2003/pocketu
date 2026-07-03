package com.lozada.pocketu

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.data.Movimiento
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgregarMovimientoActivity : AppCompatActivity() {

    private lateinit var edtDescripcion: EditText
    private lateinit var edtMonto: EditText
    private lateinit var edtCategoria: EditText
    private lateinit var edtFecha: EditText
    private lateinit var spTipo: Spinner
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_movimiento)

        // 1. Enlazamos las vistas con los IDs del XML
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtMonto = findViewById(R.id.edtMonto)
        edtCategoria = findViewById(R.id.edtCategoria)
        edtFecha = findViewById(R.id.edtFecha)
        spTipo = findViewById(R.id.spTipo)
        btnGuardar = findViewById(R.id.btnGuardar)

        // 2. Configuramos el Spinner (Ingreso / Gasto)
        val opciones = arrayOf("Ingreso", "Gasto")
        spTipo.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            opciones
        )

        // 3. Autocompletamos la fecha de hoy por comodidad
        val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        edtFecha.setText(fechaHoy)

        // 4. Acción del botón Guardar
        btnGuardar.setOnClickListener {

            val descripcion = edtDescripcion.text.toString().trim()
            val montoTexto = edtMonto.text.toString().trim()
            val categoria = edtCategoria.text.toString().trim()
            val fechaIngresada = edtFecha.text.toString().trim()
            val tipo = spTipo.selectedItem.toString()

            // Validamos que ningún campo esté vacío
            if (descripcion.isEmpty() || montoTexto.isEmpty() || categoria.isEmpty() || fechaIngresada.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creamos el objeto Movimiento
            val movimiento = Movimiento(
                descripcion = descripcion,
                monto = montoTexto.toDouble(),
                tipo = tipo,
                categoria = categoria,
                fecha = fechaIngresada // Usamos la fecha que está en el EditText
            )

            // Guardamos en la base de datos usando corrutinas
            lifecycleScope.launch {
                AppDatabase
                    .getDatabase(this@AgregarMovimientoActivity)
                    .movimientoDao()
                    .insertarMovimiento(movimiento)

                // Volvemos al hilo principal para mostrar el mensaje y cerrar
                runOnUiThread {
                    Toast.makeText(
                        this@AgregarMovimientoActivity,
                        "Movimiento guardado",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish() // Cierra la pantalla y vuelve al menú
                }
            }
        }
    }
}