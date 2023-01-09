package com.xebia.moviesappxebia.data.movies

import com.xebia.moviesappxebia.data.ACCESS_TOKEN
import com.xebia.moviesappxebia.data.API_PATH
import com.xebia.moviesappxebia.data.API_VERSION
import com.xebia.moviesappxebia.data.model.MoviesDetailResponse
import com.xebia.moviesappxebia.data.model.NowPlayingResponse
import com.xebia.moviesappxebia.data.model.PopularMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesService {

    @GET("$API_VERSION/$API_PATH/popular?api_key=$ACCESS_TOKEN&language=en-US&page=1")
    suspend fun fetchPopularMovies(): PopularMoviesResponse

    @GET("$API_VERSION/$API_PATH/now_playing?api_key=$ACCESS_TOKEN&language=en-US&page=1")
    suspend fun fetchNowPlayingMovies(): NowPlayingResponse

    @GET("$API_VERSION/$API_PATH/{movieId}?api_key=$ACCESS_TOKEN&language=en-US&page=1")
    suspend fun fetchMovieDetailsById(@Path("movieId") movieId: Int): MoviesDetailResponse
}
