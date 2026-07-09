package com.lozada.pocketu

object Validaciones {

    // 1. Verificación de la contraseña (más de 6 caracteres)
    fun validarContrasena(contrasena: String): Boolean {
        if (contrasena.isEmpty()) return false
        return contrasena.length >= 6
    }

    // 2. Validación del formato de correo
    // Nota: Usamos Regex puro de Kotlin porque Patterns.EMAIL_ADDRESS requiere Android
    fun validarCorreo(correo: String): Boolean {
        if (correo.isEmpty()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()
        return correo.matches(emailRegex)
    }

    // 3. Validación del monto (no negativo o cero)
    fun validarMonto(monto: Double): Boolean {
        return monto > 0.0
    }
}