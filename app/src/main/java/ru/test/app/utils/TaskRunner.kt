package ru.test.app.utils

import android.os.AsyncTask


object TaskRunner {
    fun <T> runWithIntValue(
        value: Int,
        onGetDataFrom: (value: Int) -> T,
        onSuccess: (T) -> Unit,
        onStartLoad: () -> Unit = {},
        onCanceled: () -> Unit = {},
        onError: (e: Exception) -> Unit = {}
    ) {
        DownloadTask(
            onStartLoad,
            onGetDataFrom,
            onSuccess,
            onCanceled,
            onError
        ).execute(value)
    }

    private class DownloadTask<T>(
        val onPreExecute: () -> Unit,
        val doInBackground: (Int) -> T,
        val onPostExecute: (T) -> Unit,
        val onCanceled: () -> Unit,
        val onError: (e: Exception) -> Unit
    ) : AsyncTask<Int, Unit, DownloadTask<T>.Result?>() {

        inner class Result {
            var resultValue: T? = null
            var exception: Exception? = null

            constructor(resultValue: T) {
                this.resultValue = resultValue
            }

            constructor(exception: Exception) {
                this.exception = exception
            }
        }

        override fun onPreExecute() {
            onPreExecute.invoke()
        }

        override fun doInBackground(vararg value: Int?): Result? {
            var result: Result? = null
            if (!isCancelled) {
                result = try {
                    Result(doInBackground.invoke(value.first()!!))
                } catch (e: Exception) {
                    Result(e)
                }
            }
            return result
        }

        override fun onPostExecute(result: Result?) {
            result?.resultValue?.also {
                onPostExecute.invoke(result.resultValue!!)
            }
            result?.exception?.also {
                onError.invoke(it)
            }
        }

        override fun onCancelled() {
            onCanceled.invoke()
        }
    }
}