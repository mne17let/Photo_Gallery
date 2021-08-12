package com.example.photogallery.RecyclerView

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.Models.GalleryItem

class PhotoGalleryRecyclerViewAdapter: RecyclerView.Adapter<PhotoHolder>() {

    private lateinit var listWithPhotoData: List<GalleryItem>

    fun setList(list: List<GalleryItem>){
        this.listWithPhotoData = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val viewForHolder = TextView(parent.context)
        return PhotoHolder(viewForHolder)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.bind(listWithPhotoData[position].title)
    }

    override fun getItemCount(): Int {
        return listWithPhotoData.size
    }
}