package com.xebia.moviesappxebia.presentation.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.glide.GlideImage
import com.xebia.moviesappxebia.R
import com.xebia.moviesappxebia.data.moviesService
import com.xebia.moviesappxebia.data.movies.MoviesRepository
import com.xebia.moviesappxebia.domain.MoviesUseCase
import com.xebia.moviesappxebia.domain.model.NowPlaying
import com.xebia.moviesappxebia.domain.model.PopularMovie
import com.xebia.moviesappxebia.presentation.detail.MoviesDetailActivity
import com.xebia.moviesappxebia.utils.Network

/**
 *  HomeActivity is home screen for displaying now playing and
 *  most popular movies list
 */
class HomeActivity : ComponentActivity() {

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = HomeViewModel(
            MoviesUseCase(
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
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kanitFontFamily
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
            Divider(
                color = Color.White, modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )
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
                fontFamily = kanitFontFamily
            )
        }
    }

    @Composable
    fun NowPlayingList(viewModel: HomeViewModel) {
        if (Network.checkConnectivity(this)) {
            viewModel.getNowPlayingMovies()
        } else {
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
                fontFamily = kanitFontFamily
            )
        }
    }


    @Composable
    fun MostPopularList(viewModel: HomeViewModel) {
        if (Network.checkConnectivity(this)) {
            viewModel.getPopularMovies()
        } else {
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
                    }, verticalAlignment = Alignment.CenterVertically

            ) {
                Column(modifier = Modifier.weight(2f)) {
                    GlideImage(
                        imageModel = popular.strImageUrl,
                        modifier = Modifier
                            .height(120.dp)
                            .width(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp, start = 16.dp),
                        contentScale = ContentScale.Fit,
                        contentDescription = "Movie image",
                    )
                }
                Column(modifier = Modifier.weight(7f)) {
                    Text(
                        text = popular.strTitle,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1, overflow = TextOverflow.Ellipsis,
                        fontFamily = kanitFontFamily
                    )

                    Text(
                        text = popular.strDate,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                        fontWeight = FontWeight.Normal,
                        fontFamily = kanitFontFamily,

                        )
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AverageVotingProgressBar(popular.strVote)
                }

            }
        }
    }

    /**
     * custom progressBar for showing average voting of particular movie
     */
    @Composable
    fun AverageVotingProgressBar(
        vote: Double,
        size: Dp = 50.dp,
        foregroundIndicatorColor: Color = Color(0xFFE91E63),
        shadowColor: Color = Color.LightGray,
        indicatorThickness: Dp = 2.dp,
        dataUsage: Double = vote,
        animationDuration: Int = 1000,
        dataTextStyle: TextStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.kanitregular, FontWeight.Normal)),
            fontSize = 14.sp,
            color = Color.White
        ),
        remainingTextStyle: TextStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.kanitregular, FontWeight.Normal)),
            fontSize = 5.sp
        )
    ) {
        Log.d("praveen    ", "" + vote)
        // It remembers the data usage value
        var dataUsageRemember by remember {
            mutableStateOf(-1f)
        }

        // This is to animate the foreground indicator
        val dataUsageAnimate = animateFloatAsState(
            targetValue = dataUsageRemember,
            animationSpec = tween(
                durationMillis = animationDuration
            )
        )

        // This is to start the animation when the activity is opened
        LaunchedEffect(Unit) {
            dataUsageRemember = dataUsage.toFloat()
        }

        Box(
            modifier = Modifier
                .size(size),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier
                    .size(size)
            ) {

                // For shadow
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(shadowColor, Color(0xFFD59898)),
                        center = Offset(x = this.size.width / 2, y = this.size.height / 2),
                        radius = this.size.height / 2
                    ),
                    radius = this.size.height / 2,
                    center = Offset(x = this.size.width / 2, y = this.size.height / 2)
                )

                // This is the white circle that appears on the top of the shadow circle
                drawCircle(
                    color = Color.Black,
                    radius = (size / 2 - indicatorThickness).toPx(),
                    center = Offset(x = this.size.width / 2, y = this.size.height / 2)
                )

                // Convert the dataUsage to angle
                val sweepAngle = (dataUsageAnimate.value) * 360 / 100

                // Foreground indicator
                drawArc(
                    color = foregroundIndicatorColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
                    size = Size(
                        width = (size - indicatorThickness).toPx(),
                        height = (size - indicatorThickness).toPx()
                    ),
                    topLeft = Offset(
                        x = (indicatorThickness / 2).toPx(),
                        y = (indicatorThickness / 2).toPx()
                    )
                )
            }

            // Display the average vote value
            DisplayAverageVotingText(
                animateNumber = dataUsageAnimate,
                dataTextStyle = dataTextStyle
            )
        }

    }

    @Composable
    private fun DisplayAverageVotingText(
        animateNumber: State<Float>,
        dataTextStyle: TextStyle
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Text that shows the number inside the circle
            Text(
                text = (animateNumber.value).toInt().toString() + "%",
                style = dataTextStyle
            )
        }
    }
}

val kanitFontFamily = FontFamily(
    Font(R.font.kanitregular, FontWeight.Normal),
    Font(R.font.kanitbold, FontWeight.Bold),
    Font(R.font.kanititalic, FontWeight.Normal, FontStyle.Italic)
)

