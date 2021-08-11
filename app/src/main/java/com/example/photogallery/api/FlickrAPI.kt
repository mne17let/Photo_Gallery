package com.example.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrAPI {

    @GET("/")
    fun fetchContent(): Call<String>
}