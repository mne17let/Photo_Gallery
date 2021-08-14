package com.example.photogallery.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickrAPI {

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=5f86785a0117fbc297b6c4b643fa9f1a" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    fun fetchPhotosFromAPI() : Call<FlickrResponse>

    @GET
    fun fetchURLBytes(@Url url: String): Call<ResponseBody>


    /*@GET("/")
    fun fetchContent(): Call<String>*/
}