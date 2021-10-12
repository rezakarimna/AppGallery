package com.appgallery.ui.viewmodel

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.AppLaunchChecker
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appgallery.base.BaseViewModel
import com.appgallery.data.ImageModel
import com.appgallery.data.repository.GalleryRepositoryImpl
import kotlinx.coroutines.launch

class GalleryViewModel() : BaseViewModel() {

    private val imageModelLiveData = MutableLiveData<List<ImageModel>>()
    private val repositoryImpl = GalleryRepositoryImpl()


    fun getImageModelLiveData(): LiveData<List<ImageModel>> = imageModelLiveData

    fun setImageModelLiveData(context: Context){
        viewModelScope.launch {
            imageModelLiveData.value = repositoryImpl.loadPhotosFromStorage(context)
        }
    }
}
