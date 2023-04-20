package com.sample.slidersampleila.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.slidersampleila.databinding.ItemModelsBinding
import com.sample.slidersampleila.model.Models

class ModelsListAdapter : RecyclerView.Adapter<ModelsViewHolder>() {

    private var listModels = arrayListOf<Models>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelsViewHolder {
        val binding =
            ItemModelsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ModelsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.listModels.size
    }

    override fun onBindViewHolder(holder: ModelsViewHolder, position: Int) {
        holder.bind(listModels[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(listManufacturers: List<Models>) {
        this.listModels = listManufacturers as ArrayList<Models>
        notifyDataSetChanged()
    }
}

class ModelsViewHolder(private val itemBinding: ItemModelsBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: Models) {
        itemBinding.itemIvAvatar.setImageResource(item.modelIcon)
        itemBinding.itemTxtName.text = item.modelName
    }
}