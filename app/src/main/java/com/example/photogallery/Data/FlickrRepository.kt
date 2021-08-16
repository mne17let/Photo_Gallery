package com.example.photogallery.Data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.Models.GalleryItem
import com.example.photogallery.Search.SearchInterceptor
import com.example.photogallery.api.FlickrAPI
import com.example.photogallery.api.FlickrResponse
import com.example.photogallery.api.PhotoResponse
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
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

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(SearchInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder().baseUrl(myLink)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        flickrAPI = retrofit.create(FlickrAPI::class.java)
    }

    fun fetchInterestringPhotosFromRepository(): LiveData<List<GalleryItem>> {
        val flickrHomePageRequest: Call<FlickrResponse> = flickrAPI.fetchPhotosFromAPI()
        val getLiveDataFromServer = doWebRequest(flickrHomePageRequest)

        return getLiveDataFromServer
    }

    fun searchPhotos(query: String): LiveData<List<GalleryItem>>{
        val searchRequest: Call<FlickrResponse> = flickrAPI.searchPhotos(query)
        val getLiveDataFromServer = doWebRequest(searchRequest)

        return getLiveDataFromServer
    }

    /*
     fun searchPhotos(query: String): LiveData<List<GalleryItem>> {        return fetchPhotoMetadata(flickrApi.searchPhotos(query))    }
     */


    private fun doWebRequest(request: Call<FlickrResponse>): MutableLiveData<List<GalleryItem>>{
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

    @WorkerThread
    fun fetchImageBitmap(url: String): Bitmap? {
        val response: Response<ResponseBody> = flickrAPI.fetchURLBytes(url).execute()
        val stream = response.body()?.byteStream()
        val bitmap = BitmapFactory.decodeStream(stream)

        stream?.close()

        Log.d(TAG, "Decoded bitmap=$bitmap from Response=$response")

        return bitmap
    }
}