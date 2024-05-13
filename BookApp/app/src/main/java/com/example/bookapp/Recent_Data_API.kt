package com.example.bookapp

data class Recent_Data_API(
    val books: ArrayList<Book>,
    val status: String,
    val total: Int
)