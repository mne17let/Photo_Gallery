package com.example.photogallery.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photogallery.api.FlickrAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CacheRequest

private const val TAG = "TagForRepository"

class FlickrRepository {

    private lateinit var flickrAPI: FlickrAPI

    private val myLink = "https://www.flickr.com/"

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(myLink)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        flickrAPI = retrofit.create(FlickrAPI::class.java)
    }

    fun fetchContent(): LiveData<String> {
        val flickrHomePageRequest: Call<String> = flickrAPI.fetchContent()
        val getLiveDataFromServer = doWebRequest(flickrHomePageRequest)

        return getLiveDataFromServer
    }


    fun doWebRequest(request: Call<String>): MutableLiveData<String>{
        val dataFromServerLiveData: MutableLiveData<String> = MutableLiveData()

        val callback = object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "Ответ получен: ${response.body()}")
                dataFromServerLiveData.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "Ошибка выполнения запроса. ", t)
            }
        }

        request.enqueue(callback)

        return dataFromServerLiveData
    }
}