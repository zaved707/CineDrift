package com.zavedahmad.cineDrift.roomDatabase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zavedahmad.cineDrift.movieDetailModel.MovieDetailModel

class MovieDetailModelConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMovieDetailModel(model: MovieDetailModel?): String? {
        return model?.let { gson.toJson(it) }.toString()
    }

    @TypeConverter
    fun toMovieDetailModel(json: String?): MovieDetailModel? {
        return json?.let {
            gson.fromJson(it, object : TypeToken<MovieDetailModel>() {}.type)
        }
    }
}