package com.example.photogallery.RecyclerView

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.Data.ThumbnailDownloader
import com.example.photogallery.Models.GalleryItem
import com.example.photogallery.R

class PhotoGalleryRecyclerViewAdapter: RecyclerView.Adapter<PhotoHolder>() {

    private lateinit var listWithPhotoData: List<GalleryItem>
    private lateinit var contextFromParent: Context
    private lateinit var thumbnailDownloader: ThumbnailDownloader<PhotoHolder>

    fun setList(list: List<GalleryItem>){
        this.listWithPhotoData = list
    }

    fun setThumnailDownLoader(td: ThumbnailDownloader<PhotoHolder>){
        thumbnailDownloader = td
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        contextFromParent = parent.context
        val viewForHolder = LayoutInflater.from(contextFromParent).inflate(R.layout.item_photo_list, parent, false)
        return PhotoHolder(viewForHolder)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val placeholderDrawable: Drawable = ContextCompat.getDrawable(contextFromParent, R.drawable.android) ?: ColorDrawable()
        holder.bind(placeholderDrawable)

        thumbnailDownloader.downloadImage(holder, listWithPhotoData[position].url)
    }

    override fun getItemCount(): Int {
        return listWithPhotoData.size
    }
}