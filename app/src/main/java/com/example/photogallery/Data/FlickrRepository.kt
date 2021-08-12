package com.example.photogallery.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.Models.GalleryItem
import com.example.photogallery.api.FlickrAPI
import com.example.photogallery.api.FlickrResponse
import com.example.photogallery.api.PhotoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CacheRequest

private const val TAG = "TagForRepository"

class FlickrRepository {

    private lateinit var flickrAPI: FlickrAPI

    private val myLink = "https://api.flickr.com/"

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(myLink)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        flickrAPI = retrofit.create(FlickrAPI::class.java)
    }

    fun fetchPhotosFromRepository(): LiveData<List<GalleryItem>> {
        val flickrHomePageRequest: Call<FlickrResponse> = flickrAPI.fetchPhotosFromAPI()
        val getLiveDataFromServer = doWebRequest(flickrHomePageRequest)

        return getLiveDataFromServer
    }


    fun doWebRequest(request: Call<FlickrResponse>): MutableLiveData<List<GalleryItem>>{
        val dataFromServerLiveData: MutableLiveData<List<GalleryItem>> = MutableLiveData()

        val callback = object : Callback<FlickrResponse> {
            override fun onResponse(call: Call<FlickrResponse>, response: Response<FlickrResponse>) {
                Log.d(TAG, "Ответ получен: ${response.body()}")
                val flickrResponse: FlickrResponse? = response.body()
                val photoResponse: PhotoResponse? = flickrResponse?.photos

                var galleryItems: List<GalleryItem>? = photoResponse?.galleryItems
                if (galleryItems == null){
                    galleryItems = mutableListOf()
                }

                galleryItems = galleryItems.filterNot{
                    it.id.isBlank()
                }
                dataFromServerLiveData.value = galleryItems
            }

            override fun onFailure(call: Call<FlickrResponse>, t: Throwable) {
                Log.d(TAG, "Ошибка выполнения запроса. $t", t)
            }
        }

        request.enqueue(callback)

        return dataFromServerLiveData
    }
}