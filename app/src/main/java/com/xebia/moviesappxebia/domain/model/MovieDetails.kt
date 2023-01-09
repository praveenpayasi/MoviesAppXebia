package com.xebia.moviesappxebia.domain.model

data class MovieDetails(
    val strImage: String = "",
    val strTitle: String = "",
    val strDate: String = "",
    val strOverview: String = "",
    val strGenreTitle: List<String> = emptyList()

)