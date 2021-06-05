package com.winda.couchpotato.core.utils

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object FunctionLibrary {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId: ZoneId = ZoneId.systemDefault()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateFromString(dateString: String) : LocalDate {
        return LocalDate.parse(dateString, formatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateAsLong(dateString: String) : Long{
        val date = getDateFromString(dateString)
        return date.atStartOfDay(zoneId).toEpochSecond()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLocalDateTimeFromLong(epochTime : Long) : LocalDateTime {
        // JVM representation of a millisecond epoch absolute instant
        val instant = Instant.ofEpochMilli(epochTime)

        // Adding the timezone information to be able to format it (change accordingly)
        return LocalDateTime.ofInstant(instant, zoneId)
    }

    fun makeToast(context: Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun buildLikeQuery(query : String) : String {
        val arr = query.split(' ')
        return "%${arr.joinToString(separator = "%") { it }}%"
    }

}