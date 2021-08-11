package com.example.photogallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photogallery.fragments.PhotoGalleryFragment

class PhotoGalleryMainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isEmptyFragmentContainer = savedInstanceState == null

        if (isEmptyFragmentContainer){
            supportFragmentManager
                .beginTransaction()
                .add(R.id.id_frame_fragment_container, PhotoGalleryFragment())
                .commit()
        }

    }
}