package com.zavedahmad.cineDrift.roomDatabase


import androidx.room.Database

import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ PreferenceEntity::class, FavouritesEntity::class], version = 1, exportSchema = false)
@TypeConverters(MovieDetailModelConverter::class)
abstract class MainDatabase : RoomDatabase(){
    abstract fun preferencesDao(): PreferencesDao
    abstract fun favouritesDao(): FavouritesDao
}