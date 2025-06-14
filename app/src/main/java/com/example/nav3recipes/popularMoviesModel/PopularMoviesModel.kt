package com.example.nav3recipes.popularMoviesModel

data class PopularMoviesModel(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)