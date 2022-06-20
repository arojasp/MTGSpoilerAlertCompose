package es.alejandro.mtgspoileralert.backgroundservice

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import es.alejandro.mtgspoileralert.cards.repository.ICardsRepository
import es.alejandro.mtgspoileralert.datastore.SetsDataStoreManager
import kotlinx.coroutines.flow.first

@HiltWorker
class CallWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: ICardsRepository,
    val setsStoreManager: SetsDataStoreManager
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val result = repository.getCardsForSet(setsStoreManager.sets.first())
        Log.d(TAG, "" + result)
        return Result.success()
    }

    companion object {
        const val TAG = "CallWorker"
    }
}
