package com.xebia.moviesappxebia.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xebia.moviesappxebia.domain.MostPopularUseCase
import com.xebia.moviesappxebia.domain.model.NowPlaying
import com.xebia.moviesappxebia.domain.model.PopularMovie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class HomeViewModel (
    private val mostPopularUseCase: MostPopularUseCase
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
            mostPopularUseCase.fetchMostPopularMovies().collect {
                _popularListState.value = it
            }
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            mostPopularUseCase.fetchNowPlayingMovies().collect {
                _nowPlayingListState.value = it
            }
        }
    }
}