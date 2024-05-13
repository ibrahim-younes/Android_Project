package com.example.bookapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Favourite_Book : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookMark_Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_book)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.Saved_RecycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list
        adapter = BookMark_Adapter(mutableListOf())
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        // Update the adapter's data
        adapter.notifyDataSetChanged()
    }
}