package com.zavedahmad.cineDrift.roomDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel

@TypeConverters(MovieDetailModelConverter::class)
@Entity(tableName = "FavouritesTable")
data class FavouritesEntity (
    @PrimaryKey
    val movieID: String,
    val movieData: MovieDetailModel
)