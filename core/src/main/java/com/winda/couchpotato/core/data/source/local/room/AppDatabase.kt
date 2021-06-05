package com.winda.couchpotato.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.winda.couchpotato.core.data.source.local.entity.FavoriteShowEntity
import com.winda.couchpotato.core.data.source.local.entity.ShowEntity

@Database(entities = [FavoriteShowEntity::class, ShowEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao() : MovieDatabaseDao
}