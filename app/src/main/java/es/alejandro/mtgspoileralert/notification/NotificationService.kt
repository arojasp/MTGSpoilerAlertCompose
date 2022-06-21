package es.alejandro.mtgspoileralert.notification

import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import es.alejandro.mtgspoileralert.R
import es.alejandro.mtgspoileralert.datastore.SetsDataStoreManager
import kotlinx.coroutines.flow.first

class NotificationService(
    val context: Context,
    val datastore: SetsDataStoreManager
) {

    companion object {
        const val CHANNEL_ID = "SetsChannel"
        const val NOTIFICATION_ID = 1
    }

    suspend fun showNewSetsNotification(priority: Int = NotificationCompat.PRIORITY_DEFAULT) {
        val actualSet = datastore.sets.first()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_transparent_foreground)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setContentTitle("New cards")
            .setContentText("There are new cards on $actualSet")
            .setPriority(priority)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}
