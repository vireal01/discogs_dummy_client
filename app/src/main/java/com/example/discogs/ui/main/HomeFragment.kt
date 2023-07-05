package com.example.discogs.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.discogs.R
import com.example.discogs.ui.DiscogsViewModel

class HomeFragment : Fragment() {

    private val viewModel: DiscogsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)
        super.onCreate(savedInstanceState)
        viewModel.onHomeOpen()
        return rootView
    }
}