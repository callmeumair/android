package com.commutetimely.core.data.di

import android.content.Context
import com.commutetimely.core.data.commute.CommuteRepositoryImpl
import com.commutetimely.core.data.location.LocationService
import com.commutetimely.core.data.remote.MapboxService
import com.commutetimely.core.data.remote.WeatherbitService
import com.commutetimely.core.data.weather.WeatherRepositoryImpl
import com.commutetimely.core.domain.repository.CommuteRepository
import com.commutetimely.core.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Hilt module for data layer dependencies
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("weatherbit")
    fun provideWeatherbitRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherbit.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("mapbox")
    fun provideMapboxRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.mapbox.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherbitService(@Named("weatherbit") retrofit: Retrofit): WeatherbitService {
        return retrofit.create(WeatherbitService::class.java)
    }

    @Provides
    @Singleton
    fun provideMapboxService(@Named("mapbox") retrofit: Retrofit): MapboxService {
        return retrofit.create(MapboxService::class.java)
    }

    @Provides
    @Singleton
    @Named("weatherbit_api_key")
    fun provideWeatherbitApiKey(): String {
        return "836afe5ccf9c46e1bc2fa3a894f676b3" // From buildConfig
    }

    @Provides
    @Singleton
    @Named("mapbox_access_token")
    fun provideMapboxAccessToken(): String {
        return "pk.eyJ1IjoiY29tbXV0ZXRpbWVseSIsImEiOiJjbWUzMzUydmcwMmN1MmtzZnoycGs1ZDhhIn0.438vHnYipmUNS7JoCglyMg" // From buildConfig
    }

    @Provides
    @Singleton
    fun provideLocationService(@ApplicationContext context: Context): LocationService {
        return LocationService(context)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherService: WeatherbitService,
        @Named("weatherbit_api_key") apiKey: String
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherService, apiKey)
    }

    @Provides
    @Singleton
    fun provideCommuteRepository(
        mapboxService: MapboxService,
        @Named("mapbox_access_token") accessToken: String
    ): CommuteRepository {
        return CommuteRepositoryImpl(mapboxService, accessToken)
    }
}
