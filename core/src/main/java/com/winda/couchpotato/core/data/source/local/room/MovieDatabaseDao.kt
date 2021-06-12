package com.winda.couchpotato.core.data.source.local.room

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.room.*
import com.winda.couchpotato.core.data.source.local.entity.FavoriteShowEntity
import com.winda.couchpotato.core.data.source.local.entity.ShowEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MovieDatabaseDao {
    // favorite
    @Query("select * from ${FavoriteShowEntity.TABLE_NAME}")
    fun getListFavorite() : Flow<List<FavoriteShowEntity>>

    @Query("select count(*) from ${FavoriteShowEntity.TABLE_NAME} where id=:id")
    fun getCountFavoriteById(id : Int) : LiveData<Int>

    @Insert
    suspend fun insertFavorite(favoriteShowEntity: FavoriteShowEntity)

    @Delete
    suspend fun deleteFavorite(favoriteShowEntity: FavoriteShowEntity)

    // shows
    @Query("select * from ${ShowEntity.TABLE_NAME} where type=:type and title like :searchKeyword")
    fun getShows(type : String, searchKeyword : String) : Flow<List<ShowEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = ShowEntity::class)
    suspend fun insertMovies(listMovies : List<ShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = ShowEntity::class)
    suspend fun insertTvShows(listTvShows : List<ShowEntity>)


}