package com.lozada.pocketu

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionesTest {

    // ==========================================
    // PRUEBA 1: VALIDACIÓN DE CONTRASEÑA
    // ==========================================
    @Test
    fun validarContrasena_menorA6Caracteres_retornaFalse() {
        // ARRANGE (Preparar)
        val contrasenaInvalida = "12345"

        // ACT (Actuar)
        val resultado = Validaciones.validarContrasena(contrasenaInvalida)

        // ASSERT (Verificar)
        assertFalse("Una contraseña de 5 caracteres debe fallar", resultado)
    }

    @Test
    fun validarContrasena_seisOMasCaracteres_retornaTrue() {
        // ARRANGE
        val contrasenaValida = "123456" // Caso de borde: exactamente 6
        val contrasenaLarga = "MiSuperClaveSegura" // Caso válido

        // ACT
        val resultadoBorde = Validaciones.validarContrasena(contrasenaValida)
        val resultadoLarga = Validaciones.validarContrasena(contrasenaLarga)

        // ASSERT
        assertTrue("Una contraseña de exactamente 6 caracteres debe ser válida", resultadoBorde)
        assertTrue("Una contraseña mayor a 6 caracteres debe ser válida", resultadoLarga)
    }

    // ==========================================
    // PRUEBA 2: VALIDACIÓN DE CORREO
    // ==========================================
    @Test
    fun validarCorreo_formatoInvalido_retornaFalse() {
        // ARRANGE
        val sinArroba = "usuariogmail.com"
        val sinDominio = "usuario@" // Caso de borde
        val dobleArroba = "usuario@@gmail.com" // Caso de borde

        // ACT
        val resSinArroba = Validaciones.validarCorreo(sinArroba)
        val resSinDominio = Validaciones.validarCorreo(sinDominio)
        val resDobleArroba = Validaciones.validarCorreo(dobleArroba)

        // ASSERT
        assertFalse("Correo sin @ no es válido", resSinArroba)
        assertFalse("Correo que termina en @ no es válido", resSinDominio)
        assertFalse("Correo con doble @ no es válido", resDobleArroba)
    }

    @Test
    fun validarCorreo_formatoValido_retornaTrue() {
        // ARRANGE
        val correoValido = "estudiante@universidad.edu.ec"

        // ACT
        val resultado = Validaciones.validarCorreo(correoValido)

        // ASSERT
        assertTrue("El formato con @ y dominio correcto debe pasar", resultado)
    }

    // ==========================================
    // PRUEBA 3: VALIDACIÓN DE MONTO
    // ==========================================
    @Test
    fun validarMonto_ceroONegativo_retornaFalse() {
        // ARRANGE
        val montoCero = 0.0
        val montoNegativo = -5.50

        // ACT
        val resCero = Validaciones.validarMonto(montoCero)
        val resNegativo = Validaciones.validarMonto(montoNegativo)

        // ASSERT
        assertFalse("Un monto de 0.0 debe ser inválido", resCero)
        assertFalse("Un monto negativo debe ser inválido", resNegativo)
    }

    @Test
    fun validarMonto_mayorACero_retornaTrue() {
        // ARRANGE
        val montoValido = 15.50
        val montoBorde = 0.01 // Caso de borde: el monto positivo más pequeño posible

        // ACT
        val resValido = Validaciones.validarMonto(montoValido)
        val resBorde = Validaciones.validarMonto(montoBorde)

        // ASSERT
        assertTrue("Un monto de 15.50 debe ser válido", resValido)
        assertTrue("Un monto de 0.01 debe ser válido", resBorde)
    }
}