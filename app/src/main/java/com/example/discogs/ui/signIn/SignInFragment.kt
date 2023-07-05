package com.example.discogs.ui.signIn

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.discogs.R
import com.example.discogs.ui.DiscogsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

class SignInFragment : Fragment() {

    private val viewModel: DiscogsViewModel by viewModels()

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.sign_in_start_fragment, container, false)
        super.onCreate(savedInstanceState)

        rootView.findViewById<View>(R.id.oauthVerifierBtn).setOnClickListener {
            viewModel.onAuthVerifierClick(context)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(viewModel.getAuthorizationUrl())
            startActivity(intent)
            navigateToFragment(R.id.start_fragment_container_view)
        }

        // Token validation
        rootView.findViewById<View>(R.id.validateToken).setOnClickListener {
            val personalTokenFromEditText =
                rootView.findViewById<EditText>(R.id.personalTokenEditText)
            viewModel.onValidateTokenClick(context, personalTokenFromEditText)
        }

        rootView.findViewById<View>(R.id.loginWithPredefinedToken).setOnClickListener {
            viewModel.onLoginWithMockTokenClick(context)
        }

        return rootView
    }

    private fun navigateToFragment(fragmentRes: Int) {
        val accessTokenFragment = AccessTokenFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(fragmentRes, accessTokenFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}