package com.commutetimely.core.data.weather

import com.commutetimely.core.data.remote.WeatherbitService
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.model.CommuteImpact
import com.commutetimely.core.domain.repository.WeatherRepository
import com.commutetimely.core.domain.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of WeatherRepository using Weatherbit API
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherbitService,
    private val apiKey: String
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double
    ): Resource<WeatherInfo> {
        return try {
            val response = weatherService.getCurrentWeather(
                lat = latitude,
                lon = longitude,
                key = apiKey
            )

            if (response.data.isNotEmpty()) {
                val weatherData = response.data.first()
                val weatherInfo = WeatherInfo(
                    temperature = weatherData.temp,
                    feelsLike = weatherData.app_temp,
                    humidity = weatherData.rh,
                    windSpeed = weatherData.wind_spd,
                    windDirection = weatherData.wind_dir,
                    precipitation = weatherData.precip,
                    weatherCondition = weatherData.weather.description,
                    iconCode = weatherData.weather.icon,
                    visibility = weatherData.vis,
                    uvIndex = weatherData.uv.toInt(),
                    airQuality = weatherData.aqi,
                    commuteImpact = calculateCommuteImpact(weatherData)
                )
                Resource.Success(weatherInfo)
            } else {
                Resource.Error("No weather data available")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to fetch weather data: ${e.message}")
        }
    }

    override suspend fun getHourlyForecast(
        latitude: Double,
        longitude: Double,
        hours: Int
    ): Resource<List<WeatherInfo>> {
        return try {
            val response = weatherService.getHourlyForecast(
                lat = latitude,
                lon = longitude,
                key = apiKey,
                hours = hours
            )

            val forecasts = response.data.map { hourlyData ->
                WeatherInfo(
                    temperature = hourlyData.temp,
                    feelsLike = hourlyData.app_temp,
                    humidity = hourlyData.rh,
                    windSpeed = hourlyData.wind_spd,
                    windDirection = hourlyData.wind_dir,
                    precipitation = hourlyData.precip,
                    weatherCondition = hourlyData.weather.description,
                    iconCode = hourlyData.weather.icon,
                    visibility = hourlyData.vis,
                    uvIndex = 0, // Not available in hourly data
                    airQuality = 0, // Not available in hourly data
                    commuteImpact = calculateCommuteImpactFromHourly(hourlyData)
                )
            }
            Resource.Success(forecasts)
        } catch (e: Exception) {
            Resource.Error("Failed to fetch hourly forecast: ${e.message}")
        }
    }

    private fun calculateCommuteImpact(weatherData: com.commutetimely.core.data.remote.CurrentWeatherData): CommuteImpact {
        // Poor conditions
        if (weatherData.precip > 5.0 || // Heavy rain
            weatherData.snow > 2.0 || // Snow
            weatherData.vis < 1.0 || // Poor visibility
            weatherData.wind_spd > 50.0 || // High winds
            weatherData.weather.code in listOf(200, 201, 202, 210, 211, 212, 221, 230, 231, 232) // Thunderstorms
        ) {
            return CommuteImpact.POOR
        }

        // Moderate conditions
        if (weatherData.precip > 1.0 || // Light rain
            weatherData.snow > 0.0 || // Any snow
            weatherData.vis < 5.0 || // Reduced visibility
            weatherData.wind_spd > 25.0 || // Moderate winds
            weatherData.temp < -5.0 || weatherData.temp > 35.0 || // Extreme temperatures
            weatherData.weather.code in listOf(300, 301, 302, 310, 311, 312, 313, 314, 321, 500, 501, 502, 511, 520, 521, 522, 531, 611, 612, 615, 616, 620, 621, 622, 701, 711, 721, 731, 741, 751, 761, 762, 771, 781)
        ) {
            return CommuteImpact.MODERATE
        }

        // Good conditions for everything else
        return CommuteImpact.GOOD
    }

    private fun calculateCommuteImpactFromHourly(hourlyData: com.commutetimely.core.data.remote.HourlyForecastData): CommuteImpact {
        // Poor conditions
        if (hourlyData.precip > 5.0 || // Heavy rain
            hourlyData.snow > 2.0 || // Snow
            hourlyData.vis < 1.0 || // Poor visibility
            hourlyData.wind_spd > 50.0 || // High winds
            hourlyData.pop > 80 // High probability of precipitation
        ) {
            return CommuteImpact.POOR
        }

        // Moderate conditions
        if (hourlyData.precip > 1.0 || // Light rain
            hourlyData.snow > 0.0 || // Any snow
            hourlyData.vis < 5.0 || // Reduced visibility
            hourlyData.wind_spd > 25.0 || // Moderate winds
            hourlyData.temp < -5.0 || hourlyData.temp > 35.0 || // Extreme temperatures
            hourlyData.pop > 50 // Moderate probability of precipitation
        ) {
            return CommuteImpact.MODERATE
        }

        // Good conditions for everything else
        return CommuteImpact.GOOD
    }
}
