package com.example.nav3recipes.retrofitApi

import com.example.nav3recipes.movieDetailModel.MovieDetailModel
import com.example.nav3recipes.popularMoviesModel.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDetailApi {
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
}