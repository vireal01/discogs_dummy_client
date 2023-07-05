package com.example.discogs.di

import com.example.discogs.network.CurlInterceptorWrapper
import com.example.discogs.network.HttpLoggingInterceptor
import com.example.discogs.network.api.Api
import com.example.discogs.network.api.OauthApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object DI {
    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(CurlInterceptorWrapper())
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.discogs.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val retrofitSerivalizable by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.discogs.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val discogsService by lazy {
        retrofit.create(OauthApi::class.java)
    }

    val serializableDiscogsService by lazy {
        retrofitSerivalizable.create(Api::class.java)
    }
}