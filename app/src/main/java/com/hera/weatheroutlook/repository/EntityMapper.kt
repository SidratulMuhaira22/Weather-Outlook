package com.hera.weatheroutlook.repository

import com.hera.weatheroutlook.model.WeatherData
import com.hera.weatheroutlook.model.WeatherResponse

object EntityMapper {

    fun mapWeatherResponseToEntity(input: WeatherResponse, currentLocation: Boolean): WeatherData {
        val item = input.weather?.get(0)
        val main = input.main
        val id =
            if (currentLocation) 0
            else {
                when (input.id) {
                        5128638 -> {1} //NY
                        1880252 -> {2} //Singapore
                        1275339 -> {3} //Mumbai
                        1273294 -> {4} //Delhi
                        2147714 -> {5} //Sydney
                        2158177 -> {6} //Melbourne
                        else -> {0}
                    }
                }
        return WeatherData(
            id,
            input.id,
            input.name,
            System.currentTimeMillis(),
            item?.icon,
            item?.description,
            main?.temp,
            main?.feelsLike,
        )
    }
}