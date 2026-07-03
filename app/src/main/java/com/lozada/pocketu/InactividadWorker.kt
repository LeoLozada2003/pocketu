package com.lozada.pocketu

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lozada.pocketu.data.AppDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class InactividadWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        // 1. Instanciamos la base de datos de Room usando el contexto del Worker
        val database = AppDatabase.getDatabase(context)

        // 2. Leemos el último movimiento desde Room
        val ultimoMovimiento = database.movimientoDao().obtenerUltimoMovimiento()

        if (ultimoMovimiento != null) {
            // 3. Calculamos la diferencia de tiempo
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaGuardada = sdf.parse(ultimoMovimiento.fecha)
            val fechaActual = Date()

            if (fechaGuardada != null) {
                val diferenciaMilisegundos = fechaActual.time - fechaGuardada.time

                // Convertimos la diferencia a minutos
                val minutosInactividad = TimeUnit.MILLISECONDS.toMinutes(diferenciaMilisegundos)

                // 4. Si pasaron 10 minutos o más, enviamos la notificación
                if (minutosInactividad >= 10) {
                    mostrarNotificacion()
                }
            }
        } else {
            // Opcional: Si no hay NINGÚN movimiento, también le recordamos
            mostrarNotificacion()
        }

        return Result.success()
    }

    private fun mostrarNotificacion() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "canal_inactividad_pocketu"

        // Para Android 8.0 o superior, se requiere un Canal de Notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Recordatorios de PocketU",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Al tocar la notificación, abrimos la pantalla para agregar gastos
        val intent = Intent(context, AgregarMovimientoActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construimos el diseño de la notificación
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round) // Tu logo
            .setContentTitle("¡No pierdas el hilo!")
            .setContentText("Llevas varios días sin registrar movimientos. Mantén tus finanzas al día.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Enviamos la notificación
        notificationManager.notify(1, builder.build())
    }
}