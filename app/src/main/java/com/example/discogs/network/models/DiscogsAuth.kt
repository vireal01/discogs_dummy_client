package com.example.pileofmusic.network.models
import com.google.gson.annotations.SerializedName


data class DiscogsAuth (
    @SerializedName("oauth_token")
    var oauth_token: String? = null,

    @SerializedName("hello")
    var hello: String? = null,
)
