package com.winda.couchpotato.core.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = FavoriteShowEntity.TABLE_NAME)
@Parcelize
class FavoriteShowEntity (
    @PrimaryKey
    val id : Int,
    val posterUrl : String?,
    val backdropUrl : String?,
    val title: String,
    val releaseDateEpoch : Long,
    val userScores : Int,
    val overview : String) : Parcelable {

    companion object {
        const val TABLE_NAME = "favorite_show"
    }

}