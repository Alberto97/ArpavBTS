package org.alberto97.arpavbts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object Extensions {

    fun ViewGroup.inflate(layout: Int): View {
        return LayoutInflater.from(this.context).inflate(layout, this, false)
    }

    // Nicely stolen from retrofit, expecting OkHttp to implement this later
    suspend fun Call.await(): ResponseBody {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }

            this.enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val body = response.body
                        if (body == null) {
                            val e = KotlinNullPointerException("Response was null but response body userType was declared as non-null")
                            continuation.resumeWithException(e)
                        } else {
                            continuation.resume(body)
                        }
                    } else {
                        continuation.resumeWithException(IOException(response.code.toString()))
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }
            })
        }
    }
}