package com.mudassar.feature.chat.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mudassar.feature.chat.domain.ChatSyncScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WorkManagerChatSyncScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : ChatSyncScheduler {

    override fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<ChatSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            CHAT_SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            request
        )
    }
}

private const val CHAT_SYNC_WORK_NAME = "chat_sync_work"
