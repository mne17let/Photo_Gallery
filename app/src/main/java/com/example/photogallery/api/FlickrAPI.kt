package com.example.photogallery.api

import retrofit2.Call
import retrofit2.http.GET

interface FlickrAPI {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=5f86785a0117fbc297b6c4b643fa9f1a" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    fun fetchPhotosFromAPI() : Call<FlickrResponse>

    /*@GET("/")
    fun fetchContent(): Call<String>*/
}