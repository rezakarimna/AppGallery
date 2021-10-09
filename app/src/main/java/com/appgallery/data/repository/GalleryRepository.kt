package com.appgallery.data.repository

import com.appgallery.data.ImageModel
import io.reactivex.Single

interface GalleryRepository {

    fun getImageAll() : Single<List<ImageModel>>
}