package com.aritradas.movieapp.domain.repository

import androidx.paging.PagingData
import com.aritradas.movieapp.domain.model.Movie
import com.aritradas.movieapp.domain.model.MovieDetail
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieDetails(movieId: Int): MovieDetail
    fun getMoviesPager(query: String = ""): Flow<PagingData<Movie>>
}
