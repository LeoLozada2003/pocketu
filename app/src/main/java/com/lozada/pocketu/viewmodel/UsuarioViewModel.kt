// UsuarioViewModel.kt

package com.lozada.pocketu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lozada.pocketu.data.Usuario
import com.lozada.pocketu.data.UsuarioRepository
import kotlinx.coroutines.launch


class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {


    private val _usuarios =
        MutableLiveData<List<Usuario>>()


    val usuarios: LiveData<List<Usuario>>
        get() = _usuarios



    fun cargarUsuarios() {

        viewModelScope.launch {

            val lista =
                repository.obtenerUsuarios()

            _usuarios.value = lista

        }

    }


    fun registrarUsuario(usuario: Usuario) {

        viewModelScope.launch {

            repository.registrarUsuario(usuario)

            cargarUsuarios()

        }

    }


    fun actualizarUsuario(usuario: Usuario) {

        viewModelScope.launch {

            repository.actualizarUsuario(usuario)

            cargarUsuarios()

        }

    }


    fun eliminarUsuario(usuario: Usuario) {

        viewModelScope.launch {

            repository.eliminarUsuario(usuario)

            cargarUsuarios()

        }

    }

}