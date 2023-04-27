package com.app.testcat.interfaces

import com.app.testcat.model.CatUI

interface CatItemListener {

    fun clickFavorite(item: CatUI)
    fun clickImage(item:CatUI)


}