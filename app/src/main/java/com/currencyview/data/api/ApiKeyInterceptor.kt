package com.currencyview.data.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val apiKey = "fIxpV1gUWknElWzU7VJPKqsZPWwlFuTD"

        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader("apikey", apiKey)
            .build()
        return chain.proceed(newRequest)
    }


}
