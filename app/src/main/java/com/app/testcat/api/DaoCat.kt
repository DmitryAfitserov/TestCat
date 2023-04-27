package com.app.testcat.api

import androidx.room.*
import com.app.testcat.model.CatNet
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoCat {
    @Query("SELECT * FROM catnet")
    fun getAllCats(): Flow<List<CatNet>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cat: CatNet)

    @Delete
    suspend fun delete(cat: CatNet)
}