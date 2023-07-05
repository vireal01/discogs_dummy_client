package com.example.discogs.network.api

import android.util.Log
import com.example.discogs.di.DI
import com.example.pileofmusic.network.models.AuthUser
import com.example.pileofmusic.network.models.DiscogsAuth
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Headers

interface OauthApi {
    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        userAgent
    )
    @GET("oauth/request_token")
    suspend fun getOauthToken(@Header("Authorization") authHeader: String ): String

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        userAgent
    )
    @POST("oauth/access_token")
    suspend fun getAccessToken(@Header("Authorization") authHeader: String ): String

    @Headers(
        "Content-Type: application/x-www-form-urlencoded",
        userAgent
    )
    @GET("/oauth/request_token")
    suspend fun getRequestToken(@Header("Authorization") authHeader: String ): String

    @GET("/")
    suspend fun getSample(): DiscogsAuth

    @GET("oauth/identity")
    suspend fun getIdentity(): Call<DiscogsAuth>

    @GET("oauth/identity")
    suspend fun authorize(@Header("Authorization") authHeader: String): String
    // success resp example
    // {"id": 16651987, "username": "vireal", "resource_url": "https://api.discogs.com/users/vireal", "consumer_name": "vireal"}


    companion object {
        const val userAgent = "User-Agent: Dalvik/2.1.0 (Linux; U; Android 13; sdk_gphone64_arm64 Build/TE1A.220922.012)/42000000"

        fun getFirstOauthHeader(): String {
            val user = AuthUser.getInstance()
            val timestamp = System.currentTimeMillis()
            return "OAuth oauth_consumer_key=\"${user.oauth_consumer_key}\", " +
                    "oauth_nonce=\"someString\", " +
                    "oauth_signature=\"${user.oauth_signature}&\", " +
                    "oauth_signature_method=\"PLAINTEXT\", " +
                    "oauth_timestamp=\"$timestamp\""
        }

        fun getSecondOauthHeader(
            oauth_verifier: String
        ): String {
            val user = AuthUser.getInstance()
            val timestamp = System.currentTimeMillis()
            return "OAuth oauth_consumer_key=\"${user.oauth_consumer_key}\", " +
                    "oauth_nonce=\"someString\", " +
                    "oauth_token=\"${user.oauth_token}\", " +
                    "oauth_signature=\"${user.extended_oauth_signature}\", " +
                    "oauth_signature_method=\"PLAINTEXT\", " +
                    "oauth_timestamp=\"$timestamp\", " +
                    "oauth_verifier=\"$oauth_verifier\""
        }

        suspend fun updateRefreshToken() {
            val oauthTokenAndSecret = DI.discogsService.getRequestToken(getFirstOauthHeader())
                .replace("oauth_token=", "")
            Log.d("discogs debug", oauthTokenAndSecret)
            val oauthSignature: String = "(oauth_token_secret=)*=(.*)"
                .toRegex().find(oauthTokenAndSecret)!!.value
                .replace("=", "")
            AuthUser.createExtendedOauthSignature(oauthSignature)

            val newOauthToken: String = "^.*(&oauth_token_secret=)".toRegex()
                .find(oauthTokenAndSecret)!!.value
            AuthUser.setOauthToken(newOauthToken.replace("&oauth_token_secret=", ""))
        }
    }
}
