package com.zavedahmad.cineDrift.popularMoviesModel

data class MoviesResponse(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)