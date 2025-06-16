package com.zavedahmad.cineDrift.roomDatabase

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {
    @Upsert
    suspend  fun addMovie(favouritesEntity: FavouritesEntity)

    @Query("DELETE FROM FavouritesTable WHERE movieID = :movieID" )
    suspend fun deleteFromFavourites(movieID: String)

    @Query("SELECT * FROM FavouritesTable")
    fun getAllFavouritesFlow(): Flow<List<FavouritesEntity>?>

    @Query("SELECT * FROM FavouritesTable")
    fun getAllFavourites():List<FavouritesEntity>?

    @Query ("SELECT EXISTS(SELECT 1 FROM FavouritesTable WHERE movieID = :movieID)")
    suspend fun checkMovieInFavourites(movieID: String): Boolean

    @Query("SELECT * FROM FavouritesTable WHERE movieID = :movieID")
    suspend fun getMovieByIDFromDatabase(movieID: String): FavouritesEntity
}