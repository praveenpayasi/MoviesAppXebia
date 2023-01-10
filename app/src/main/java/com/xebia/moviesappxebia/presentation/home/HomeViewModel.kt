package com.xebia.moviesappxebia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xebia.moviesappxebia.domain.MoviesUseCase
import com.xebia.moviesappxebia.domain.model.NowPlaying
import com.xebia.moviesappxebia.domain.model.PopularMovie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 *  HomeViewModel is for getting manipulated data from use-case layer
 *  and collecting into stateFlow
 */

class HomeViewModel(
    private val moviesUseCase: MoviesUseCase
) :
    ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _popularListState = MutableStateFlow(emptyList<PopularMovie>())

    // The UI collects from this StateFlow to get its state updates
    val popularListState: StateFlow<List<PopularMovie>> = _popularListState

    // Backing property to avoid state updates from other classes
    private val _nowPlayingListState = MutableStateFlow(emptyList<NowPlaying>())

    // The UI collects from this StateFlow to get its state updates
    val nowPlayingListState: StateFlow<List<NowPlaying>> = _nowPlayingListState


    fun getPopularMovies() {
        viewModelScope.launch {
            moviesUseCase.fetchMostPopularMovies().collect {
                _popularListState.value = it
            }
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            moviesUseCase.fetchNowPlayingMovies().collect {
                _nowPlayingListState.value = it
            }
        }
    }
}