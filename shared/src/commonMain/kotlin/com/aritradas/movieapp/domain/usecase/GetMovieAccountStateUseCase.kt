package com.aritradas.movieapp.domain.usecase

import com.aritradas.movieapp.domain.model.MovieAccountState
import com.aritradas.movieapp.domain.repository.FavoriteRepository

class GetMovieAccountStateUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(movieId: Int): MovieAccountState {
        return favoriteRepository.getMovieAccountStates(movieId)
    }
}
