package com.winda.couchpotato.core.data.source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = ShowEntity.TABLE_NAME)
@Parcelize
class ShowEntity (
    @PrimaryKey
    val id : Int,
    val type : String,
    val posterUrl : String?,
    val backdropUrl : String?,
    val title: String?,
    val releaseDateEpoch : Long,
    val userScores : Int,
    val overview : String?) : Parcelable {

    companion object {
        const val TABLE_NAME = "shows"
        const val TYPE_MOVIES = "movie"
        const val TYPE_TV = "tv"
    }

}