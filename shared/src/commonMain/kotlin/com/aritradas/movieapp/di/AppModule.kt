package com.aritradas.movieapp.di

import com.aritradas.movieapp.data.remote.ApiServices
import com.aritradas.movieapp.data.repository.FavoriteRepositoryImpl
import com.aritradas.movieapp.data.repository.MovieRepositoryImpl
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import com.aritradas.movieapp.domain.repository.MovieRepository
import com.aritradas.movieapp.domain.usecase.*
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
                header("Accept", "application/json")
            }
        }
    }

    single { ApiServices(client = get()) }

    single<MovieRepository> { MovieRepositoryImpl(apiServices = get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(apiServices = get()) }

    // UseCases
    factory { GetMoviesUseCase(movieRepository = get()) }
    factory { GetMovieDetailsUseCase(movieRepository = get()) }
    factory { GetAccountDetailsUseCase(favoriteRepository = get()) }
    factory { GetMovieAccountStateUseCase(favoriteRepository = get()) }
    factory { ToggleFavoriteUseCase(favoriteRepository = get()) }
    factory { GetFavoriteMoviesUseCase(favoriteRepository = get()) }

    // ViewModels
    factory { MoviesViewModel(getMoviesUseCase = get()) }
    factory { 
        MovieDetailViewModel(
            getMovieDetailsUseCase = get(),
            getAccountDetailsUseCase = get(),
            getMovieAccountStateUseCase = get(),
            toggleFavoriteUseCase = get()
        ) 
    }
    factory { 
        FavouritesViewModel(
            getAccountDetailsUseCase = get(),
            getFavoriteMoviesUseCase = get()
        ) 
    }
}
