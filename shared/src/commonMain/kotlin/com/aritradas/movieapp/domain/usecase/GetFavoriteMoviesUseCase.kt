package com.aritradas.movieapp.domain.usecase

import androidx.paging.PagingData
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(accountId: Int): Flow<PagingData<Movie>> {
        return favoriteRepository.getFavoriteMoviesPager(accountId)
    }
}
