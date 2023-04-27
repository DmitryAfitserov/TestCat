package com.app.testcat.view.pager.cat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.app.testcat.api.ApiCat
import com.app.testcat.api.DaoCat
import com.app.testcat.model.CatUI
import com.app.testcat.model.CatNet
import com.app.testcat.source.CatPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatListViewModel @Inject constructor(
    private val apiCat: ApiCat,
    private val daoCat: DaoCat
) : ViewModel() {

    init {
        viewModelScope.launch {
            flowBd()
        }
    }

    var catBdFlow: StateFlow<List<CatNet>>? = null


    suspend fun flowBd(): StateFlow<List<CatNet>> {
        if (catBdFlow == null) {
            catBdFlow = daoCat.getAllCats().stateIn(viewModelScope)
        }
        return catBdFlow!!
    }


//    val cat: Flow<PagingData<CatUI>> = Pager(
//        PagingConfig(10, prefetchDistance = 1),
//    ){
//        CatPagingSource(apiCat)
//    }.flow.map { pagingData  ->
//        pagingData.map { catNet -> CatUI(catNet.id, catNet.url, flow().value.any { catBD -> catBD.id == catNet.id }) }
//    }.cachedIn(viewModelScope)


    fun paging() = Pager(
        PagingConfig(10, prefetchDistance = 1),
    ) {
        CatPagingSource(apiCat)
    }.flow.map { pagingData ->
        pagingData.map { catNet ->
            catNet.toCatUI(flowBd().value.any { catBD -> catBD.id == catNet.id })
        }
    }.cachedIn(viewModelScope)


    fun clickFavorite(cat: CatUI) {
        catBdFlow?.let {
            if (it.value.any { catNet -> catNet.id == cat.id }) {
                cat.isFavorite = false
                viewModelScope.launch {
                    daoCat.delete(cat.toCatNet())
                }
            } else {
                cat.isFavorite = true
                viewModelScope.launch {
                    daoCat.insert(cat.toCatNet())
                }
            }
        }
    }


}