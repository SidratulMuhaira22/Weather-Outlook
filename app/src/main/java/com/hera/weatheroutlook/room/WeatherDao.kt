package com.hera.weatheroutlook.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.hera.weatheroutlook.model.WeatherData

@Dao
interface WeatherDao {

    @Upsert(entity = WeatherData::class)
    suspend fun updateOrInsert(entities: Array<WeatherData>)

    @Query("DELETE FROM weather WHERE id = 0")
    suspend fun deleteCurrentLocation()

    @Query("SELECT * FROM weather")
    suspend fun fetchAllWeather(): List<WeatherData>
}