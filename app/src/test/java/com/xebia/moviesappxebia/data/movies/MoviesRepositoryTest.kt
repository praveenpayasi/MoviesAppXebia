package com.xebia.moviesappxebia.data.movies

import io.mockk.impl.annotations.MockK

class MoviesRepositoryTest {

    @MockK
    private lateinit var service: MoviesService
}