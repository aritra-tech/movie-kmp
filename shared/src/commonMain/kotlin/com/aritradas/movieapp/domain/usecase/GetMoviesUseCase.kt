package com.aritradas.movieapp.domain.usecase

import androidx.paging.PagingData
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(query: String = ""): Flow<PagingData<Movie>> {
        return movieRepository.getMoviesPager(query)
    }
}
