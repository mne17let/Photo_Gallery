package com.example.photogallery.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.Data.FlickrRepository
import com.example.photogallery.Models.GalleryItem
import com.example.photogallery.R
import com.example.photogallery.RecyclerView.PhotoGalleryRecyclerViewAdapter
import com.example.photogallery.ViewModel.PhotoGalleryViewModel
import com.example.photogallery.api.FlickrAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CacheRequest

private const val TAG = "PhotoGalleryFragment"

class PhotoGalleryFragment: Fragment() {
    private lateinit var recyclerViewWithPhoto: RecyclerView
    private lateinit var photoGalleryViewModel: PhotoGalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewWithPhoto = view.findViewById(R.id.id_recyclerview_photo_gallery_fragment)
        recyclerViewWithPhoto.layoutManager = GridLayoutManager(context, 3)
        setObserver()
    }

    fun setObserver(){
        photoGalleryViewModel.galleryItemsLiveData.observe(
            viewLifecycleOwner,
            Observer {
                galleryItems -> Log.d(TAG, "Получены данные из вьюмодели: $galleryItems")
                recyclerViewWithPhoto.adapter = PhotoGalleryRecyclerViewAdapter().also {
                    it.setList(galleryItems)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel()
        /*val flickrData: LiveData<List<GalleryItem>> = FlickrRepository().fetchPhotosFromRepository()

        flickrData.observe(this,
            Observer { newString -> Log.d(TAG, "Ответ получен: $newString")
        })*/

    }

    fun getViewModel(){
        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }
}