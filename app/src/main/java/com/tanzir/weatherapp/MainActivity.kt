package com.tanzir.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.tanzir.weatherapp.file.getLocation
import com.tanzir.weatherapp.viewmodels.WeatherVIewModel

class MainActivity : AppCompatActivity() {

    private val weatherVIewModel: WeatherVIewModel by viewModels()
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
                //Toast.makeText(this, "Precise location access granted", Toast.LENGTH_SHORT).show()
                detectLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                //Toast.makeText(this, "approximate location access granted", Toast.LENGTH_SHORT).show()
                detectLocation()

            }
            else -> {
                // No location access granted.
                Toast.makeText(this, "No location access granted ", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // locationProvider = FusedLocationProviderClient(this)

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun detectLocation() {
       getLocation(this){
           weatherVIewModel.setNewLocation(it)
       }
    }
}