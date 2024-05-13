package com.example.bookapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class BookMark_Adapter(var favoriteBooks: MutableList<Book>) :
    RecyclerView.Adapter<BookMark_Adapter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_Text)
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.image_View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("Adapter", "onCreateViewHolder: favoriteBooks size = ${favoriteBooks.size}")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_favourite_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder: favoriteBooks size = ${favoriteBooks.size}")
        val book = favoriteBooks[position]
        holder.titleTextView.text = book.title
        Glide.with(holder.itemView.context)
            .load(book.image)
            .placeholder(R.drawable.b1)
            .error(R.drawable.b2)
            .into(holder.bookCoverImageView)
    }

    override fun getItemCount(): Int {
        return favoriteBooks.size
    }

    fun addBookToFavorites(book: Book) {
        if (!favoriteBooks.contains(book)) {
            favoriteBooks.add(book)
            notifyItemInserted(favoriteBooks.size - 1)

            // Log the addition of the book
            Log.d("BookMark_Adapter", "Book added: $book")
            // Log the updated list of favorite books
            Log.d("BookMark_Adapter", "Favorite books: $favoriteBooks")
        }
    }
    @JvmName("getFavoriteBooksList")
    fun getFavoriteBooks(): MutableList<Book> {
        return favoriteBooks

    }

    // Function to update the adapter with a new list of favorite books
    fun updateFavoriteBooks(newFavoriteBooks: MutableList<Book>) {
        favoriteBooks = newFavoriteBooks
        notifyDataSetChanged()
    }

}