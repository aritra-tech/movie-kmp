package com.aritradas.movieapp.domain.usecase

import com.aritradas.movieapp.domain.model.MovieDetail
import com.aritradas.movieapp.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): MovieDetail {
        return movieRepository.getMovieDetails(movieId)
    }
}
