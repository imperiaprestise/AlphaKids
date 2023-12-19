package com.example.alphakids.view.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.alphakids.R
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.util.createCustomDrawable
import com.example.alphakids.databinding.FragmentProfileBinding
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.help.HelpActivity
import com.example.alphakids.view.main.MainViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var mainViewModel: MainViewModel
    private lateinit var joinedTextView: TextView
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var emaillTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameTextView = view.findViewById(R.id.tv_name)
        emailTextView = view.findViewById(R.id.tv_email)
        joinedTextView = view.findViewById(R.id.tv_datejoined)
        logoutButton = view.findViewById(R.id.logoutButton)
        emaillTextView = view.findViewById(R.id.tv_detailemail)

        mainViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))
            .get(MainViewModel::class.java)

        profileViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))
            .get(ProfileViewModel::class.java)

        profileViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }

        logoutButton.setOnClickListener {
            mainViewModel.logout()
        }

        binding.bantuanButton.setOnClickListener {
            navigateToHelpActivity()
        }
    }

    private fun updateUI(user: UserModel) {

        usernameTextView.text = user.username
        emailTextView.text = user.email
        joinedTextView.text = user.dateJoined
        emaillTextView.text = user.email

        try {
            val drawable = createCustomDrawable(requireContext(), user.username.first().uppercaseChar())
            binding?.ivProfile?.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateToHelpActivity(){
        val intent = Intent(activity, HelpActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}