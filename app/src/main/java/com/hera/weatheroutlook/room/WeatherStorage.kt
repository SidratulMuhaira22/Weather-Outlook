package com.hera.weatheroutlook.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hera.weatheroutlook.model.WeatherData

@Database(
    entities = [WeatherData::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherStorage : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}