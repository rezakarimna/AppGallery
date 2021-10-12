package com.appgallery.ui.view

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appgallery.R
import com.appgallery.data.ImageModel
import com.appgallery.databinding.ActivityPermissionBinding
import com.appgallery.ui.adapter.AdapterGallery
import com.appgallery.ui.viewmodel.GalleryViewModel
import kotlinx.coroutines.delay
import org.koin.android.scope.ScopeActivity

class permissionActivity : AppCompatActivity() {
    lateinit var binding: ActivityPermissionBinding
    val viewModel: GalleryViewModel by viewModels<GalleryViewModel>()
    private lateinit var externalStoragePhotoAdapter: AdapterGallery
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*checkPermissions()
        requestPermission()*/
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        requestPermission()

    }
    private fun checkPermissions() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(this, "Granted", Toast.LENGTH_LONG).show()
                    Log.i("TAGpermissionActivity", "checkPermissions: Granted")
                    //  setupExternalStorageRecyclerView()
                    observeImageModelList()
                } else {
                    Toast.makeText(this, "deny", Toast.LENGTH_LONG).show()
                    Log.i("TAGpermissionActivity", "checkPermissions: deny")
                }
            }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Granted2", Toast.LENGTH_LONG).show()
            Log.i("TAGpermissionActivity", "checkPermissions: Granted2")
            //  setupExternalStorageRecyclerView()
            observeImageModelList()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            Log.i("TAGpermissionActivity", "checkPermissions: launch")
            Toast.makeText(this, "launch", Toast.LENGTH_LONG).show()

        }
    }

    private fun observeImageModelList() {
        viewModel.setImageModelLiveData()
        Log.i("TAGpermissionActivity", "checkPermissions: viewModel")
        viewModel.getImageModelLiveData().observe(this, { ImageModels ->
            if (ImageModels.isEmpty()) {
                Toast.makeText(this, "عکسی یافت نشد", Toast.LENGTH_SHORT).show()
            } else {
                setupExternalStorageRecyclerView(ImageModels)
            }
        })
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
        val quray = contentResolver.query(collection, projection, null, null, sortOrder)
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

    private fun setupExternalStorageRecyclerView(ImageModels: List<ImageModel>) =
        binding.rvPublicPhotos.apply {
            //   val photos = loadPhotosFromStorage()
            Toast.makeText(this@permissionActivity, "${ImageModels.size}", Toast.LENGTH_SHORT)
                .show()
            externalStoragePhotoAdapter = AdapterGallery(ImageModels)
            adapter = externalStoragePhotoAdapter
            layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)

        }

}