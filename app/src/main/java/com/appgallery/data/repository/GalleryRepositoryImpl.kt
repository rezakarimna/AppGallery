package com.appgallery.data.repository

import com.appgallery.data.ImageModel
import io.reactivex.Single

class GalleryRepositoryImpl : GalleryRepository {

    override fun getImageAll(): Single<List<ImageModel>> {
        TODO("Not yet implemented")
    }
}