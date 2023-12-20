package com.example.alphakids.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alphakids.R
import com.example.alphakids.view.scan.ScanActivity
import com.example.alphakids.data.pref.UserModel
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.databinding.FragmentHomeBinding
import com.example.alphakids.view.adapter.ListBookAdapter
import com.example.alphakids.view.books.Books
import com.example.alphakids.view.login.LoginViewModel
import com.example.alphakids.view.main.MainViewModel
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var usernameTextView: TextView

    private val viewModel: MainViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        usernameTextView = view.findViewById(R.id.emailTextView)
        val userPreference = UserPreference.getInstance(requireContext().dataStore)

        lifecycle.coroutineScope.launch {
            userPreference.getSession().collect{ user ->
                updateUI(user)
            }
        }

        val bookNames = resources.getStringArray(R.array.data_book)
        val bookDescriptions = resources.getStringArray(R.array.data_deskripsi)
        val bookImages = resources.obtainTypedArray(R.array.data_gambar)

        val books = ArrayList<Books>()
        for (i in bookNames.indices) {
            val book = Books(bookNames[i], bookDescriptions[i], bookImages.getResourceId(i, -1))
            books.add(book)
        }

        val recyclerView = binding.rvBook
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        recyclerView.adapter = ListBookAdapter(books)

        bookImages.recycle()

        binding.buttonScan.setOnClickListener {
            navigateToScanActivity()
        }
    }

    private fun updateUI(user: UserModel){
        val greetingMessage = "Hi, ${user.username}"
        usernameTextView.text = greetingMessage    }

    private fun navigateToScanActivity() {
        val intent = Intent(activity, ScanActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}