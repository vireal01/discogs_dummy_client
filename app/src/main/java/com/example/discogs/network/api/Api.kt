package com.example.discogs.network.api

import com.example.pileofmusic.network.models.DiscogsAuth
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Headers

interface Api {



    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: some_user_agent"
    )
    @GET("oauth/request_token")
    suspend fun getOauthToken(@Header("Authorization") authHeader: String ): String

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        "User-Agent: some_user_agent"
    )
    @POST("oauth/access_token")
    suspend fun getAccessToken(@Header("Authorization") authHeader: String ): String


    @GET("/")
    suspend fun getSample(): DiscogsAuth

    @GET("oauth/identity")
    suspend fun getIdentity(): Call<DiscogsAuth>

    companion object {
        fun getFirstOauthHeader(
            oauth_consumer_key: String = "yTiqkuuoLzzcgDFFJcTs",
            oauth_signature: String = "yWgUGThaNSwQZAAKOeKQEYUfmlDCVOCt"
        ): String {
            val timestamp = System.currentTimeMillis()
            return "OAuth oauth_consumer_key=\"$oauth_consumer_key\", " +
                    "oauth_nonce=\"someString\", " +
                    "oauth_signature=\"$oauth_signature&\", " +
                    "oauth_signature_method=\"PLAINTEXT\", " +
                    "oauth_timestamp=\"$timestamp\""
        }

        fun getSecondOauthHeader(
            oauth_consumer_key: String = "yTiqkuuoLzzcgDFFJcTs",
            oauth_signature: String = "yWgUGThaNSwQZAAKOeKQEYUfmlDCVOCt",
            oauth_token: String,
            oauth_verifier: String
        ): String {
            val timestamp = System.currentTimeMillis()
            return "OAuth oauth_consumer_key=\"$oauth_consumer_key\", " +
                    "oauth_nonce=\"someString\", " +
                    "oauth_signature=\"$oauth_signature&\", " +
                    "oauth_signature_method=\"PLAINTEXT\", " +
                    "oauth_token=\"$oauth_token\", " +
                    "oauth_verifier=\"$oauth_verifier\", " +
                    "oauth_timestamp=\"$timestamp\""
        }
    }
}