package com.lozada.pocketu

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.lozada.pocketu.adapter.UsuarioAdapter
import com.lozada.pocketu.data.AppDatabase
import com.lozada.pocketu.data.Usuario
import com.lozada.pocketu.data.UsuarioRepository
import com.lozada.pocketu.databinding.ActivityUsuariosBinding
import com.lozada.pocketu.viewmodel.UsuarioViewModel
import com.lozada.pocketu.viewmodel.UsuarioViewModelFactory

class UsuariosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuariosBinding
    private lateinit var adapter: UsuarioAdapter

    private val viewModel: UsuarioViewModel by viewModels {
        UsuarioViewModelFactory(
            UsuarioRepository(
                AppDatabase.getDatabase(applicationContext)
                    .usuarioDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarRecyclerView()
        observarUsuarios()

        viewModel.cargarUsuarios()
    }

    private fun configurarRecyclerView() {

        adapter = UsuarioAdapter(

            emptyList(),

            onClick = { usuario ->

                editarUsuario(usuario)

            },

            onLongClick = { usuario ->

                confirmarEliminar(usuario)

            }

        )

        binding.recyclerUsuarios.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerUsuarios.adapter = adapter
    }

    private fun observarUsuarios() {

        viewModel.usuarios.observe(this) { lista ->

            adapter.actualizarLista(lista)

        }

    }

    private fun editarUsuario(usuario: Usuario) {

        val intent = Intent(
            this,
            RegistroActivity::class.java
        )

        intent.putExtra("EDITAR", true)
        intent.putExtra("ID", usuario.id)
        intent.putExtra("NOMBRE", usuario.nombre)
        intent.putExtra("CORREO", usuario.correo)
        intent.putExtra("CONTRASENA", usuario.contrasena)

        startActivity(intent)

    }

    private fun confirmarEliminar(usuario: Usuario) {

        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar usuario")
            .setMessage(
                "¿Deseas eliminar a ${usuario.nombre}?"
            )
            .setPositiveButton("Eliminar") { _, _ ->

                eliminarUsuario(usuario)

            }
            .setNegativeButton("Cancelar", null)
            .show()

    }

    private fun eliminarUsuario(usuario: Usuario) {

        viewModel.eliminarUsuario(usuario)

        Snackbar.make(
            binding.root,
            "Usuario eliminado",
            Snackbar.LENGTH_LONG
        )
            .setAction("DESHACER") {

                val usuarioRestaurado = usuario.copy(
                    id = 0
                )

                viewModel.registrarUsuario(usuarioRestaurado)

            }
            .show()

    }

    override fun onResume() {
        super.onResume()

        viewModel.cargarUsuarios()
    }
}