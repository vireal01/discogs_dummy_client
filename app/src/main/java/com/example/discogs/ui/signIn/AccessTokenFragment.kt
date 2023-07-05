package com.example.discogs.ui.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.discogs.R
import com.example.discogs.di.DI
import com.example.discogs.network.api.OauthApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccessTokenFragment : Fragment() {
    @OptIn(DelicateCoroutinesApi::class)
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
                    val oauthVerifier = editText.text
                    OauthApi.updateRefreshToken()

                    val authHeader = OauthApi.getSecondOauthHeader(
                        oauth_verifier = oauthVerifier.toString()
                    )
                    val accessToken = DI.discogsService.getAccessToken(authHeader)
                    editor?.putString("accessToken", accessToken)
                    editor?.apply()

                }
            }
        }
        return rootView
    }
}