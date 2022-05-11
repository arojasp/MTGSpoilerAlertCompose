package es.alejandro.mtgspoileralert.backgroundservice

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CallWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.d(TAG, "Worker started")
        return Result.success()
    }

    companion object {
        const val TAG = "CallWorker"
    }
}