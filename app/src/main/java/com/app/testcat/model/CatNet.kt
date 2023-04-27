package com.app.testcat.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatNet(

    @PrimaryKey val id : String,
    @ColumnInfo(name = "url")val url: String,

    ){

    fun toCatUI(isFavorite: Boolean): CatUI{
        return CatUI(id, url, isFavorite)
    }

}