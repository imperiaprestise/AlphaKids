package com.example.alphakids.view.books

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Books (
    val nama: String,
    val deskripsi: String,
    val gambar: Int
) : Parcelable