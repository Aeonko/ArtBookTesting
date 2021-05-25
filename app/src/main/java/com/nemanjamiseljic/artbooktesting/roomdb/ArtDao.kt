package com.nemanjamiseljic.artbooktesting.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArt(art: Art)

    @Update
    suspend fun updateArt(art: Art)

    @Delete
    suspend fun deleteArt(art: Art)

    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>>
}