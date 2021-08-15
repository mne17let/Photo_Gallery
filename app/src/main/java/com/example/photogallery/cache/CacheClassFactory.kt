package com.example.photogallery.cache

import android.app.ActivityManager
import android.content.Context
import androidx.fragment.app.Fragment

class CacheClassFactory(private var fragment: Fragment) {

    fun createImageCacheClass(): ImageCache{
        val activityManager: ActivityManager = fragment.requireContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryClass = activityManager.memoryClass
        val maxSizeForCacheClass = 1024 * 1024 * memoryClass / 8
        val result = ImageCache(maxSizeForCacheClass)
        return result
    }
}


/*
int memClass = mainView.getMemoryClassFromActivity();
int cacheSize = 1024 * 1024 * memClass / 8;
IconCache iconCache = new IconCache(cacheSize);
*/