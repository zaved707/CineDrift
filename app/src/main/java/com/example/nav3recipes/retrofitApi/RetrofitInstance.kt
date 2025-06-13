package com.example.nav3recipes.retrofitApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val movieDetailApi: MovieDetailApi = getInstance().create(MovieDetailApi::class.java)
}