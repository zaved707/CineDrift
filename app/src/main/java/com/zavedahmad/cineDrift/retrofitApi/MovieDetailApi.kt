package com.zavedahmad.cineDrift.retrofitApi

import com.zavedahmad.cineDrift.AuthorizationModel
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel
import com.zavedahmad.cineDrift.popularMoviesModel.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApi {
    @GET("authentication")
    suspend fun verifyKey(
        @Header("Authorization") authHeader: String,
        @Header("accept") acceptHeader: String = "application/json"
    ): Response<AuthorizationModel>


    @GET("movie/{movieId}")
    suspend fun getMovie(
        @Path("movieId") movieId: String,
        @Query("language") language: String,
        @Header("Authorization") authHeader: String,
        @Header("accept") acceptHeader: String = "application/json"
    ): Response<MovieDetailModel>

    @GET("movie/{listOf}")
    suspend fun getPopularMovies(
        @Path ("listOf") listOf: String,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Header("Authorization") authHeader: String,
        @Header("accept") acceptHeader: String = "application/json"
    ): Response<MoviesResponse>

    @GET("search/movie")
    suspend fun getMoviesBySearch(
        @Query("query") query:String,
        @Query("include_adult") includeAdult : Boolean,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US",
        @Header("Authorization") authHeader: String,
        @Header("accept") acceptHeader: String = "application/json"
    ): Response<MoviesResponse>
}