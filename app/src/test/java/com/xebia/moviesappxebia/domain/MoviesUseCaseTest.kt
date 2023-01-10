package com.xebia.moviesappxebia.domain

import com.xebia.moviesappxebia.data.model.*
import com.xebia.moviesappxebia.data.model.MoviesDetailResponse
import com.xebia.moviesappxebia.data.model.Result
import com.xebia.moviesappxebia.data.model.ResultX
import com.xebia.moviesappxebia.data.movies.MoviesRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.xebia.moviesappxebia.domain.model.PopularMovie as DomainPopularMovie
import com.xebia.moviesappxebia.data.model.Result as DataPopularMovie
import com.xebia.moviesappxebia.domain.model.NowPlaying as DomainNowPlaying
import com.xebia.moviesappxebia.data.model.ResultX as DataNowPlaying
import com.xebia.moviesappxebia.domain.model.MovieDetails as DomainMovieDetails
import com.xebia.moviesappxebia.data.model.MoviesDetailResponse as DataMovieDetails


class MoviesUseCaseTest {

    companion object{
        const val IMAGE_PATH = "https://image.tmdb.org/t/p/w154"
    }
    @MockK
    private lateinit var repository: MoviesRepository

    private lateinit var moviesUseCase: MoviesUseCase

    private val listOfDataPopularMovie: List<Result> = listOf(
        DataPopularMovie(adult = false, backdrop_path = "backdrop_path1",
            genre_ids = listOf(0,1,2), id = 1, original_language = "en", original_title = "Movie 1",
            overview = "overview1", popularity = 10.0, poster_path = "image url 1", release_date = "9-1-2023", title = "Title1",
            video = true, vote_average = 77.0, vote_count = 1),
        DataPopularMovie(adult = false, backdrop_path = "backdrop_path2",
            genre_ids = listOf(0,1,2), id = 2, original_language = "en", original_title = "Movie 2",
            overview = "overview2", popularity = 10.0, poster_path = "image url 2", release_date = "9-1-2023", title = "Title2",
            video = true, vote_average = 77.0, vote_count = 1),
    )

    private val listOfDomainPopularMovie: List<DomainPopularMovie> = listOf(
        DomainPopularMovie(idMovie = 1, strTitle = "Movie 1", strDate = "9-1-2023",strVote = 77.0,strImageUrl = "${IMAGE_PATH}image url 1"),
        DomainPopularMovie(idMovie = 2, strTitle = "Movie 2", strDate = "9-1-2023",strVote = 77.0,strImageUrl = "${IMAGE_PATH}image url 2"),
    )

    private val listOfDataNowPlayingMovie: List<ResultX> = listOf(
        DataNowPlaying(adult = true, backdrop_path = "backdrop_path3",
            genre_ids = listOf(3,4,5), id = 3, original_language = "hi", original_title = "Movie 3",
            overview = "overview3", popularity = 20.0, poster_path = "image url 3", release_date = "10-1-2023", title = "Title3",
            video = true, vote_average = 70.0, vote_count = 3),
        DataNowPlaying(adult = true, backdrop_path = "backdrop_path4",
            genre_ids = listOf(6,7,8), id = 4, original_language = "en", original_title = "Movie 4",
            overview = "overview4", popularity = 20.0, poster_path = "image url 4", release_date = "10-1-2023", title = "Title4",
            video = true, vote_average = 70.0, vote_count = 4),
    )

    private val listOfDomainNowPlaying: List<DomainNowPlaying> = listOf(
        DomainNowPlaying(movieId = 3,strImage = "${IMAGE_PATH}image url 3"),
        DomainNowPlaying(movieId = 4,strImage = "${IMAGE_PATH}image url 4"),
    )



    private val belongsToCollectionData : BelongsToCollection = BelongsToCollection(
        backdrop_path = "backdrop_path",id = 1, name = "Name1", poster_path ="poster_path")

    private val listOfGenre : List<Genre> = listOf(
        Genre(id = 1, name = "Name1"),
        Genre(id = 2, name = "Name2"),
    )

    private val listOfProductionCompany : List<ProductionCompany> = listOf(
        ProductionCompany(id = 1, logo_path = "logo_path1",name = "Name1",origin_country = "origin_country1"),
        ProductionCompany(id = 2, logo_path = "logo_path2",name = "Name2",origin_country = "origin_country2"),
    )

    private val listOfProductionCountry : List<ProductionCountry> = listOf(
        ProductionCountry(iso_3166_1 = "1",name = "Name1"),
        ProductionCountry(iso_3166_1 = "2", name = "Name2"),
    )

    private val listOfSpokenLanguage : List<SpokenLanguage> = listOf(
        SpokenLanguage(english_name = "english_name1",iso_639_1 = "1",name = "Name1"),
        SpokenLanguage(english_name = "english_name2",iso_639_1 = "2", name = "Name2"),
    )

    private val moviesDetailResponse : MoviesDetailResponse = DataMovieDetails(
        adult = true, backdrop_path = "backdrop_path3",belongs_to_collection = belongsToCollectionData,
        budget = 100, genres = listOfGenre, homepage = "homepage",id = 1, imdb_id = "1", original_language = "en",
        original_title = "title1", overview = "Overview", popularity = 77.0, poster_path = "image url 1",
        production_companies = listOfProductionCompany, production_countries = listOfProductionCountry,
        release_date = "9-1-2023", revenue = 100, runtime = 10, spoken_languages = listOfSpokenLanguage,
        status = "status", tagline = "tagline", title = "Movie 1",video = true, vote_average = 0.0, vote_count = 77)


    private val listOfGenreTitle : List<String> = listOf(
        ("Name1"),
        ("Name2"),
    )

    private val listOfDomainMovieDetails: DomainMovieDetails =
        DomainMovieDetails(strImage = "${IMAGE_PATH}image url 1", strTitle = "Movie 1", strDate = "9-1-2023",strOverview = "Overview", strGenreTitle = listOfGenreTitle)


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        moviesUseCase = MoviesUseCase(repository)
    }

    @Test
    fun `when most popular movies is fetched then it is mapped to PopularMovie`() {
        // Step 1
        coEvery { repository.fetchPopularMovies() } returns flowOf(listOfDataPopularMovie)
        runBlocking {
            // Step 2
            val actualDomainPopularMovies = moviesUseCase.fetchMostPopularMovies().first()

            // Step 3
            assertEquals(listOfDomainPopularMovie, actualDomainPopularMovies)
        }
    }

    @Test
    fun `when now playing movies is fetched then it is mapped to NowPlaying`() {
        // Step 1
        coEvery { repository.fetchNowPlayingMovies() } returns flowOf(listOfDataNowPlayingMovie)
        runBlocking {
            // Step 2
            val actualDomainNowPlayingMovies = moviesUseCase.fetchNowPlayingMovies().first()

            // Step 3
            assertEquals(listOfDomainNowPlaying, actualDomainNowPlayingMovies)
        }
    }

    @Test
    fun `when movies details is fetched then it is mapped to MovieDetails`() {
        // Step 1
        coEvery { repository.fetchMoviesById(76600) } returns flowOf(moviesDetailResponse)
        runBlocking {
            // Step 2
            val actualDomainMovieDetails = moviesUseCase.fetchMoviesDetails(76600).first()

            // Step 3
            assertEquals(listOfDomainMovieDetails, actualDomainMovieDetails)
        }
    }

}