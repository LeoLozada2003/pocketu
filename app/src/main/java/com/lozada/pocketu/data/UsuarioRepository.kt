// UsuarioRepository.kt

package com.lozada.pocketu.data


class UsuarioRepository(
    private val dao: UsuarioDao
) {


    suspend fun obtenerUsuarios(): List<Usuario> {

        return dao.getAll()

    }


    suspend fun registrarUsuario(usuario: Usuario): Long {

        return dao.registrarUsuario(usuario)

    }


    suspend fun actualizarUsuario(usuario: Usuario) {

        dao.update(usuario)

    }


    suspend fun eliminarUsuario(usuario: Usuario) {

        dao.delete(usuario)

    }

}