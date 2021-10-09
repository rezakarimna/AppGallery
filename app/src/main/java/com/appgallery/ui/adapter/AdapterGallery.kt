package com.appgallery.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appgallery.R
import com.appgallery.data.ImageModel
import com.appgallery.databinding.ItemGalleryBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AdapterGallery(private val photos: List<ImageModel>) :
    RecyclerView.Adapter<AdapterGallery.ViewHolder>() {

    class ViewHolder(private val itemBinding: ItemGalleryBinding, private val context: Context) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bin(imageModel: ImageModel) {
            Glide.with(context)
                .load(imageModel.urlImage)
                .placeholder(R.color.black)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(itemBinding.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding =
            ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bin(photos[position])
    }

    override fun getItemCount() = photos.size


}