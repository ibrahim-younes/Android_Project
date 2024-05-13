package com.example.bookapp

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var subtitleTextView: TextView
    private lateinit var bookCoverImageView: ImageView
    private lateinit var authorTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var pages: TextView
    private lateinit var description: TextView
    private lateinit var download : Button
    private lateinit var favmark : ImageButton
    lateinit var adapter: BookMark_Adapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        titleTextView = findViewById(R.id.title_TextView)
        subtitleTextView = findViewById(R.id.subtitle_TextView)
        bookCoverImageView = findViewById(R.id.book_CoverImageView)
        authorTextView = findViewById(R.id.authors_TextView)
        urlTextView = findViewById(R.id.publisher_TextView)
        pages = findViewById(R.id.pages_textView)
        description = findViewById(R.id.description_TextView)
        download = findViewById(R.id.download_Button)
        favmark = findViewById(R.id.bookmark_Button)


        // Retrieve the clicked book from intent extras
        val clickedBook = intent.getSerializableExtra("clicked_book") as? Book

        adapter = BookMark_Adapter(mutableListOf())

        // If a book is clicked, display its details in the layout
        clickedBook?.let { book ->
            // Update UI elements with book details
            titleTextView.text = book.title
            subtitleTextView.text = book.subtitle
            authorTextView.text = book.authors
            urlTextView.text = book.url
            pages.text = book.pages // Access pages after initializing it

            // Load book cover image using Glide library
            Glide.with(this)
                .load(book.image)
                .placeholder(R.drawable.b1) // Placeholder image while loading
                .error(R.drawable.b2) // Error image if loading fails
                .into(bookCoverImageView)

            download.setOnClickListener {
                startDownload(book.url)
            }

            favmark.setOnClickListener {
                // Check if clickedBook is not null
                clickedBook?.let {
                    // Add the clicked book to the adapter
                    adapter.addBookToFavorites(it)
                    Toast.makeText(this,"Added To The BookMark",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    // Function to start the download process
    fun startDownload(downloadUrl: String) {
        // Create a download request with specified URL, title, and description
        val request = DownloadManager.Request(Uri.parse(downloadUrl))
            .setTitle("Book Download") // Title of the download notification
            .setDescription("Downloading") // Description of the download notification
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(
                this,
                Environment.DIRECTORY_DOWNLOADS, // Save the file in Downloads directory
                "book.pdf"
            )

        // Get the DownloadManager service and enqueue the download request
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}
