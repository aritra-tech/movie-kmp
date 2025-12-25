package com.aritradas.movieapp.domain.usecase

import com.aritradas.movieapp.domain.model.FavoriteResponse
import com.aritradas.movieapp.domain.repository.FavoriteRepository

class ToggleFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        mediaId: Int,
        isFavorite: Boolean
    ): FavoriteResponse {
        return favoriteRepository.addFavorite(accountId, mediaId, isFavorite)
    }
}
