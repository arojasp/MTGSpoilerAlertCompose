package es.alejandro.mtgspoileralert.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import es.alejandro.mtgspoileralert.MainActivity
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

        val cardsIntent = Intent(
            Intent.ACTION_VIEW,
            "https://mtgsac.com/set=$actualSet".toUri(),
            context,
            MainActivity::class.java
        )
        val pending = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(cardsIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getPendingIntent(1, PendingIntent.FLAG_MUTABLE)
            } else {
                getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }

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
            .setContentIntent(pending)
            .setPriority(priority)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}
