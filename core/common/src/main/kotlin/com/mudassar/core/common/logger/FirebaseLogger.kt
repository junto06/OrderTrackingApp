package com.mudassar.core.common.logger

import com.mudassar.core.base.ErrorLogger
import timber.log.Timber
import javax.inject.Inject

class FirebaseLogger @Inject constructor() : ErrorLogger {
    override fun log(error: Throwable) {
        Timber.e(error)
        //TODO: Log to Firebase or other error tracking service like Datadog
    }
}
