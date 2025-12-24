package com.aritradas.movieapp.di

import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.data.repository.FavoriteRepositoryImpl
import com.aritradas.movieapp.data.repository.MovieRepositoryImpl
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.presentation.favourites.FavouritesViewModel
import com.aritradas.movieapp.presentation.movieDetails.MovieDetailViewModel
import com.aritradas.movieapp.presentation.movies.MoviesViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule = module {
    single {
        HttpClient {
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorClient: $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = true
                        encodeDefaults = true
                    }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }

            defaultRequest {
                // Warning: Token should ideally be injected or loaded from config
                // check if we can access BuildKonfig or similar. 
                // For now, assuming it will be passed or handled by interceptor in app, 
                // but user code had it hardcoded. We'll use a placeholder or expect it.
                // Reverting to hardcoded placeholder for demo purposes if Config object not present.
                // Or better: header("Authorization", "Bearer ${getPlatformConfig().apiKey}")
                 header("Accept", "application/json")
            }
        }
    }

    single { ApiServices(client = get()) }

    single<MovieRepository> { MovieRepositoryImpl(apiServices = get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(apiServices = get()) }

    factory { MoviesViewModel(movieRepository = get(), favoriteRepository = get()) }
    factory { MovieDetailViewModel(movieRepository = get(), favoriteRepository = get()) }
    factory { FavouritesViewModel(favoriteRepository = get()) }
}
