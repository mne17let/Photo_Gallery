package com.example.photogallery.Data

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.LruCache
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.photogallery.cache.ImageCache
import java.util.concurrent.ConcurrentHashMap

private const val NAME_OF_HANDLER_THREAD = "ImageDownloader"
private const val MESSAGE_DOWNLOAD = 0

class ThumbnailDownloader<T>(private val responseHandler: Handler,
                                private val onThumbnailDownloaded:
                                 (T, Bitmap) -> Unit )
    : HandlerThread(NAME_OF_HANDLER_THREAD) {

    private lateinit var imageCache: ImageCache


    private lateinit var requestHandler: Handler
    private val requestMap = ConcurrentHashMap<T, String>()
    private val flickrRepository = FlickrRepository()

    private var isQuit = false

    fun setImageCacheInstance(ic: ImageCache){
        imageCache = ic
    }

    override fun quit(): Boolean {
        isQuit = true
        return super.quit()
    }

    fun downloadImage(target: T, url: String){
        Log.d(NAME_OF_HANDLER_THREAD, "Получена ссылка в методе downLoadImage: $url")

        requestMap[target] = url
        requestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
    }



    @Suppress("UNCHECKED_CAST")
    @SuppressLint("HandlerLeak")
    override fun onLooperPrepared() {
        requestHandler = object : Handler(){
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD){
                    val target = msg.obj as T
                    Log.i(NAME_OF_HANDLER_THREAD, "Got a request for URL в методе onLooperPrepared: ${requestMap[target]}")
                    handleRequest(target)
                }
            }
        }
    }

    private fun handleRequest(target: T) {
        val url = requestMap[target] ?: return

        val bitmap: Bitmap
        if(imageCache.getBitmapFromMemory(url) == null){
            bitmap = flickrRepository.fetchImageBitmap(url) ?: return
            imageCache.setBitmapToMemory(url, bitmap)
        } else{
            bitmap = imageCache.getBitmapFromMemory(url) ?: return
        }

        responseHandler.post(
            Runnable {
                    if (requestMap[target] != url || isQuit == true){
                        return@Runnable
                    }
                    requestMap.remove(target)
                    onThumbnailDownloaded(target, bitmap)
            }
        )
    }


    /*
    private void handleRequest(final T target) {
    final String packageName = mRequestMap.get(target);
    final Drawable icon;

    if (packageName != null) {
        try {
            if (iconCache.getBitmapFromMemory(packageName) == null) {
                icon = packageManager.getApplicationIcon(packageName);
                iconCache.setBitmapToMemory(packageName, icon);
            } else {
                icon = iconCache.getBitmapFromMemory(packageName);
            }
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRequestMap.get(target) == null || !mRequestMap.get(target).equals(packageName)) {
                        return;
                    }
                    mRequestMap.remove(target);
                    mGetIconThreadListener.onIconDownloaded(target, icon);
                }
            });
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
     */

    val fragmentLifecycleObserver: LifecycleObserver = object : LifecycleObserver{
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun beginWork(){
            Log.d(NAME_OF_HANDLER_THREAD, "Starting background thread")
            start()
            looper
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun stopWork(){
            Log.d(NAME_OF_HANDLER_THREAD, "Destroying background thread")
            quit()
        }
    }


    val viewLifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun clearQueue() {
            Log.d(NAME_OF_HANDLER_THREAD, "Clearing all requests from queue")
            requestHandler.removeMessages(MESSAGE_DOWNLOAD)
            requestMap.clear()
        }
    }

}