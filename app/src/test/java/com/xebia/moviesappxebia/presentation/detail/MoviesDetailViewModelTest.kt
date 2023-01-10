package com.xebia.moviesappxebia.presentation.detail

import com.xebia.moviesappxebia.domain.MoviesUseCase
import com.xebia.moviesappxebia.domain.model.MovieDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class MoviesDetailViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var moviesUseCase: MoviesUseCase

    private lateinit var viewModel: MovieDetailViewModel

    @MockK
    private lateinit var movieDetails: MovieDetails

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `movies detail state flow is emitting data`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            // Step 1 In set up
            coEvery { moviesUseCase.fetchMoviesDetails(76600) } returns flowOf(movieDetails)

            // Step 2
            viewModel = MovieDetailViewModel(moviesUseCase)
            viewModel.getMovieDetailsById(76600)
            val movieDetailState = viewModel.movieDetailState.take(2).toList()

            // Step 3
            Assert.assertEquals(movieDetailState[0], MovieDetails())
            Assert.assertEquals(movieDetailState[1], movieDetails)
        }
    }
}