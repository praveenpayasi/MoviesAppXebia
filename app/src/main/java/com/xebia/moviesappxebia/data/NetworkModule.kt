package com.xebia.moviesappxebia.data

import com.xebia.moviesappxebia.data.movies.MoviesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  this class is used for network call using retrofit
 *
 */

const val API_PATH = "/movie/"
const val API_VERSION = "3"
const val ACCESS_TOKEN = "515f9ae65855f1fd8108fc9acffece6b"

val logging = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
val client = OkHttpClient.Builder()
    .addInterceptor(logging)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/")
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val moviesService = retrofit.create(MoviesService::class.java)
