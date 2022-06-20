package es.alejandro.mtgspoileralert.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import es.alejandro.mtgspoileralert.R

class NotificationService(
    val context: Context
) {

    companion object {
        const val CHANNEL_ID = "SetsChannel"
        const val NOTIFICATION_ID = 1
    }

    fun showNewSetsNotification(priority: Int = NotificationCompat.PRIORITY_DEFAULT) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("new sets")
            .setContentText("There are new sets")
            .setPriority(priority)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}
