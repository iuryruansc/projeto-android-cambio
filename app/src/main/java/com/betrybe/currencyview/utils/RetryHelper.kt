package com.betrybe.currencyview.utils

import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class RetryHelper {

    companion object {
        private const val MAX_RETRY_COUNT = 3
        private const val MAX_DELAY_MILLIS = 5000L

        //Function to retry the request
        suspend fun <T> retry(
            retryCount: Int = MAX_RETRY_COUNT,
            block: suspend (attempt: Int) -> T
        ): T {
            repeat(retryCount) { attempt ->
                try {
                    return block(attempt)
                } catch (e: HttpException) {
                    //Handle HTTP Exception
                } catch (e: IOException) {
                    if (attempt < retryCount - 1) {
                        val delayMillis = (DialogBoxUtils.pow(
                            2, (attempt + 1)
                                .coerceAtLeast(1)
                        ) * 1000L)
                                .coerceAtMost(MAX_DELAY_MILLIS)
                        delay(delayMillis)
                    } else {
                        throw e
                    }
                }
            }
            throw IllegalStateException("Reached max retry count")
        }
    }
}