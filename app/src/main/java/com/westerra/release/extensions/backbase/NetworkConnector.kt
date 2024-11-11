package com.westerra.release.extensions.backbase

import com.backbase.android.utils.net.NetworkConnector
import com.backbase.android.utils.net.ServerRequestWorker
import com.backbase.android.utils.net.response.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun NetworkConnector.executeAsSuspended(): Response = suspendCoroutine { cont ->
    val worker = ServerRequestWorker(this@executeAsSuspended) { cont.resume(it) }
    worker.start()
}
