package com.example.bookapp
import java.io.Serializable


data class Book(
    val authors: String,
    val description: String,
    val download: String,
    val id: String,
    val image: String,
    val pages: String,
    val publisher: String,
    val subtitle: String,
    val title: String,
    val url: String,
): Serializable
