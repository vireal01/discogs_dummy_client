package com.example.pileofmusic.network.models

import com.google.gson.annotations.SerializedName


data class DiscogsAuth(
    @SerializedName("oauth_token")
    var oauth_token: String? = null,

    @SerializedName("hello")
    var hello: String? = null,
)


data class AuthUser(
    var oauth_token: String,
    var oauth_token_secret: String,
    var oauth_consumer_key: String,
    var oauth_signature: String,
    var access_token: String,
    var extended_oauth_signature: String,
    var userName: String
) {
    companion object {
        // AvatarUtils - usage of singleton
        @Volatile
        private var instance: AuthUser? = null

        fun getInstance(): AuthUser {
            if (instance == null) {
                instance = AuthUser(
                    "",
                    "",
                    "yTiqkuuoLzzcgDFFJcTs",
                    "yWgUGThaNSwQZAAKOeKQEYUfmlDCVOCt",
                    "",
                    "",
                    ""
                )
            }
            return instance!!
        }

        fun setOauthToken(oauthToken: String) {
            instance?.oauth_token = oauthToken
        }

        fun createExtendedOauthSignature(plusOauthSignature: String) {
            instance?.extended_oauth_signature = "${instance?.oauth_signature}&$plusOauthSignature"
        }

        fun setOauthTokenSecret(oauthTokenSecret: String) {
            instance?.oauth_token_secret = oauthTokenSecret
        }

        fun setOauthSignature(oauthSignature: String) {
            instance?.oauth_signature = oauthSignature
        }

        fun setOauthConsumerKey(oauthConsumerKey: String) {
            instance?.oauth_consumer_key = oauthConsumerKey
        }

        fun setAccessToken(accessToken: String) {
            instance?.access_token = accessToken
        }

        fun setUserName(userName: String) {
            instance?.userName = userName
        }
    }
}
