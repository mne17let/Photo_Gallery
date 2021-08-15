package com.example.photogallery.cache

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.collection.LruCache
import java.lang.Exception

class ImageCache(maxSize: Int): LruCache<String, Bitmap>(maxSize) {

    fun getBitmapFromMemory(key: String): Bitmap?{
        return this.get(key)
    }

    fun setBitmapToMemory(key: String, bitmap: Bitmap){
        if (getBitmapFromMemory(key) == null){
            this.put(key, bitmap)
            Log.d("Кэширование", "$key добавлен в кэш")
        }
    }
}




