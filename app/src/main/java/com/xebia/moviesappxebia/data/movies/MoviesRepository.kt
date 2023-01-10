package com.xebia.moviesappxebia.data.movies

import kotlinx.coroutines.flow.flow

/**
 *  this class is used for collecting data from api call
 *
 */
class MoviesRepository(private val service: MoviesService) {

    companion object{
        const val IMAGE_PATH = "https://image.tmdb.org/t/p/w154"
    }
    suspend fun fetchPopularMovies() = flow {
        emit(service.fetchPopularMovies().results)
    }

    suspend fun fetchNowPlayingMovies() = flow {
        emit(service.fetchNowPlayingMovies().results)
    }

    suspend fun fetchMoviesById(movieId: Int) = flow {
        emit(service.fetchMovieDetailsById(movieId))
    }
}