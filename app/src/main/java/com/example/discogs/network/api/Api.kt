package com.example.discogs.network.api

import com.example.discogs.network.models.DiscogsIdentity
import com.example.pileofmusic.network.models.AuthUser
import com.example.pileofmusic.network.models.DiscogsAuth
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface Api {
    @GET("/")
    suspend fun getSample(): DiscogsAuth

    @GET("users/{username}/collection/folders/1")
    fun getCollection(
        @Header("Authorization") authHeader: String,
        @Path("username") id: String
    ): Call<DiscogsIdentity>

    @GET("oauth/identity")
    fun getIdentity(@Header("Authorization") authHeader: String): Call<DiscogsIdentity>
    // success resp example
    // {"id": 16651987, "username": "vireal", "resource_url": "https://api.discogs.com/users/vireal", "consumer_name": "vireal"}



    companion object {
        const val userAgent = "User-Agent: Dalvik/2.1.0 (Linux; U; Android 13; sdk_gphone64_arm64 Build/TE1A.220922.012)/42000000"
        const val mockToken: String = "fJYlsWiCvRlNMoHRzRkwUPesENTYXadeinGvTYmx"

        fun getAuthHeaderForPersonalAccessToken(token: String = mockToken): String {
            return "Discogs token=$token"
        }
    }
}
