package com.example.alphakids.view.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.alphakids.R
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.util.createCustomDrawable
import com.example.alphakids.databinding.FragmentProfileBinding
import com.example.alphakids.view.ViewModelFactory
import com.example.alphakids.view.help.HelpActivity
import com.example.alphakids.view.main.MainViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var joinedTextView: TextView
    private lateinit var emaillTextView: TextView

    private val mainViewModel: MainViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private val profileViewModel : ProfileViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

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

        profileViewModel.getUserData().observe(viewLifecycleOwner) { user ->
            user?.let {
                updateUI(user)
            }
        }

        logoutButton.setOnClickListener {
            mainViewModel.logout()
        }

        binding.bantuanButton.setOnClickListener {
            navigateToHelpActivity()
        }

        binding.buttonLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun updateUI(user: UserModel) {

        usernameTextView.text = user.username ?: "Username not available"
        emailTextView.text = user.email ?: "Email not available"
        emaillTextView?.let {
            it.text = user.email
        }

        if (user.username.isNullOrEmpty()) {
            return
        }

        val drawable = createCustomDrawable(requireContext(), user.username.first().uppercaseChar(), 100)
        binding?.ivProfile?.let {
            it.setImageDrawable(drawable)
        }


        try {
            val formattedDate = formatDate(user.dateJoined)
            joinedTextView.text = formattedDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun formatDate(dateString: String): String {
        if (dateString.isNotEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
            val date = inputFormat.parse(dateString)
            return outputFormat.format(date)
        } else {
            return ""
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