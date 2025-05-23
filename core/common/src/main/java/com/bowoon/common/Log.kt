package com.bowoon.common

import com.bowoon.kurlypretest.core.common.BuildConfig

object Log {
    private val IS_SHOWING = BuildConfig.IS_DEBUGGING_LOGGING
    private const val PREFIX = "kurlypretest"

    fun i(tag: String, msg: String) {
        if (IS_SHOWING) android.util.Log.i("$PREFIX$tag", getMessageWithLineNumber(msg))
    }

    fun i(msg: String) {
        if (IS_SHOWING) android.util.Log.i("$PREFIX${tag()}", getMessageWithLineNumber(msg))
    }

    fun v(tag: String, msg: String) {
        if (IS_SHOWING) android.util.Log.v("$PREFIX$tag", getMessageWithLineNumber(msg))
    }

    fun v(msg: String) {
        if (IS_SHOWING) android.util.Log.v("$PREFIX${tag()}", getMessageWithLineNumber(msg))
    }

    fun d(tag: String, msg: String) {
        if (IS_SHOWING) android.util.Log.d("$PREFIX$tag", getMessageWithLineNumber(msg))
    }

    fun d(msg: String) {
        if (IS_SHOWING) android.util.Log.d("$PREFIX${tag()}", getMessageWithLineNumber(msg))
    }

    fun w(tag: String, msg: String) {
        if (IS_SHOWING) android.util.Log.d("$PREFIX$tag", getMessageWithLineNumber(msg))
    }

    fun w(msg: String) {
        if (IS_SHOWING) android.util.Log.w("$PREFIX${tag()}", getMessageWithLineNumber(msg))
    }

    fun e(tag: String, msg: String) {
        if (IS_SHOWING) android.util.Log.d("$PREFIX$tag", getMessageWithLineNumber(msg))
    }

    fun e(msg: String) {
        if (IS_SHOWING) android.util.Log.e("$PREFIX${tag()}", getMessageWithLineNumber(msg))
    }

    fun printStackTrace(tr: Throwable? = null) {
        if (IS_SHOWING) tr?.printStackTrace()
    }

    private fun getMessageWithLineNumber(msg: String): String =
        Thread.currentThread().stackTrace.let { trace ->
            val firstMatchIndex = trace.indexOfFirst { it.className.equals("com.bowoon.common.Log") && it.fileName.equals("Log.kt") }
            val index = if (firstMatchIndex == -1) return msg else firstMatchIndex + 2

            if (index in trace.indices) {
                "(${trace[index].fileName}:${trace[index].lineNumber}) $msg"
            } else {
                msg
            }
        }

    private fun tag(): String =
        Thread.currentThread().stackTrace.let { trace ->
            val firstMatchIndex = trace.indexOfFirst { it.className.equals("com.bowoon.common.Log") && it.fileName.equals("Log.kt") }
            val index = if (firstMatchIndex == -1) return "LinkNotFound" else firstMatchIndex + 2

            if (index in trace.indices) {
                "(${trace[index].fileName}:${trace[index].lineNumber})"
            } else {
                "LinkNotFound"
            }
        }
}