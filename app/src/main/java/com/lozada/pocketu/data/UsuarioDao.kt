package com.lozada.pocketu.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsuarioDao {

    // CREATE
    @Insert
    suspend fun registrarUsuario(
        usuario: Usuario
    ): Long

    // READ - Obtener todos los usuarios
    @Query("SELECT * FROM usuarios")
    suspend fun getAll(): List<Usuario>

    // READ - Buscar usuario por correo
    @Query(
        """
        SELECT * FROM usuarios
        WHERE correo = :correo
        LIMIT 1
        """
    )
    suspend fun buscarUsuarioPorCorreo(
        correo: String
    ): Usuario?

    // READ - Iniciar sesión
    @Query(
        """
        SELECT * FROM usuarios
        WHERE correo = :correo
        AND contrasena = :contrasena
        LIMIT 1
        """
    )
    suspend fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Usuario?

    // UPDATE
    @Update
    suspend fun update(
        usuario: Usuario
    )

    // DELETE
    @Delete
    suspend fun delete(
        usuario: Usuario
    )
}