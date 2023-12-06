package com.example.alphakids.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alphakids.DetailActivity
import com.example.alphakids.R
import com.example.alphakids.view.books.Books

class ListBookAdapter(private val listBook: ArrayList<Books>) : RecyclerView.Adapter<ListBookAdapter.ListViewHolder>(){
    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgGambar: ImageView = itemView.findViewById(R.id.bookImage)
        val btnBooks: Button = itemView.findViewById(R.id.readButton)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_books)
        val readButton : Button = itemView.findViewById(R.id.readButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder{
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val book = listBook[position]
        val (name, buttoon, photo) = listBook[position]
        holder.imgGambar.setImageResource(photo)
        holder.tvName.text = name
        holder.btnBooks.text = "Read"

        holder.readButton.setOnClickListener {
            val intentDetail = Intent(holder.btnBooks.context, DetailActivity::class.java)
            intentDetail.putExtra("key_book", listBook[holder.adapterPosition])
            holder.readButton.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listBook.size
}


