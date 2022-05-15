package com.tanzir.weatherapp

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tanzir.weatherapp.adapter.ForecastAdapter
import com.tanzir.weatherapp.databinding.FragmentWeatherBinding
import com.tanzir.weatherapp.file.getLocation
import com.tanzir.weatherapp.viewmodels.WeatherVIewModel


class WeatherFragment : Fragment() {

    private lateinit var binding: FragmentWeatherBinding
    private val weatherVIewModel: WeatherVIewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weather_menu, menu)
        val searchView = menu.findItem(R.id.item_search).actionView as SearchView
        searchView.queryHint = "Search any city"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
               query?.let{
                   convertQueryToLatLng(query)
               }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    private fun convertQueryToLatLng(query: String) {

        val geocoder=Geocoder(requireContext())
       val addressList:List<Address> = geocoder.getFromLocationName(query,1)
        if(addressList.isNotEmpty())
        {
            val lat=addressList[0].latitude
            val lng = addressList[0].longitude
            val location = Location("").apply {
                latitude=lat
                longitude=lng
            }
            weatherVIewModel.setNewLocation(location)
        }else{
            Toast.makeText(requireContext(), "Invalid city", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
      if(item.itemId==R.id.item_lotion){
          getLocation(requireContext()){
              weatherVIewModel.setNewLocation(it)
          }
      }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val vIewModel: WeatherVIewModel by activityViewModels()
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val adapter = ForecastAdapter()

        binding.forecastRV.layoutManager =
            LinearLayoutManager(requireActivity()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        binding.forecastRV.adapter = adapter
        weatherVIewModel.locationLiveData.observe(viewLifecycleOwner) {
//            Toast.makeText(requireContext(), "${it.latitude},${it.longitude}", Toast.LENGTH_SHORT)
//                .show()
            vIewModel.fetchData()
        }
        weatherVIewModel.currentLiveData.observe(viewLifecycleOwner) {
            Log.d("WeatherFragment", "${it.main.temp}")
            binding.current = it
        }
        weatherVIewModel.forecastLiveData.observe(viewLifecycleOwner) {

            Log.d("", "${it.list.size}")
            adapter.submitList(it.list)
        }
        return binding.root
    }


}