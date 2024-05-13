package com.example.bookapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookapp.databinding.ActivityUserdashboardBinding
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable


class DashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityUserdashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var originalBookList: List<Book>
    private val favoriteBooks: List<Book> = mutableListOf() // Initialize list of favorite books


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // Logout button click listener
        binding.logoutButton.setOnClickListener {
            firebaseAuth.signOut() // Sign out the current user
            startActivity(Intent(this, MainActivity::class.java)) // Redirect to main activity
            finish()
        }

        recyclerView = binding.bookRecycleView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list and set item click listener
        adapter = BookAdapter(emptyList()) { clickedBook ->
            // Handle click action here, by opening details layout with clickedBook data
            val intent = Intent(this, BookDetailsActivity::class.java)
            intent.putExtra("clicked_book", clickedBook as Serializable)
            startActivity(intent)
        }

        if (favoriteBooks.isEmpty()) {
            // Send a notification to encourage the user to add books to favorites
            sendEmptyFavoritesNotification()
        }

        recyclerView.adapter = adapter


        //.............................................................Fetch book data from API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.dbooks.org/api/") // Base URL for the API
            .addConverterFactory(GsonConverterFactory.create()) // Converter for JSON response
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getTodoResponse().enqueue(object : Callback<Recent_Data_API> {
            override fun onResponse(
                call: Call<Recent_Data_API>,
                response: Response<Recent_Data_API>
            ) {
                val body = response.body()
                if (body != null) {
                    // Store original book list
                    originalBookList = body.books
                    // Update adapter data with API response
                    adapter.updateData(originalBookList)
                }
            }

            override fun onFailure(call: Call<Recent_Data_API>, t: Throwable) {
                // Log error message if API call fails
                Log.d("Checkresponse", t.toString())
            }
        })

        //..................................................................search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { search(it) }
                return true
            }
        })

        binding.favButton.setOnClickListener {
            startActivity(Intent(this,Favourite_Book::class.java))
        }
    }

    // Check if user is logged in and display appropriate message
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            binding.subTittleTv.text = "Not Logged In (Guest)"
        } else {
            val email = firebaseUser.email
            binding.subTittleTv.text = email
        }
    }


    // Function to filter the book list based on the search query
    private fun search(query: String) {
        val filteredList = if (query.isEmpty()) {
            // If the query is empty, show all books
            originalBookList
        } else {
            // Filter books based on title, authors, or subtitle
            originalBookList.filter { book ->
                book.title.contains(query, ignoreCase = true) ||
                        book.authors.contains(query, ignoreCase = true) ||
                        book.subtitle.contains(query, ignoreCase = true)
            }
        }
        adapter.updateData(filteredList)
    }
    private fun sendEmptyFavoritesNotification() {
        // Create a notification channel if running on Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "empty_favorites_channel"
            val channelName = "Empty Favorites Channel"
            val description = "Channel for notifying the user when favorites list is empty"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(this, "empty_favorites_channel")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Your favorites list is empty")
            .setContentText("Start adding books to your favorites to keep track of them!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            notify(0, notificationBuilder.build())
        }
    }
}