package com.xebia.moviesappxebia.domain

import com.xebia.moviesappxebia.data.movies.MoviesRepository
import com.xebia.moviesappxebia.domain.model.MovieDetails
import com.xebia.moviesappxebia.domain.model.NowPlaying
import com.xebia.moviesappxebia.domain.model.PopularMovie
import kotlinx.coroutines.flow.map

/**
 *  this class is used for getting data from repository layer
 *  and converting it into data model according to our need.
 */
class MoviesUseCase(private val repository: MoviesRepository) {

    suspend fun fetchMostPopularMovies() =
        repository.fetchPopularMovies().map { movies ->
            movies.map { moviex ->
                PopularMovie(
                    idMovie = moviex.id,
                    strTitle = moviex.original_title,
                    strDate = moviex.release_date,
                    strVote = moviex.vote_average*10,
                    strImageUrl = "${MoviesRepository.IMAGE_PATH}${moviex.poster_path}"
                )
            }
        }

    suspend fun fetchNowPlayingMovies() =
        repository.fetchNowPlayingMovies().map { movies ->
            movies.map { moviex ->
                NowPlaying(
                    movieId = moviex.id,
                    strImage = "${MoviesRepository.IMAGE_PATH}${moviex.poster_path}"
                )
            }
        }

    suspend fun fetchMoviesDetails(movieId: Int) =
        repository.fetchMoviesById(movieId).map { movies ->
            MovieDetails(strImage = "${MoviesRepository.IMAGE_PATH}${movies.poster_path}",
                strTitle = movies.title,
                strDate = movies.release_date,
                strOverview = movies.overview,
                strGenreTitle = movies.genres.map { genre -> genre.name })
        }
}