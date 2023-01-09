package com.xebia.moviesappxebia.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NowPlayingResponse(
    val dates: Dates,
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)