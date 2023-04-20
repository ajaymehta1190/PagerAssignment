package com.sample.slidersampleila.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.slidersampleila.databinding.ItemManufacturerBinding
import com.sample.slidersampleila.model.Manufacturers

class ManufacturerPagerAdapter : RecyclerView.Adapter<PagerViewHolder>() {

    private var listManufacturer = arrayListOf<Manufacturers>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding =
            ItemManufacturerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return this.listManufacturer.size
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(listManufacturer[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(listManufacturers: List<Manufacturers>) {
        this.listManufacturer = listManufacturers as ArrayList<Manufacturers>
        notifyDataSetChanged()
    }
}

class PagerViewHolder(private val itemBinding: ItemManufacturerBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(item: Manufacturers) {
        itemBinding.itemIvManufacturer.setImageResource(item.manufacturerIcon)
    }
}