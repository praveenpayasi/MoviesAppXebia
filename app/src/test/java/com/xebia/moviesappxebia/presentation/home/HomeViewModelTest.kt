package com.xebia.moviesappxebia.presentation.home

import com.xebia.moviesappxebia.domain.MoviesUseCase
import com.xebia.moviesappxebia.domain.model.NowPlaying
import com.xebia.moviesappxebia.domain.model.PopularMovie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.Before
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var moviesUseCase: MoviesUseCase

    private lateinit var viewModel: HomeViewModel

    @MockK
    private lateinit var popularMovie: PopularMovie

    @MockK
    private lateinit var nowPlaying: NowPlaying

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
    fun `now playing movies state flow is emitting data`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            // Step 1 In set up
            val nowPlayingMovieList = listOf(nowPlaying)
            coEvery { moviesUseCase.fetchNowPlayingMovies() } returns flowOf(nowPlayingMovieList)

            // Step 2
            viewModel = HomeViewModel(moviesUseCase)
            viewModel.getNowPlayingMovies()
            val nowPlayingListState = viewModel.nowPlayingListState.take(2).toList()

            // Step 3
            assertEquals(nowPlayingListState[0], emptyList<NowPlaying>())
            assertEquals(nowPlayingListState[1], nowPlayingMovieList)
        }
    }

    @Test
    fun `most popular movies state flow is emitting data`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            // Step 1 In set up
            val popularMovieList = listOf(popularMovie)
            coEvery { moviesUseCase.fetchMostPopularMovies() } returns flowOf(popularMovieList)

            // Step 2
            viewModel = HomeViewModel(moviesUseCase)
            viewModel.getPopularMovies()
            val popularListState = viewModel.popularListState.take(2).toList()

            // Step 3
            assertEquals(popularListState[0], emptyList<PopularMovie>())
            assertEquals(popularListState[1], popularMovieList)

        }
    }

}