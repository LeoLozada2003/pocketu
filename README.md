# PocketU 📱💰

## Descripción

PocketU es una aplicación móvil Android diseñada para ayudar a estudiantes universitarios a gestionar sus finanzas personales mediante el registro y control de ingresos y egresos. La aplicación busca ofrecer una forma sencilla, rápida y visual de monitorear el dinero disponible, permitiendo a los usuarios tomar mejores decisiones financieras durante su vida académica.

---

## Problema que Resuelve

Muchos estudiantes universitarios administran sus gastos de forma informal o no llevan un registro de sus movimientos financieros. Esto dificulta conocer cuánto dinero tienen disponible, cuánto han gastado y en qué categorías se concentra su consumo.

PocketU surge como una solución para centralizar la información financiera personal del estudiante y facilitar el seguimiento de sus ingresos, gastos y balance disponible desde un dispositivo móvil.

---

## Objetivo de la Aplicación

Desarrollar una aplicación móvil que permita a los estudiantes universitarios registrar y consultar sus ingresos y egresos de manera sencilla, proporcionando información clara sobre su situación financiera y ayudándoles a mejorar la administración de sus recursos económicos.

---

## Usuarios Objetivo

- Estudiantes universitarios.
- Jóvenes que desean controlar sus gastos personales.
- Usuarios que buscan una herramienta simple para llevar un control financiero básico.

---

## Historias de Usuario del MVP

### HU-01: Registro de ingresos y egresos

**Como** estudiante universitario,

**quiero** registrar mis ingresos y egresos,

**para** llevar un control de mis finanzas personales y conocer mi balance disponible.

---

### HU-02: Visualización del balance

**Como** estudiante universitario,

**quiero** visualizar mi balance actual,

**para** conocer cuánto dinero tengo disponible en cualquier momento.

---

### HU-03: Consulta de movimientos

**Como** estudiante universitario,

**quiero** consultar mis transacciones registradas,

**para** revisar mis movimientos financieros y controlar mis gastos.

---

## Funcionalidades Principales

- Inicio de sesión.
- Visualización del balance actual.
- Registro de ingresos.
- Registro de gastos.
- Clasificación por categorías.
- Consulta de transacciones recientes.
- Resumen financiero mensual.
- Dashboard financiero intuitivo.
- Interfaz basada en Material Design 3.

---

## Funcionalidades implementadas

Actualmente la aplicación cuenta con las siguientes funcionalidades implementadas:

- ✅ Inicio de sesión de usuarios.
- ✅ Registro de nuevos usuarios.
- ✅ Validación del correo electrónico.
- ✅ Validación de contraseña (mínimo 6 caracteres).
- ✅ Validación de campos obligatorios.
- ✅ Autenticación local mediante Room Database.
- ✅ Navegación entre las pantallas de Login, Registro y Principal.
- ✅ Persistencia local de usuarios.
- ✅ Mensajes de error para credenciales inválidas y formularios incompletos.

---

## Capturas de las funcionalidades implementadas

### Inicio de sesión

![Pantalla de Login](login.png)

### Registro de usuarios

![Pantalla de Registro](register.png)

---

## Tecnologías Utilizadas

### Framework y herramientas

- Android Studio
- Kotlin
- Jetpack Compose
- Material Design 3
- Arquitectura MVP (Modelo - Vista - Presentador)
- Room Database
- Git
- GitHub
- Figma (Prototipado y diseño UX/UI)

---

## Arquitectura del Proyecto

El proyecto sigue el patrón de arquitectura MVP (Model-View-Presenter), permitiendo una mejor separación de responsabilidades entre la interfaz de usuario, la lógica de negocio y la gestión de datos.

```text
View
  ↓
Presenter
  ↓
Model
```

---

## Instalación

### Requisitos

- Android Studio Ladybug o superior
- JDK 17+
- Android SDK 35
- Git

### Clonar el repositorio

```bash
git clone https://github.com/usuario/pocketu.git
```

### Abrir el proyecto

1. Abrir Android Studio.
2. Seleccionar **Open Project**.
3. Elegir la carpeta del proyecto.
4. Esperar la sincronización de Gradle.
5. Ejecutar la aplicación en un emulador o dispositivo Android.

---

## Capturas de Pantalla

### Pantalla de Bienvenida

![Pantalla de Bienvenida](bienvenida.png)

### Pantalla Principal

![Pantalla Principal](dashboard.png)

### Pantalla de Nueva Transacción

![Pantalla de Nueva Transacción](transaccion.png)

---

## Diseño y Prototipado

Las interfaces fueron diseñadas en Figma siguiendo las recomendaciones de Material Design 3 y posteriormente evaluadas mediante pruebas de usabilidad con usuarios.

### Principales mejoras realizadas

- Mayor claridad en los botones de navegación.
- Mejor jerarquía visual.
- Incremento del contraste de textos.
- Optimización de accesibilidad.
- Mejor comprensión del flujo principal del usuario.

---

## Estado Actual del Proyecto

🟡 **En desarrollo**

Actualmente se encuentra completada la fase de:

- ✅ Investigación del problema.
- ✅ Definición de requerimientos.
- ✅ Historias de usuario.
- ✅ Diseño UX/UI.
- ✅ Wireframes.
- ✅ Prototipo de alta fidelidad en Figma.
- ✅ Evaluación de usabilidad.
- ✅ Refinamiento del MVP.
- ✅ Implementación del inicio de sesión.
- ✅ Implementación del registro de usuarios.
- ✅ Validaciones de formularios.
- ✅ Integración de Room Database.
- ✅ Navegación entre pantallas.

### Próximas actividades

- Implementación del registro de ingresos.
- Implementación del registro de gastos.
- Visualización del balance.
- Gestión de categorías.
- Historial de transacciones.
- Resumen financiero mensual.
- Pruebas funcionales y optimización.

---

## Autor

**Erick Lozada**

Proyecto académico desarrollado para la asignatura de Desarrollo de Aplicaciones Móviles.

---

## Licencia

Este proyecto tiene fines académicos y educativos.