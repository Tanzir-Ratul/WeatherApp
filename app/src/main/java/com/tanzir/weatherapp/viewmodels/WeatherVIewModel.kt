package com.tanzir.weatherapp.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tanzir.weatherapp.models.CurrentModel
import com.tanzir.weatherapp.models.ForecastModel
import com.tanzir.weatherapp.repos.WeatherRepository
import kotlinx.coroutines.launch

class WeatherVIewModel:ViewModel() {
    val repository = WeatherRepository()
    val locationLiveData :MutableLiveData<Location> = MutableLiveData()

    val currentLiveData:MutableLiveData<CurrentModel> = MutableLiveData()
    val forecastLiveData:MutableLiveData<ForecastModel> = MutableLiveData()

    fun fetchData(){
        viewModelScope.launch {
            try {
                currentLiveData.value = repository.fetchCurrentData(locationLiveData.value!!)
                forecastLiveData.value = repository.fetchForecastData(locationLiveData.value!!)

            }catch( e:Exception){
               Log.d("WeatherViewModel",e.localizedMessage)
            }
        }
    }

    fun setNewLocation(location: Location){
        locationLiveData.value=location;
    }
}