package com.appgallery.ui.view

import android.Manifest
import android.content.ContentUris
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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appgallery.data.ImageModel
import com.appgallery.databinding.ActivityPermissionBinding
import com.appgallery.ui.adapter.AdapterGallery
import com.appgallery.ui.viewmodel.GalleryViewModel

class PermissionActivity : AppCompatActivity() {

    lateinit var binding: ActivityPermissionBinding
    private val viewModel: GalleryViewModel by viewModels<GalleryViewModel>()
    private lateinit var externalStoragePhotoAdapter: AdapterGallery
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    private fun checkPermissions() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    observeImageModelList()
                } else {
                    Toast.makeText(this, "deny", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            observeImageModelList()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            Toast.makeText(this, "launch", Toast.LENGTH_LONG).show()
        }
    }

    private fun observeImageModelList() {
        viewModel.setImageModelLiveData(this)

        viewModel.getImageModelLiveData().observe(this, { ImageModels ->
            if (ImageModels.isEmpty()) {
                Toast.makeText(this, "عکسی یافت نشد", Toast.LENGTH_SHORT).show()
            } else {
                setupExternalStorageRecyclerView(ImageModels)
            }
        })
    }

    private fun setupExternalStorageRecyclerView(ImageModels: List<ImageModel>) =
        binding.rvPublicPhotos.apply {
            externalStoragePhotoAdapter = AdapterGallery(ImageModels)
            adapter = externalStoragePhotoAdapter
            layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
        }

}