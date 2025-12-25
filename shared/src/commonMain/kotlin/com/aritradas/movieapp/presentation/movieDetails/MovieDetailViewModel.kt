package com.aritradas.movieapp.presentation.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.domain.usecase.GetAccountDetailsUseCase
import com.aritradas.movieapp.domain.usecase.GetMovieAccountStateUseCase
import com.aritradas.movieapp.domain.usecase.GetMovieDetailsUseCase
import com.aritradas.movieapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val getMovieAccountStateUseCase: GetMovieAccountStateUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _movieDetail = MutableStateFlow<MovieDetail?>(null)
    val movieDetail = _movieDetail.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    private var accountId: Int? = null

    init {
        fetchAccountDetails()
    }

    private fun fetchAccountDetails() {
        viewModelScope.launch {
            try {
                val account = getAccountDetailsUseCase()
                accountId = account.id
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieDetail.value = getMovieDetailsUseCase(movieId)
                checkIfFavorite(movieId)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun checkIfFavorite(movieId: Int) {
        viewModelScope.launch {
            try {
                val state = getMovieAccountStateUseCase(movieId)
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
                toggleFavoriteUseCase(
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
