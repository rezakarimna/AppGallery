package com.appgallery.ui.viewmodel

import android.app.Application
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.AppLaunchChecker
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appgallery.data.ImageModel

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

   /* private val imageModelLiveData : MutableLiveData<List<ImageModel>> by lazy {
        MutableLiveData<List<ImageModel>> () .also {
            loadPhotosFromStorage()
        }
    }
*/
   private val imageModelLiveData : MutableLiveData<List<ImageModel>> = MutableLiveData<List<ImageModel>> ()

    fun setImageModelLiveData() {
        imageModelLiveData.value = loadPhotosFromStorage()
    }

    fun getImageModelLiveData() : LiveData<List<ImageModel>> {
        return imageModelLiveData
    }

    private fun loadPhotosFromStorage(): List<ImageModel> {
        val imageList = mutableListOf<ImageModel>()
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        val sortOrder = "${MediaStore.Images.Media.DISPLAY_NAME} ASC"
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.WIDTH
        )
        val quray = getApplication<Application>().contentResolver.query(collection, projection, null, null, sortOrder)
        quray?.use { cursor: Cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)
            val widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)

            while (cursor.moveToNext()) {
                val id: Long = cursor.getLong(idColumn)
                val name: String = cursor.getString(nameColumn)
                val height: Int = cursor.getInt(heightColumn)
                val width: Int = cursor.getInt(widthColumn)

                val contentUri: Uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageList += ImageModel(contentUri.toString())
            }
        }

        return imageList
    }
}
