package com.app.testcat.model

data class CatUI(

    val id: String,
    val url: String,

    var isFavorite: Boolean = false

){
    fun toCatNet(): CatNet{
        return CatNet(id, url)
    }
}
