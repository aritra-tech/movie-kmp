package com.aritradas.movieapp.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aritradas.movieapp.domain.model.AccountDetails
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _accountId = MutableStateFlow<Int?>(null)
    
    val favoriteMovies: Flow<PagingData<Movie>> = _accountId.flatMapLatest { id ->
        if (id != null) {
            favoriteRepository.getFavoriteMoviesPager(id).cachedIn(viewModelScope)
        } else {
            emptyFlow()
        }
    }

    init {
        getAccountDetails()
    }

    private fun getAccountDetails() {
        viewModelScope.launch {
            try {
                val account = favoriteRepository.getAccountDetails()
                _accountId.value = account.id
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
