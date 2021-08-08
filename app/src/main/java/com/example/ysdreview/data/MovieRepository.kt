package com.example.ysdreview.data

import android.content.Context
import com.example.ysdreview.data.local.MovieLocalDataSource
import com.example.ysdreview.data.remote.MovieRemoteDataSource
import com.example.ysdreview.model.MovieDetail

class MovieRepository(
    private val context: Context,
    private val remoteSource: MovieRemoteDataSource = MovieRemoteDataSource()
) {

    private val localSource: MovieLocalDataSource = MovieLocalDataSource(context)

    suspend fun fetchNowPlayingMovies() = remoteSource.fetchNowPlayingMovies()

    suspend fun searchMovies(query: String) = remoteSource.searchMovies(query)

    suspend fun fetchUpcomingMovies() = remoteSource.fetchUpcomingMovies()

   suspend fun getDetailMovie(movieId: Int): Result<MovieDetail> {
        val localData = localSource.getMovieDetailById(movieId)

        return if (localData == null) {
            val remoteData = remoteSource.fetchDetailMovie(movieId)
            localSource.saveMovieDetail(remoteData.getOrDefault(MovieDetail()))
            remoteData
        } else {
            Result.success(localData)
        }
    }
}