package com.example.bookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class BookAdapter(private var bookList: List<Book>, private val onItemClick: (Book) -> Unit):
    RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    // ViewHolder inner class to hold the views of each item in the RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.title_Text)
        val authorTextView: TextView = itemView.findViewById(R.id.authors_Text)
        val subtitleTextView: TextView = itemView.findViewById(R.id.subtitle_Text)
        val idTextView: TextView = itemView.findViewById(R.id.id_Text)
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.image_View)

        init {
            // Set click listener for the item view
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(bookList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false)
        return ViewHolder(view)
    }

    // Replace the contents of a ViewHolder (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]

        // Set data to the views of each item
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.authors
        holder.idTextView.text = book.id
        holder.subtitleTextView.text = book.subtitle

        // Load book cover image using Glide library
        Glide.with(holder.itemView.context)
            .load(book.image)
            .placeholder(R.drawable.b1) // Placeholder image while loading
            .error(R.drawable.b2) // Error image if loading fails
            .into(holder.bookCoverImageView) // ImageView to load the image into
    }

    // Return the size of the dataset
    override fun getItemCount(): Int {
        return bookList.size
    }

    // Update dataset with new data
    fun updateData(newBookList: List<Book>) {
        bookList = newBookList
        notifyDataSetChanged() // Notify RecyclerView that the dataset has changed
    }
}
