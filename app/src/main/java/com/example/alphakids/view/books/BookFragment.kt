package com.example.alphakids.view.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alphakids.R
import com.example.alphakids.databinding.FragmentBookBinding
import com.example.alphakids.view.adapter.ListBookAdapter


class BookFragment : Fragment() {

    private var _binding: FragmentBookBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bookNames = resources.getStringArray(R.array.data_book)
        val bookDescriptions = resources.getStringArray(R.array.data_deskripsi)
        val bookImages = resources.obtainTypedArray(R.array.data_gambar)

        // Inisialisasi list buku
        val books = ArrayList<Books>()
        for (i in bookNames.indices) {
            val book = Books(bookNames[i], bookDescriptions[i], bookImages.getResourceId(i, -1))
            books.add(book)
        }

        // Inisialisasi RecyclerView dan set adapter
        val recyclerView = binding.rvBook
        recyclerView.layoutManager = GridLayoutManager(activity, 2)

        // Set adapter ke RecyclerView
        recyclerView.adapter = ListBookAdapter(books)

        // Recycle the typed array
        bookImages.recycle()

    }

}