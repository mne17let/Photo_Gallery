package com.example.photogallery.fragments

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.Data.FlickrRepository
import com.example.photogallery.Data.ThumbnailDownloader
import com.example.photogallery.Models.GalleryItem
import com.example.photogallery.R
import com.example.photogallery.RecyclerView.PhotoGalleryRecyclerViewAdapter
import com.example.photogallery.RecyclerView.PhotoHolder
import com.example.photogallery.ViewModel.PhotoGalleryViewModel
import com.example.photogallery.api.FlickrAPI
import com.example.photogallery.cache.CacheClassFactory
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
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader.viewLifecycleObserver)
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
                galleryItems -> Log.d(TAG, "???????????????? ???????????? ???? ??????????????????: $galleryItems")
                recyclerViewWithPhoto.adapter = PhotoGalleryRecyclerViewAdapter().also {
                    it.setList(galleryItems)
                    it.setThumnailDownLoader(thumbnailDownloader)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewModel()
        setHasOptionsMenu(true)
        /*val flickrData: LiveData<List<GalleryItem>> = FlickrRepository().fetchInterestringPhotosFromRepository()

        flickrData.observe(this,
            Observer { newString -> Log.d(TAG, "?????????? ??????????????: $newString")
        })*/

        retainInstance = true

        val responseHandler = Handler()
        /*val myFunForAnotherThread = {photoHolder: PhotoHolder, bitmap: Bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bind(drawable)
        }*/


        thumbnailDownloader = ThumbnailDownloader(responseHandler){
            photoHolder: PhotoHolder, bitmap: Bitmap ->
                val drawable = BitmapDrawable(resources, bitmap)
                photoHolder.bind(drawable)
        }

        thumbnailDownloader.setImageCacheInstance(CacheClassFactory(this).createImageCacheClass())

        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    fun getViewModel(){
        photoGalleryViewModel = ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    override fun onDestroyView(){
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(thumbnailDownloader.viewLifecycleObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_with_search, menu)

        val menuItemSearch: MenuItem = menu.findItem(R.id.id_menu_item_search_view)
        val searchView: SearchView = menuItemSearch.actionView as SearchView

        searchView.setOnQueryTextListener(MyOnQueryTextListener())
    }

    inner class MyOnQueryTextListener: SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(queryText: String): Boolean {
            Log.d(TAG, queryText)
            photoGalleryViewModel.downLoadImage(queryText)
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            Log.d(TAG, newText)
            return false
        }

    }
}

/*
 val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
 val searchView = searchItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(queryText: String): Boolean {
            Log.d(TAG, "QueryTextSubmit: $queryText")
            photoGalleryViewModel.fetchPhotos(queryText)
            return true
            }
                override fun onQueryTextChange(queryText: String): Boolean {
                Log.d(TAG, "QueryTextChange: $queryText")
                return false
                }
                })
                }
 */