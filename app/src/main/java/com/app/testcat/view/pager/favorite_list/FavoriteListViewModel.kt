package com.app.testcat.view.pager.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.testcat.api.DaoCat
import com.app.testcat.model.CatUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val daoCat: DaoCat
) : ViewModel() {


    fun getCats(): Flow<List<CatUI>> = daoCat.getAllCats().map {
            listNet ->
        listNet.map { catNet -> catNet.toCatUI(true) }.reversed()
    }


    fun deleteFavorite(cat: CatUI){
        viewModelScope.launch {
            daoCat.delete(cat.toCatNet())
        }
    }

}