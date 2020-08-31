package ru.test.app.utils

import android.os.AsyncTask


object TaskRunner {
    fun <T> run(
        onStartLoad: () -> Unit = {},
        onGetData: () -> T,
        onSuccess: (T) -> Unit,
        onCanceled: () -> Unit = {},
        onError: () -> Unit = {},
        onError2: (e: Exception) -> Unit = {}
    ) {
        DownloadTask(
            onStartLoad,
            onGetData,
            onSuccess,
            onCanceled,
            onError,
            onError2
        ).execute()
    }

    class DownloadTask<T>(
        val onPreExecute: () -> Unit,
        val doInBackground: () -> T,
        val onPostExecute: (T) -> Unit,
        val onCanceled: () -> Unit,
        val onError: () -> Unit,
        val onError2: (e: Exception) -> Unit,
    ) : AsyncTask<Unit, Unit, DownloadTask<T>.Result?>() {

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

        override fun doInBackground(vararg p0: Unit?): Result? {
            var result: Result? = null
            if (!isCancelled) {
                result = try {
                    Result(doInBackground.invoke())
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
                onError.invoke()
                onError2.invoke(it)
            }
        }

        override fun onCancelled() {
            onCanceled.invoke()
        }
    }
}