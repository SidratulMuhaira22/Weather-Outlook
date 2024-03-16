package com.hera.weatheroutlook.repository

import com.hera.weatheroutlook.BuildConfig
import com.hera.weatheroutlook.ApiService
import com.hera.weatheroutlook.model.NetworkResult
import com.hera.weatheroutlook.model.WeatherData
import com.hera.weatheroutlook.model.WeatherResponse
import com.hera.weatheroutlook.room.WeatherDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStore @Inject constructor(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao
) {
    fun fetchWeatherOtherCities(cityIds: Array<String>): Flow<NetworkResult<Any>> = flow {
        try {
            emit(NetworkResult.Loading)
            var entities = emptyArray<WeatherData>()
            var errorMessage = ""
            for (id in cityIds) {
                val result =
                    apiService.fetchWeatherCityId(id, BuildConfig.API_KEY, "metric")
                if (result.isSuccessful) {
                    val weatherResponse = result.body() as WeatherResponse
                    entities += EntityMapper.mapWeatherResponseToEntity(weatherResponse, false)
                } else {
                    val jsonObj = JSONObject(result.errorBody()!!.charStream().readText())
                    val message = jsonObj.getString("message")
                    errorMessage = message
//                    emit(Result.Error(message))
                }
            }
            if (errorMessage.isNotBlank()) emit(NetworkResult.Error(errorMessage))
            else {
                weatherDao.updateOrInsert(entities)
                emit(NetworkResult.Success(Any()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(e.localizedMessage!!))
        }
    }

    fun fetchWeatherCurrentLocation(lat: Double, lon: Double): Flow<NetworkResult<Any>> = flow {
        try {
            emit(NetworkResult.Loading)
            val result =
                apiService.fetchWeatherLatLon(lat, lon, BuildConfig.API_KEY, "metric")
            if (result.isSuccessful) {
                val weatherResponse = result.body() as WeatherResponse
                val entity = EntityMapper.mapWeatherResponseToEntity(weatherResponse, true)
                weatherDao.deleteCurrentLocation()
                weatherDao.updateOrInsert(arrayOf(entity))
                emit(NetworkResult.Success(Any()))
            } else {
                val jsonObj = JSONObject(result.errorBody()!!.charStream().readText())
                val message = jsonObj.getString("message")
                emit(NetworkResult.Error(message))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(e.localizedMessage!!))
        }
    }

    fun fetchWeatherFromDatabase(): Flow<NetworkResult<List<WeatherData>>> = flow {
        try {
            emit(NetworkResult.Loading)
            val entities = weatherDao.fetchAllWeather()
            emit(NetworkResult.Success(entities))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error(e.localizedMessage!!))
        }
    }
}