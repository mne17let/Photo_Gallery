package com.example.photogallery.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.photogallery.Data.FlickrRepository
import com.example.photogallery.Models.GalleryItem

private const val TAG_FROM_VIEWMODEL = "ViewModel"
class PhotoGalleryViewModel: ViewModel() {

    var galleryItemsLiveData: LiveData<List<GalleryItem>>
    private val photoGalleryRepository: FlickrRepository = FlickrRepository()

    private val mutableSearchString: MutableLiveData<String> = MutableLiveData()

    init {
        mutableSearchString.value = "Поиск"

        galleryItemsLiveData = Transformations.switchMap(mutableSearchString){
                textString -> photoGalleryRepository.searchPhotos(textString)
        }

        Log.d(TAG_FROM_VIEWMODEL, "Во вьюмодели получены данные: ${galleryItemsLiveData.toString()}")
    }

    fun downLoadImage(query: String = ""){
        mutableSearchString.value = query
    }

    fun myFunForTransformations(text: String): LiveData<List<GalleryItem>>{
        return photoGalleryRepository.searchPhotos(text)
    }
}