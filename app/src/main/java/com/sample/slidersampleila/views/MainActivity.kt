package com.sample.slidersampleila.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.sample.slidersampleila.R
import com.sample.slidersampleila.adapters.ManufacturerPagerAdapter
import com.sample.slidersampleila.adapters.ModelsListAdapter
import com.sample.slidersampleila.core.ApiState
import com.sample.slidersampleila.databinding.ActivityMainBinding
import com.sample.slidersampleila.model.Manufacturers
import com.sample.slidersampleila.model.Models
import com.sample.slidersampleila.utils.setCarouselEffects
import com.sample.slidersampleila.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pagerAdapter: ManufacturerPagerAdapter
    private lateinit var modelsAdapter: ModelsListAdapter

    private var listManufacturers = arrayListOf<Manufacturers>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initPagerView()
        initViewPagerListener()
        initModelsView()
        initObserver()

        mainViewModel.getManufacturerList()
    }

    private fun initPagerView() {
        pagerAdapter = ManufacturerPagerAdapter()
        binding.pagerManufacturer.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = pagerAdapter
            TabLayoutMediator(
                binding.tabIndicator,
                binding.pagerManufacturer
            ) { _, _ ->
            }.attach()
            setCarouselEffects()
        }
    }

    private fun initViewPagerListener() {
        binding.pagerManufacturer.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bindModelsData(listManufacturers[position].listModels)
            }
        })
    }

    private fun initModelsView() {
        modelsAdapter = ModelsListAdapter()
        binding.recyclerModels.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = modelsAdapter
            isNestedScrollingEnabled = true
        }
    }

    private fun bindManufacturerData(listItems: List<Manufacturers>) {
        listManufacturers = listItems as ArrayList<Manufacturers>
        pagerAdapter.setItems(listManufacturers)
        bindModelsData(listManufacturers[0].listModels)
    }

    private fun bindModelsData(listItems: List<Models>) {
        modelsAdapter.setItems(listItems)
        setFilter(listItems)
        resetFilter()
    }

    private fun setFilter(listItems: List<Models>) {

        val allItems = arrayListOf<Models>()

        binding.edtSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                allItems.clear()
                if (listItems.isNotEmpty()) {
                    val data = listItems.filter { item: Models ->
                        item.modelName.lowercase().contains(query.lowercase())
                    }
                    allItems.addAll(data)
                }
                bindDataToView(allItems)
                return false
            }

        })

        //Display close button on search bar
        val closeButtonId = resources.getIdentifier("android:id/search_close_btn", null, null)
        val closeButtonImage = binding.edtSearchView.findViewById(closeButtonId) as ImageView
        closeButtonImage.setImageResource(R.drawable.ic_close)

        closeButtonImage.setOnClickListener {
            resetFilter()
            closeButtonImage.visibility = View.GONE
            modelsAdapter.setItems(listItems)
        }
    }

    fun bindDataToView(listItems: List<Models>) {
        if (listItems.isEmpty()) {
            binding.txtNoDataAvailable.visibility = View.VISIBLE
            binding.recyclerModels.visibility = View.GONE
        } else {
            binding.txtNoDataAvailable.visibility = View.GONE
            binding.recyclerModels.visibility = View.VISIBLE
            modelsAdapter.setItems(listItems)
        }
    }

    private fun resetFilter() {
        binding.txtNoDataAvailable.visibility = View.GONE
        binding.recyclerModels.visibility = View.VISIBLE
        binding.edtSearchView.setQuery("", true)
        binding.edtSearchView.clearFocus()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            mainViewModel._manufacturerDataListFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        Toast.makeText(this@MainActivity, "Loading Master Data", Toast.LENGTH_SHORT)
                            .show()
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Failure :: " + it.errMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is ApiState.Success -> {
                        Toast.makeText(this@MainActivity, "Data Retrieved", Toast.LENGTH_SHORT)
                            .show()
                        bindManufacturerData(it.T as ArrayList<Manufacturers>)
                    }
                    is ApiState.Empty -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Retrieved Empty Data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}