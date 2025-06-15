package com.zavedahmad.cineDrift.roomDatabase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PreferencesDao {
    @Upsert
    suspend fun updatePreference(preferenceEntity: PreferenceEntity)

    @Query("SELECT * FROM PreferencesTable WHERE accessKey = :key ")
    suspend fun getPreference(key: String): PreferenceEntity
}