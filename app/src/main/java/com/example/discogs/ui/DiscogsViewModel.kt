package com.example.discogs.ui

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.discogs.di.DI
import com.example.discogs.network.api.Api
import com.example.discogs.network.api.OauthApi
import com.example.discogs.network.models.DiscogsIdentity
import com.example.discogs.ui.main.MainActivity
import com.example.pileofmusic.network.models.AuthUser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.example.pileofmusic.network.models.DiscogsAuth
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

class DiscogsViewModel : ViewModel() {
    private val _data = MutableLiveData<String>()
    val data: LiveData<String> get() = _data
//    var username = mutableListOf<String>("")


    val sharedPref = Instrumentation().context?.applicationContext?.getSharedPreferences(
        "prefs",
        Context.MODE_PRIVATE
    )
    val editor = sharedPref?.edit()

    fun getSharedPrefs(): SharedPreferences? {
        return sharedPref
    }

    init {
        //
    }

    private val executionHandler = CoroutineExceptionHandler { _, e ->
        Log.d("Discogs view model", e.toString())
    }

    fun onAuthVerifierClick(context: Context?) {
        viewModelScope.launch(Dispatchers.IO + executionHandler) {
            val authString = OauthApi.getFirstOauthHeader()
            DI.discogsService.getOauthToken(authString)
            val oauthToken = DI.discogsService.getOauthToken(authString)
                .replace("oauth_token=", "")
            AuthUser.setOauthToken(oauthToken)
            editor?.putString("oauthToken", oauthToken)
            editor?.apply()
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context?.applicationContext,
                    "Please, sign in and copy the confirmation code",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun getAuthorizationUrl(): String {
        return "https://discogs.com/oauth/authorize?oauth_token=${AuthUser.getInstance().oauth_token}"
    }

    fun onValidateTokenClick(context: Context?, editText: EditText) {
        var message = ""
        val tokenToCheck = editText.text.toString()
        if (tokenToCheck == "") {
            return Toast.makeText(context, "The token field is empty", Toast.LENGTH_LONG).show()
        }
        var response: Response<DiscogsIdentity>
        // make view model as for sniffer and use viewModel scope instead of global/ dispatcher.io
        // view model example from android.developers.com
        viewModelScope.launch(Dispatchers.IO + executionHandler) {
            response = DI.serializableDiscogsService.getIdentity(
                Api.getAuthHeaderForPersonalAccessToken(
                    token = tokenToCheck
                )
            ).execute()
            message = if (response.code() == 200) {
                "Token is valid"
            } else {
                "Invalid token"
            }
        }.invokeOnCompletion {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onLoginWithMockTokenClick(context: Context?) {
        var response: Response<DiscogsIdentity>
        viewModelScope.launch(Dispatchers.IO + executionHandler) {
            response = DI.serializableDiscogsService
                .getIdentity(Api.getAuthHeaderForPersonalAccessToken())
                .execute()
            if (response.isSuccessful) {
                GlobalScope.launch {
                    AuthUser.setUserName(response.body()?.username.toString())
                } // IDK how to avoid GlobalScope usage here
                Log.d("123 username", response.body()?.username.toString())
                Log.d("123 username", AuthUser.getInstance().userName)
                val intent = Intent(context, MainActivity::class.java)
                if (context != null) {
                    startActivity(context, intent, Bundle())
                }
            }
        }
    }

    fun onHomeOpen() {
        viewModelScope.launch(Dispatchers.IO + executionHandler) {
            DI.serializableDiscogsService.getCollection(
                    Api.getAuthHeaderForPersonalAccessToken(),
                    AuthUser.getInstance().userName
                ).execute()
        }
    }
}