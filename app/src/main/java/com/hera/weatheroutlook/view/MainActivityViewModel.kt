package com.hera.weatheroutlook.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hera.weatheroutlook.model.NetworkResult
import com.hera.weatheroutlook.model.WeatherData
import com.hera.weatheroutlook.repository.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val dataStore: DataStore) : ViewModel() {
    private val _otherCities = MutableSharedFlow<NetworkResult<Any>>()
    val otherCities: SharedFlow<NetworkResult<Any>> = _otherCities

    private val _currentLocation = MutableSharedFlow<NetworkResult<Any>>()
    val currentLocation: SharedFlow<NetworkResult<Any>> = _currentLocation

    private val _weatherNetworkResult = MutableSharedFlow<NetworkResult<List<WeatherData>>>()
    val weatherNetworkResult: SharedFlow<NetworkResult<List<WeatherData>>> = _weatherNetworkResult

    fun fetchWeatherOtherCities(cityIds: Array<String>) {
        viewModelScope.launch {
            _otherCities.emitAll(dataStore.fetchWeatherOtherCities(cityIds))
        }
    }

    fun fetchWeatherCurrentLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            _currentLocation.emitAll(dataStore.fetchWeatherCurrentLocation(lat, lon))
        }
    }

    fun fetchWeatherFromDatabase() {
        viewModelScope.launch {
            _weatherNetworkResult.emitAll(dataStore.fetchWeatherFromDatabase())
        }
    }
}