package es.alejandro.mtgspoileralert.backgroundservice

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import es.alejandro.mtgspoileralert.sets.repository.ISetsRepository

@HiltWorker
class CallWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: ISetsRepository
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val result = repository.getAllSets()
        Log.d(TAG, "" + result)
        return Result.success()
    }

    companion object {
        const val TAG = "CallWorker"
    }
}
