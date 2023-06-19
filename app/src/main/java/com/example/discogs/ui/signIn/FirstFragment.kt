package com.example.discogs.ui.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.discogs.R
import com.example.discogs.di.DI
import com.example.discogs.network.api.Api
import com.example.pileofmusic.network.models.AuthUser
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirstFragment : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.sign_in_start_fragment, container, false)

        super.onCreate(savedInstanceState)
        val sharedPref = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()

        rootView.findViewById<View>(R.id.oauthVerifierBtn).setOnClickListener {
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch(Dispatchers.Main) {
                val authString = Api.getFirstOauthHeader()
                DI.discogsService.getOauthToken(authString)
                val oauthToken = DI.discogsService.getOauthToken(authString)
                    .replace("oauth_token=", "")
                AuthUser.setOauthToken(oauthToken)
                editor?.putString("oauthToken", oauthToken)
                editor?.apply()
            }
            val a = sharedPref?.getString("oauthToken", "no oauth token stored").orEmpty()
            Log.d("discogs debug", a)

            val url = "https://discogs.com/oauth/authorize?oauth_token=$a"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            Log.d("discogs debug", url)
            startActivity(i)
            Toast.makeText(
                context,
                "Please, sign in and copy the confirmation code",
                Toast.LENGTH_LONG
            ).show()
            navigateToAccessTokenFragment()
        }

        return rootView
    }

    private fun navigateToAccessTokenFragment() {
        val accessTokenFragment = AccessTokenFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.start_fragment_container_view, accessTokenFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}