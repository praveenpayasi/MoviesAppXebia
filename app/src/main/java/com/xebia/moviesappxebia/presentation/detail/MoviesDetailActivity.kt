package com.xebia.moviesappxebia.presentation.detail

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import com.xebia.moviesappxebia.data.moviesService
import com.xebia.moviesappxebia.data.movies.MoviesRepository
import com.xebia.moviesappxebia.domain.MoviesUseCase
import com.xebia.moviesappxebia.presentation.home.kanitFontFamily
import com.xebia.moviesappxebia.utils.Network

/**
 *  MoviesDetailActivity is for displaying movies details
 *
 */
class MoviesDetailActivity : ComponentActivity() {

    private lateinit var viewModel: MovieDetailViewModel
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = intent.getIntExtra("KEY_MOVIE_ID", 0)

        viewModel = MovieDetailViewModel(
            MoviesUseCase(
                MoviesRepository(
                    moviesService
                )
            )
        )

        setContent {
            MovieDetailContainer()
        }
    }

    @Composable
    fun MovieDetailContainer() {
        Surface(
            modifier = Modifier,
            color = Color.Black.copy(alpha = 0.8f) // This is what you're missing
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = "Close button",
                            tint = Color.White
                        )
                    }
                }
                MovieDetail()
            }
        }
    }

    @Composable
    fun MovieDetail() {
        if (Network.checkConnectivity(this)) {
            viewModel.getMovieDetailsById(movieId = movieId)
        } else {
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
        val movieDetail by viewModel.movieDetailState.collectAsState()
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                imageModel = movieDetail.strImage,
                modifier = Modifier
                    .width(150.dp)
                    .height(220.dp)
                    .padding(top = 20.dp, bottom = 20.dp),
                contentScale = ContentScale.FillBounds,
                contentDescription = "Movie image"
            )
            Text(
                text = movieDetail.strTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontFamily = kanitFontFamily
            )

            Text(
                text = movieDetail.strDate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = kanitFontFamily
            )


            Text(
                text = "Overview",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, bottom = 10.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = kanitFontFamily
            )

            Text(
                text = movieDetail.strOverview,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 16.dp, bottom = 16.dp),
                fontWeight = FontWeight.Normal,
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = kanitFontFamily
            )

            val genre = movieDetail.strGenreTitle
            val genreScrollState = rememberScrollState()
            Row(modifier = Modifier.horizontalScroll(genreScrollState)) {
                genre.forEach {
                    GenreItem(it)
                }
            }
        }
    }

    @Composable
    fun GenreItem(title: String) {
        Row {
            Text(
                text = title.uppercase(),
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 8.dp, bottom = 16.dp)
                    .background(color = Color.White, shape = RectangleShape)
                    .padding(6.dp),
                fontWeight = FontWeight.Normal,
                fontFamily = kanitFontFamily
            )
        }
    }
}