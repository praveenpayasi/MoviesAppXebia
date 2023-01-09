package com.xebia.moviesappxebia.presentation.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import com.xebia.moviesappxebia.data.moviesService
import com.xebia.moviesappxebia.data.movies.MoviesRepository
import com.xebia.moviesappxebia.domain.MostPopularUseCase
import com.xebia.moviesappxebia.domain.model.NowPlaying
import com.xebia.moviesappxebia.domain.model.PopularMovie
import com.xebia.moviesappxebia.presentation.detail.MoviesDetailActivity
import com.xebia.moviesappxebia.utils.CircularProgressBar
import com.xebia.moviesappxebia.utils.Network

class HomeActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = HomeViewModel(
            MostPopularUseCase(
                MoviesRepository(
                    moviesService
                )
            )
        )
        setContent {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Movies",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(48.dp)
                        .padding(top = 10.dp, bottom = 16.dp),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                Container(homeViewModel)
            }
        }
    }

    @Composable
    fun Container(viewModel: HomeViewModel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NowPlayingText()
            Row {
                NowPlayingList(viewModel)
            }
            Divider(color = Color.White, modifier = Modifier
                .fillMaxWidth()
                .height(30.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MostPopularText()
            MostPopularList(homeViewModel)
        }
    }

    @Composable
    fun NowPlayingText() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            Text(
                text = "Playing Now",
                fontSize = 18.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp),
                fontWeight = FontWeight.Normal,
                //fontFamily = kanitFontFamily
            )
        }
    }

    @Composable
    fun NowPlayingList(viewModel: HomeViewModel) {
        if (Network.checkConnectivity(this)){
            viewModel.getNowPlayingMovies()
        }else{
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
        val playing by viewModel.nowPlayingListState.collectAsState()
        val scrollState = rememberScrollState()
        Row(modifier = Modifier.horizontalScroll(scrollState)) {
            playing.forEach {
                NowPlayingListItem(it)
            }
        }
    }

    @Composable
    fun NowPlayingListItem(playing: NowPlaying) {
        Card() {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(this, MoviesDetailActivity::class.java)
                        intent.putExtra("KEY_MOVIE_ID", playing.movieId)
                        startActivity(intent)
                    }
            ) {
                GlideImage(
                    imageModel = playing.strImage,
                    modifier = Modifier
                        .height(180.dp)
                        .width(120.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Now Playing image",
                )
            }
        }
    }

    @Composable
    fun MostPopularText() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
        ) {
            Text(
                text = "Most Popular",
                fontSize = 18.sp,
                color = Color.Yellow,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 16.dp),
                fontWeight = FontWeight.Normal,
                //fontFamily = kanitFontFamily
            )
        }
    }


    @Composable
    fun MostPopularList(viewModel: HomeViewModel) {
        if (Network.checkConnectivity(this)){
            viewModel.getPopularMovies()
        }else{
            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
        val popular by viewModel.popularListState.collectAsState()
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            popular.forEach {
                MostPopularListItem(it)
            }
        }
    }

    @Composable
    fun MostPopularListItem(popular: PopularMovie) {
        Card(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .clickable {
                        val intent = Intent(this, MoviesDetailActivity::class.java)
                        intent.putExtra("KEY_MOVIE_ID", popular.idMovie)
                        startActivity(intent)
                    }
            ) {
                GlideImage(
                    imageModel = popular.strImageUrl,
                    modifier = Modifier
                        .height(120.dp)
                        .width(110.dp)
                        .padding(top = 6.dp, bottom = 6.dp),
                    contentScale = ContentScale.Fit,
                    contentDescription = "Movie image",
                )
                Column() {
                    Text(
                        text = popular.strTitle,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                        //fontFamily = kanitFontFamily
                    )

                    Text(
                        text = popular.strDate,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        fontWeight = FontWeight.Normal,
                        //fontFamily = kanitFontFamily,

                    )
                }

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically){
//                    Text(
//                        text = "70%",
//                        fontSize = 16.sp,
//                        color = Color.White,
//                        fontWeight = FontWeight.Normal,
//                        //fontFamily = kanitFontFamily,
//
//                    )

                    CircularProgressBar(
                        modifier = Modifier.size(60.dp),
                        progress = 30f,
                        progressMax = 100f,
                        progressBarColor = Color.Red,
                        progressBarWidth = 2.dp,
                        backgroundProgressBarColor = Color.White,
                        backgroundProgressBarWidth = 2.dp,
                        roundBorder = true,
                        startAngle = 90f
                    )
                }

            }
        }
    }
}

//val kanitFontFamily = FontFamily(
//    Font(R.font.kanitregular, FontWeight.Normal),
//    Font(R.font.kanitbold, FontWeight.Bold),
//    Font(R.font.kanititalic, FontWeight.Normal, FontStyle.Italic)
//)

