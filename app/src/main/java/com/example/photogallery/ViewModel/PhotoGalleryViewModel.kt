package com.example.photogallery.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photogallery.Data.FlickrRepository
import com.example.photogallery.Models.GalleryItem

private const val TAG_FROM_VIEWMODEL = "ViewModel"
class PhotoGalleryViewModel: ViewModel() {

    val galleryItemsLiveData: LiveData<List<GalleryItem>>
    private val photoGalleryRepository: FlickrRepository

    init {
        photoGalleryRepository = FlickrRepository()
        galleryItemsLiveData = photoGalleryRepository.searchPhotos("bicycle")
        Log.d(TAG_FROM_VIEWMODEL, "Во вьюмодели получены данные: ${galleryItemsLiveData.toString()}")
    }
}