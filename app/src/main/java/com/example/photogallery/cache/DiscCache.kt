package com.example.photogallery.cache

import android.os.AsyncTask
import okhttp3.internal.cache.DiskLruCache
import java.io.File

private const val DISK_CACHE_SIZE = 1024 * 1024 * 10 // 10MB

class DiskCache: AsyncTask<File, Unit, Unit>() {

    private val diskCacheLock = Any()
    private lateinit var discLruCache: DiskLruCache

    override fun doInBackground(vararg params: File?): Unit {
        synchronized(diskCacheLock){
            val cacheDirectory: File? = params[0]
        }
    }
}



/*
private DiskLruCache diskLruCache;
class InitDiskCacheTask extends AsyncTask<File, Void, Void> {
    @Override
    protected Void doInBackground(File... params) {
        synchronized (diskCacheLock) {
            File cacheDir = params[0];
            diskLruCache = DiskLruCache.open(cacheDir, DISK_CACHE_SIZE);
            diskCacheStarting = false; // Finished initialization
            diskCacheLock.notifyAll(); // Wake any waiting threads
        }
        return null;
    }
}

private final Object diskCacheLock = new Object();
 */