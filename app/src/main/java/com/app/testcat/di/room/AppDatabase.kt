package com.app.testcat.di.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.testcat.api.DaoCat
import com.app.testcat.model.CatNet

@Database(entities = [CatNet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): DaoCat
}