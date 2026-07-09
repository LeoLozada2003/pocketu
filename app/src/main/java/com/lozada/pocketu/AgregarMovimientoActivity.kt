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
    // CAMBIO 1: Declaramos el Spinner para la categoría en lugar de EditText
    private lateinit var spCategoria: Spinner
    private lateinit var edtFecha: EditText
    private lateinit var spTipo: Spinner
    private lateinit var btnGuardar: Button

    private var modoEditar = false
    private var movimientoId = 0
    private var usuarioId = 0

    // Lista de categorías predefinidas
    private val opcionesCategoria = arrayOf("Comida", "Transporte", "Académico", "Entretenimiento", "Otros")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_movimiento)

        // 1. Enlazamos las vistas con los IDs del XML
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtMonto = findViewById(R.id.edtMonto)
        // CAMBIO 2: Enlazamos el nuevo ID del Spinner
        spCategoria = findViewById(R.id.spCategoria)
        edtFecha = findViewById(R.id.edtFecha)
        spTipo = findViewById(R.id.spTipo)
        btnGuardar = findViewById(R.id.btnGuardar)

        // 2. Configuramos el Spinner de Tipo (Ingreso / Gasto)
        val opcionesTipo = arrayOf("Ingreso", "Gasto")
        spTipo.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            opcionesTipo
        )

        // CAMBIO 3: Configuramos el nuevo Spinner de Categorías
        spCategoria.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            opcionesCategoria
        )

        // 3. Verificamos si vamos a editar un registro o a crear uno nuevo, y obtenemos el usuario
        leerDatosEditar()

        // 4. Acción del botón Guardar
        btnGuardar.setOnClickListener {
            val descripcion = edtDescripcion.text.toString().trim()
            val montoTexto = edtMonto.text.toString().trim()
            val fechaIngresada = edtFecha.text.toString().trim()
            val tipo = spTipo.selectedItem.toString()
            // CAMBIO 4: Obtenemos el texto del item seleccionado en el Spinner
            val categoria = spCategoria.selectedItem.toString()

            if (descripcion.isEmpty() || montoTexto.isEmpty() || fechaIngresada.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val montoValidado = montoTexto.toDoubleOrNull() ?: 0.0

            if (montoValidado <= 0) {
                Toast.makeText(this, "El monto debe ser mayor a $0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construimos el objeto
            val movimiento = Movimiento(
                id = if (modoEditar) movimientoId else 0,
                usuarioId = usuarioId,
                descripcion = descripcion,
                monto = montoValidado,
                tipo = tipo,
                categoria = categoria, // Se guarda la categoría seleccionada
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
            edtFecha.setText(intent.getStringExtra("FECHA"))

            // Seleccionamos el valor correcto en el Spinner de Tipo
            val tipoOriginal = intent.getStringExtra("TIPO")
            if (tipoOriginal == "Gasto") {
                spTipo.setSelection(1) // Índice 1 es Gasto
            } else {
                spTipo.setSelection(0) // Índice 0 es Ingreso
            }

            // CAMBIO 5: Seleccionamos el valor correcto en el Spinner de Categorías
            val categoriaOriginal = intent.getStringExtra("CATEGORIA")
            val indexCategoria = opcionesCategoria.indexOf(categoriaOriginal)
            if (indexCategoria >= 0) {
                spCategoria.setSelection(indexCategoria)
            }

            btnGuardar.text = "Actualizar Movimiento"
        } else {
            // Si es un movimiento nuevo, ponemos la fecha de hoy automáticamente
            val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            edtFecha.setText(fechaHoy)
        }
    }
}