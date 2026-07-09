package com.lozada.pocketu

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginUITest {

    // ARRANGE: Esta regla le dice a Espresso que abra tu MainActivity (Login) antes de empezar
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loginConCamposVacios_muestraMensajeError() {
        // ACT: Busca el botón de iniciar sesión por su ID exacto y hace clic en él sin llenar nada
        onView(withId(R.id.btnIniciarSesion))
            .perform(click())

        // ASSERT: Busca en la pantalla el texto exacto del error y verifica que sea visible
        onView(withText("Ingresa tu correo electrónico"))
            .check(matches(isDisplayed()))
    }
}