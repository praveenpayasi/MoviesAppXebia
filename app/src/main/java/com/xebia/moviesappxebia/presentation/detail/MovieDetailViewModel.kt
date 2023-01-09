package com.xebia.moviesappxebia.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xebia.moviesappxebia.domain.MostPopularUseCase
import com.xebia.moviesappxebia.domain.model.MovieDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val mostPopularUseCase: MostPopularUseCase
) : ViewModel() {
    // Backing property to avoid state updates from other classes
    private val _movieDetailState = MutableStateFlow(MovieDetails())

    // The UI collects from this StateFlow to get its state updates
    val movieDetailState: StateFlow<MovieDetails> = _movieDetailState


    fun getMovieDetailsById(movieId: Int) {
        viewModelScope.launch {
            mostPopularUseCase.fetchMoviesDetails(movieId).collect {
                _movieDetailState.value = it
            }
        }
    }
}