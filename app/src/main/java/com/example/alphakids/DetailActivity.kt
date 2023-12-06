package com.example.alphakids

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.alphakids.view.books.Books

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val dataBook = if (Build.VERSION.SDK_INT >= 33){
            intent.getParcelableExtra<Books>("key_book", Books::class.java)
        } else{
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Books>("key_book")
        }

        val ivDetailGambar = findViewById<ImageView>(R.id.iv_detail_gambar)
        val tvDetailBook = findViewById<TextView>(R.id.tv_detail_books)
        val tvDetailDeskripsi = findViewById<TextView>(R.id.tv_description)

        if (dataBook != null){
            tvDetailBook.text = dataBook.nama
            tvDetailDeskripsi.text = dataBook.deskripsi
            ivDetailGambar.setImageResource(dataBook.gambar)
        }

    }
}