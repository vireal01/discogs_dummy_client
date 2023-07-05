package com.example.discogs.network

import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response

class CurlInterceptorWrapper : Interceptor {

    private val logger = object : Logger {
        override fun log(message: String) {
            Log.d("Request curl", message)
        }
    }
    private val curlInterceptor = CurlInterceptor(logger)

    override fun intercept(chain: Interceptor.Chain): Response {
        return curlInterceptor.intercept(chain)
    }
}
