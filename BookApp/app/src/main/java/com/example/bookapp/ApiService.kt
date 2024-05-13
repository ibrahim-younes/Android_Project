package com.example.bookapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // fetching data from the "recent" endpoint using GET request
    @GET("recent")

    fun getTodoResponse():Call<Recent_Data_API>
}