package com.aritradas.movieapp.presentation.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.domain.model.MovieAccountState
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import com.aritradas.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail = _movieDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    // Assuming we need to store accountId somewhere, or fetch it.
    // For simplicity, we'll fetch account details first or assume hardcoded/passed.
    private var accountId: Int? = null

    init {
        fetchAccountDetails()
    }

    private fun fetchAccountDetails() {
        viewModelScope.launch {
            try {
                val account = favoriteRepository.getAccountDetails()
                accountId = account.id
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieDetail.value = movieRepository.getMovieDetails(movieId)
                checkIfFavorite(movieId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch {
            try {
                val state = favoriteRepository.getMovieAccountStates(movieId)
                _isFavorite.value = state.favorite
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun toggleFavorite(movieId: Int) {
        val currentAccountId = accountId ?: return
        val currentFavoriteStatus = _isFavorite.value

        viewModelScope.launch {
            try {
                favoriteRepository.addFavorite(
                    accountId = currentAccountId,
                    mediaId = movieId,
                    isFavorite = !currentFavoriteStatus
                )
                _isFavorite.value = !currentFavoriteStatus
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
