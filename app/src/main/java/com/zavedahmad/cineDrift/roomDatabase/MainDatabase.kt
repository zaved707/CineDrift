package com.zavedahmad.cineDrift.roomDatabase


import androidx.room.Database

import androidx.room.RoomDatabase


@Database(entities = [ PreferenceEntity::class], version = 1)
abstract class MainDatabase : RoomDatabase(){
    abstract fun preferencesDao(): PreferencesDao
}