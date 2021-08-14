package com.example.photogallery.RecyclerView

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photogallery.R

class PhotoHolder(private val viewForHolder: View): RecyclerView.ViewHolder(viewForHolder) {

    private lateinit var imageViewWithPhotoInfo: ImageView

    fun bind(drawable: Drawable){
        imageViewWithPhotoInfo = itemView.findViewById(R.id.id_image_view_gallery_item)
        imageViewWithPhotoInfo.setImageDrawable(drawable)

        Log.d("Адаптер", "Drawable ${drawable.toString()}")
    }

    //var bindDrawable: (Drawable) -> Unit = imageViewWithPhotoInfo::setImageDrawable

}