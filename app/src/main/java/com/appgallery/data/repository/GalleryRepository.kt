package com.appgallery.data.repository

import android.content.Context
import com.appgallery.data.ImageModel
import io.reactivex.Single

interface GalleryRepository {

    suspend fun loadPhotosFromStorage(context: Context): List<ImageModel>
}