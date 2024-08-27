package com.currencyview.common.util

fun <T> retry(
    times: Int,
    delayMillis: Long,
    block: () -> T
): T? {
    repeat(times) { attempt ->
        val result = block() // Try to execute the block
        if (result != null) {
            return result // If the block returns a non-null value, return it
        } else if (attempt < times - 1) {
            Thread.sleep(delayMillis) // Wait before retrying
        }
    }
    return null // Throw an exception if all retries fail
}