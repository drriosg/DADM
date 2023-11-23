package com.unal.edu.consumoapis

import android.provider.MediaStore.Images
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unal.edu.consumoapis.databinding.ItemDogBinding

class DogViewHolder(view:View):RecyclerView.ViewHolder(view) {

    private val binding = ItemDogBinding.bind(view)
    fun bind(images:String){
        Picasso.get().load(images).into(binding.ivDog)
        binding.ivDog
    }
}