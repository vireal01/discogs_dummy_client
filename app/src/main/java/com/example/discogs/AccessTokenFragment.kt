package com.example.discogs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.discogs.di.DI
import com.example.discogs.network.api.Api
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccessTokenFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.access_token_fragment, container, false)


        super.onCreate(savedInstanceState)
        val sharedPref = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        val editText = rootView.findViewById<EditText>(R.id.oauthVerifier)

        rootView.findViewById<Button>(R.id.requestAccessTokenBtn).setOnClickListener {
            GlobalScope.launch {
                if (editText.text.toString() != "") {
                    val oauth_verifier = editText.text
                    println(editText.text)
                    val oauth_token = sharedPref?.getString("oauthToken", "no oauth token stored")
                        .orEmpty()
                    val authHeader = Api.getSecondOauthHeader(
                        oauth_token = oauth_token,
                        oauth_verifier = oauth_verifier.toString()
                    )
                    val accessToken = DI.dicogsService.getAccessToken(authHeader)

                    editor?.putString("accessToken", accessToken)
                    editor?.apply()

                } else {
                    // idk why, but it doesn't work
//                    Log.d("discogs debug", editText.text.toString())
//                    triggerToast()
                }
            }
        }
        return rootView
    }
}