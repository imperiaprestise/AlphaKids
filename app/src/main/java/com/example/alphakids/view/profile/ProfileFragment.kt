package com.example.alphakids.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.navigation.Navigation
import kotlinx.coroutines.flow.collect
import com.example.alphakids.R
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.databinding.FragmentProfileBinding
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.main.MainViewModel
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var mainViewModel: MainViewModel
    private lateinit var joinedTextView: TextView
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailTextView = view.findViewById(R.id.tv_email)
        joinedTextView = view.findViewById(R.id.tv_datejoined)
        logoutButton = view.findViewById(R.id.logoutButton)

        mainViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))
            .get(MainViewModel::class.java)

        profileViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))
            .get(ProfileViewModel::class.java)

//        val userPreference = UserPreference.getInstance(requireContext().dataStore)
//
//        // Collect the flow using a coroutine
//        lifecycle.coroutineScope.launch {
//            userPreference.getSession().collect { user ->
//                // Perbarui elemen antarmuka pengguna dengan data pengguna
//                updateUI(user)
//            }
//        }

        profileViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }

        logoutButton.setOnClickListener {
            mainViewModel.logout()
        }

        setupView()
    }

    private fun updateUI(user: UserModel) {
        emailTextView.text = user.username
        joinedTextView.text = user.dateJoined

    }

    private fun setupView(){

        binding?.itemBantuan?.apply {
            mtrlListItemIcon.setImageResource(R.drawable.baseline_help_24)
            mtrlListItemText.text = resources.getText(R.string.help)
            mtrlListItemSecondaryText.visibility = View.GONE
        }
    }



}