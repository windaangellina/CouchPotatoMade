package com.winda.couchpotato.core.domain.model

import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.winda.couchpotato.core.utils.FunctionLibrary
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
class Show(
    val showId : Int,
    val posterUrl : String?,
    val backdropUrl : String?,
    val title: String,
    val releaseDateEpoch : Long,
    val userScores : Int,
    val overview : String
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getReleaseDateAsString() : String? {
        // unix timestamps uses seconds while java measures time in milliseconds -> hence * 1000
        val date = FunctionLibrary.getLocalDateTimeFromLong(releaseDateEpoch * 1000L)
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)

        return if (releaseDateEpoch == 0L){
            ""
        } else{
            date.format(formatter)
        }
    }

    fun getUserScoresAsString() : String{
        return "$userScores %"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTitleWithReleaseYear() : String? {
        // unix timestamps uses seconds while java measures time in milliseconds -> hence * 1000
        val releaseDate = FunctionLibrary.getLocalDateTimeFromLong(releaseDateEpoch * 1000L)

        return if (releaseDateEpoch == 0L) title
        else{
            "$title (${releaseDate.year})"
        }
    }
}