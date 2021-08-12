package com.example.photogallery.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photogallery.Data.FlickrRepository
import com.example.photogallery.Models.GalleryItem

class PhotoGalleryViewModel: ViewModel() {

    val galleryItemsLiveData: LiveData<List<GalleryItem>>
    private val photoGalleryRepository: FlickrRepository

    init {
        photoGalleryRepository = FlickrRepository()
        galleryItemsLiveData = photoGalleryRepository.fetchPhotosFromRepository()
    }
}