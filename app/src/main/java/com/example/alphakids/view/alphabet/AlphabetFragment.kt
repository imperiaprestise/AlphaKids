package com.example.alphakids.view.alphabet

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.alphakids.R
import com.example.alphakids.databinding.FragmentAlphabetBinding
import java.util.Locale

class AlphabetFragment : Fragment() {

    private var _binding: FragmentAlphabetBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlphabetBinding.inflate(inflater, container, false)
        return binding.root

    }


}