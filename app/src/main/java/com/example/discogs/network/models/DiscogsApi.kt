package com.example.discogs.network.models

import com.google.gson.annotations.SerializedName

data class DiscogsIdentity(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("resource_url")
    var resource_url: String? = null,

    @SerializedName("consumer_name")
    var consumer_name: String? = null
)