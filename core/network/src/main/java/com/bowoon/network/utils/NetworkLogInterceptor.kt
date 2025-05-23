package com.bowoon.network.utils

import com.bowoon.common.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import javax.inject.Inject

/**
 * Network Log Message 출력
 */
class NetworkLogInterceptor @Inject constructor(

) : Interceptor {
    companion object {
        private const val TAG = "#NetworkLogInterceptor"
        private const val STRING_DIVIDER_CNT = 1000
        private val NETWORK_LOG_BODY = """
            ----------------------------------------------------------------------------------
            ReqMethod : %s
            ReqUrl : %s
            Query : %s
            ReqBody : %s
            ReqHeader : %s
            ----------------------------------------------------------------------------------
            ResHeader : %s
            ResBody : %s
            ----------------------------------------------------------------------------------
        """.trimIndent()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder().build()
        val reqHeader = request.headers.names().map { key ->
            "$key: ${request.headers[key]}"
        }
        val query = request.url.query
        val response = chain.proceed(request)
        val resHeader = response.headers.toString()
        val bodyString = response.body?.string() ?: ""
        var networkLog = String.format(
            NETWORK_LOG_BODY,
            request.method,
            request.url,
            query,
            bodyToString(request),
            reqHeader.toString(),
            resHeader,
            bodyString
        )

        runCatching {
            while (networkLog.isNotEmpty()) {
                if (networkLog.length > STRING_DIVIDER_CNT) {
                    Log.d(TAG, networkLog.substring(0, STRING_DIVIDER_CNT))
                    networkLog = networkLog.substring(STRING_DIVIDER_CNT)
                } else {
                    Log.d(TAG, networkLog)
                    break
                }
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }

        return response.newBuilder().body(bodyString.toResponseBody(response.body?.contentType())).build()
    }

    private fun bodyToString(request: Request): String? =
        runCatching {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        }.getOrElse { e ->
            Log.printStackTrace(e)
            null
        }
}