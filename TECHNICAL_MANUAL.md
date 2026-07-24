Manual Técnico — PocketU v1.0

1. Descripción del sistema

PocketU es una aplicación móvil diseñada para dispositivos Android, creada específicamente para estudiantes universitarios. El problema central que resuelve es la falta de control y organización sobre las finanzas personales durante la etapa estudiantil.

El alcance de este Producto Mínimo Viable (MVP) v1.0 permite a los usuarios:

Registrar ingresos y egresos de forma rápida.

Visualizar un historial detallado de movimientos.

Gestionar su presupuesto de manera local sin depender de una conexión a internet.

2. Arquitectura de la aplicación

PocketU sigue una arquitectura estructurada para separar las responsabilidades y facilitar el mantenimiento.

Patrón de diseño principal: Modelo-Vista-Controlador (MVC) adaptado al ciclo de vida de Android.

Capa de Presentación (UI): Compuesta por las Activities y los archivos XML (MainActivity, BienvenidaActivity, RegistroActivity, etc.). Se encarga exclusivamente de mostrar los datos y capturar las interacciones del usuario.

Capa de Lógica de Negocio: Gestionada a través de las clases en Kotlin que actúan como controladores. Aquí se procesan las reglas de validación (por ejemplo, asegurar que un gasto no se registre con valor nulo).

Capa de Datos: Maneja la persistencia local utilizando SQLite de forma directa a través de clases SQLiteOpenHelper.

3. Modelo de datos

La persistencia de datos se basa en un esquema relacional ligero. A continuación se describen las entidades principales:

Usuario:

id_usuario (Clave Primaria, Autoincremental)

nombre (Texto)

correo (Texto)

Movimiento (Transacción):

id_movimiento (Clave Primaria, Autoincremental)

tipo (Texto: "Ingreso" o "Egreso")

monto (Decimal/Real)

fecha (Texto/Fecha)

descripcion (Texto)

id_usuario (Clave Foránea)

4. Tecnologías y librerías

El entorno de desarrollo está estandarizado bajo las siguientes herramientas:

Entorno de Desarrollo: Android Studio

Lenguaje de Programación: Kotlin

Base de Datos Local: SQLite (Integrada nativamente en Android)

Librerías / Dependencias Principales:

AndroidX (Core, AppCompat, ConstraintLayout, Material Design).

No se requieren librerías externas de terceros (como Retrofit o Glide) para la persistencia local de este MVP, lo que garantiza una huella de memoria mínima (~72MB).

5. Instrucciones para compilar

Para levantar el proyecto en un entorno de desarrollo local, sigue estos pasos:

Requisitos Previos:

Android Studio (Versión recomendada: Flamingo o superior).

Java Development Kit (JDK) 11 o superior (requerido para la compilación de Kotlin).

SDK mínimo (MinSdkVersion): API 24 (Android 7.0).

SDK objetivo (TargetSdkVersion): API 33 (Android 13).

Pasos de compilación:

Clonar el repositorio:

git clone https://github.com/LeoLozada2003/pocketu.git


Abrir en Android Studio: Abre el IDE, selecciona "Open" y navega hasta la carpeta del proyecto clonado.

Sincronización: Espera a que Gradle descargue las dependencias de AndroidX y construya el proyecto (esto ocurrirá automáticamente al abrir).

Ejecución: Conecta un dispositivo físico o inicia un Emulador (AVD) y presiona el botón "Run 'app'" (el icono de play verde).

(Nota: Este MVP no requiere configuración de variables de entorno ni archivos google-services.json, ya que no se conecta a APIs de terceros ni a Firebase).

6. Estructura del repositorio

pocketu/
├── app/                      # Módulo principal de la aplicación
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/         # Código fuente de los controladores y lógica en Kotlin (mantiene este nombre por convención de Android)
│   │   │   ├── res/          # Recursos gráficos (layouts XML, drawables, strings)
│   │   │   └── AndroidManifest.xml # Configuración principal y permisos
│   └── build.gradle.kts      # Configuración de dependencias a nivel de módulo
├── gradle/                   # Wrappers de Gradle para garantizar la versión de compilación
├── .gitignore                # Reglas de exclusión para archivos temporales
├── build.gradle.kts          # Configuración de Gradle a nivel de proyecto
└── README.md                 # Información general del proyecto


7. Historial de versiones

v1.0 — 24 de Julio de 2026 — MVP Final

Lanzamiento estable de la aplicación.

Implementación de interfaz de usuario con navegación fluida.

Creación de la base de datos local SQLite.

Funcionalidad completa para agregar, visualizar y categorizar movimientos financieros.

Optimización de memoria confirmada mediante Profiler.