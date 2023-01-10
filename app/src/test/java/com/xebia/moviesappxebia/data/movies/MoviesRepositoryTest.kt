package com.xebia.moviesappxebia.data.movies

import com.xebia.moviesappxebia.data.model.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 *  this class have the repository layer unit test cases
 *
 */
class MoviesRepositoryTest {

    @MockK
    private lateinit var service: MoviesService

    @MockK
    private lateinit var nowPlayingResponse: NowPlayingResponse

    @MockK
    private lateinit var popularMoviesResponse: PopularMoviesResponse

    @MockK
    private lateinit var moviesDetailResponse: MoviesDetailResponse

    @MockK
    private lateinit var popularMoviesList: List<Result>

    @MockK
    private lateinit var nowPlayingMoviesList: List<ResultX>


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun fetchPopularMovies() {
        coEvery { service.fetchPopularMovies() } returns popularMoviesResponse
        coEvery { popularMoviesResponse.results } returns popularMoviesList

        val moviesRepository = MoviesRepository(service)
        runBlocking {
            val actualResponse = moviesRepository.fetchPopularMovies().first()
            val expectedResponse = popularMoviesList
            Assert.assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun fetchNowPlayingMovies() {
        coEvery { service.fetchNowPlayingMovies() } returns nowPlayingResponse
        coEvery { nowPlayingResponse.results } returns nowPlayingMoviesList

        val moviesRepository = MoviesRepository(service)
        runBlocking {
            val actualResponse = moviesRepository.fetchNowPlayingMovies().first()
            val expectedResponse = nowPlayingMoviesList
            Assert.assertEquals(expectedResponse, actualResponse)
        }
    }

    @Test
    fun fetchMoviesById() {
        coEvery { service.fetchMovieDetailsById(76600) } returns moviesDetailResponse

        val moviesRepository = MoviesRepository(service)
        runBlocking {
            val actualResponse = moviesRepository.fetchMoviesById(76600).first()
            val expectedResponse = moviesDetailResponse
            Assert.assertEquals(expectedResponse, actualResponse)
        }
    }
}