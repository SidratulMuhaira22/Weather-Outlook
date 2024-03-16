package com.hera.weatheroutlook.di

import android.content.Context
import androidx.room.Room
import com.hera.weatheroutlook.room.WeatherDao
import com.hera.weatheroutlook.room.WeatherStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(@ApplicationContext context: Context): WeatherStorage {
        return Room.databaseBuilder(context, WeatherStorage::class.java, "Weather_Room_Db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(db: WeatherStorage): WeatherDao {
        return db.weatherDao()
    }
}