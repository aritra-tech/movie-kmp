package com.aritradas.movieapp.domain.usecase

import com.aritradas.movieapp.domain.model.AccountDetails
import com.aritradas.movieapp.domain.repository.FavoriteRepository

class GetAccountDetailsUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(): AccountDetails {
        return favoriteRepository.getAccountDetails()
    }
}
