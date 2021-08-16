package com.example.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface FlickrAPI {

    @GET("services/rest/?method=flickr.interestingness.getList")
    fun fetchPhotosFromAPI() : Call<FlickrResponse>

    @GET
    fun fetchURLBytes(@Url url: String): Call<ResponseBody>

    @GET("services/rest?method=flickr.photos.search")
    fun searchPhotos(@Query("text") searchString: String): Call<FlickrResponse>

    /*@GET("/")
    fun fetchContent(): Call<String>*/
}