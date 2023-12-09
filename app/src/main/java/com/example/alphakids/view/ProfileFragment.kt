package com.example.alphakids.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.alphakids.R
import kotlinx.coroutines.flow.collect
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.view.main.MainViewModel
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var mainViewModel: MainViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailTextView = view.findViewById(R.id.tv_email)

        logoutButton = view.findViewById(R.id.logoutButton)

        mainViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))
            .get(MainViewModel::class.java)

        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        // Collect the flow using a coroutine
        lifecycle.coroutineScope.launch {
            userPreference.getSession().collect { user ->
                // Perbarui elemen antarmuka pengguna dengan data pengguna
                updateUI(user)
            }
        }

        logoutButton.setOnClickListener {
            mainViewModel.logout()
            // Navigate to your login screen or perform any necessary actions
        }
    }

    private fun updateUI(user: UserModel) {
        // Perbarui TextView atau elemen antarmuka lainnya dengan data pengguna
        emailTextView.text = user.email
        // ...
    }

}