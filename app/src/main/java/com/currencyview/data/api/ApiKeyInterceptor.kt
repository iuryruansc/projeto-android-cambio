package com.currencyview.data.api

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val apiKey = "Rv2Yju6pf3ztPN2JBtizmHezl4GX3suv"

        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader("apikey", apiKey)
            .build()
        return chain.proceed(newRequest)
    }


}