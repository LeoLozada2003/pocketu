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

    private var modoEditar = false
    private var movimientoId = 0
    private var usuarioId = 0 // <-- Variable para guardar el ID del usuario

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

        // 3. Verificamos si vamos a editar un registro o a crear uno nuevo, y obtenemos el usuario
        leerDatosEditar()

        // 4. Acción del botón Guardar
        btnGuardar.setOnClickListener {
            val descripcion = edtDescripcion.text.toString().trim()
            val montoTexto = edtMonto.text.toString().trim()
            val categoria = edtCategoria.text.toString().trim()
            val fechaIngresada = edtFecha.text.toString().trim()
            val tipo = spTipo.selectedItem.toString()

            if (descripcion.isEmpty() || montoTexto.isEmpty() || categoria.isEmpty() || fechaIngresada.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- NUEVA VALIDACIÓN: Convertimos el texto a número de forma segura ---
            val montoValidado = montoTexto.toDoubleOrNull() ?: 0.0

            if (montoValidado <= 0) {
                Toast.makeText(this, "El monto debe ser mayor a $0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construimos el objeto
            val movimiento = Movimiento(
                id = if (modoEditar) movimientoId else 0,
                usuarioId = usuarioId, // <-- Le asignamos el movimiento al usuario correcto
                descripcion = descripcion,
                monto = montoValidado, // <-- Usamos la variable que ya validamos
                tipo = tipo,
                categoria = categoria,
                fecha = fechaIngresada
            )

            // Guardamos o actualizamos en la base de datos
            lifecycleScope.launch {
                val dao = AppDatabase.getDatabase(this@AgregarMovimientoActivity).movimientoDao()

                if (modoEditar) {
                    dao.actualizarMovimiento(movimiento)
                } else {
                    dao.insertarMovimiento(movimiento)
                }

                runOnUiThread {
                    Toast.makeText(
                        this@AgregarMovimientoActivity,
                        if (modoEditar) "Movimiento actualizado" else "Movimiento guardado",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun leerDatosEditar() {
        // Capturamos el ID del usuario activo
        usuarioId = intent.getIntExtra("USUARIO_ID", 0)
        modoEditar = intent.getBooleanExtra("EDITAR", false)

        if (modoEditar) {
            movimientoId = intent.getIntExtra("ID", 0)

            edtDescripcion.setText(intent.getStringExtra("DESCRIPCION"))
            edtMonto.setText(intent.getDoubleExtra("MONTO", 0.0).toString())
            edtCategoria.setText(intent.getStringExtra("CATEGORIA"))
            edtFecha.setText(intent.getStringExtra("FECHA"))

            // Seleccionamos el valor correcto en el Spinner
            val tipoOriginal = intent.getStringExtra("TIPO")
            if (tipoOriginal == "Gasto") {
                spTipo.setSelection(1) // Índice 1 es Gasto
            } else {
                spTipo.setSelection(0) // Índice 0 es Ingreso
            }

            btnGuardar.text = "Actualizar Movimiento"
        } else {
            // Si es un movimiento nuevo, ponemos la fecha de hoy automáticamente
            val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            edtFecha.setText(fechaHoy)
        }
    }
}