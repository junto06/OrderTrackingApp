package com.mudassar.feature.chat.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mudassar.feature.chat.domain.ChatRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ChatSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val chatRepository: ChatRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val success = chatRepository.syncPendingMessages()
        return if (success) Result.success() else Result.retry()
    }
}
