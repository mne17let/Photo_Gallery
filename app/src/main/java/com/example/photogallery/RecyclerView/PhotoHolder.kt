package com.example.photogallery.RecyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhotoHolder(private val viewForHolder: View): RecyclerView.ViewHolder(viewForHolder) {

    private var textViewWithPhotoInfo: TextView = viewForHolder as TextView

    fun bind(photoTitle: String){
        textViewWithPhotoInfo.text = photoTitle
    }

}