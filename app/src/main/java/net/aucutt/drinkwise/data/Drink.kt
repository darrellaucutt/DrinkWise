package net.aucutt.drinkwise.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Entity(
    tableName = "drink")

data class Drink(
    @PrimaryKey(autoGenerate = true)  val id: Long = 0,
    val date: Long,
    val type: String,
    val drinkWeight: Float
) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return "Date:  ${ getDay()} ${ getTimeOfDay()} Type: $type  weight: $drinkWeight"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFormattedRow() : String {
        return "${getTimeOfDay()} you drank $type which was ${String.format(Locale.US, "%.2f", drinkWeight)} standard drinks."
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDay() : String {
        val formatter = DateTimeFormatter.ofPattern("MMM d", Locale.US)
        return Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).format(formatter).lowercase().replaceFirstChar { it.uppercaseChar() }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeOfDay(): String {
        val formatter = DateTimeFormatter.ofPattern("h:mma", Locale.US)
        return Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).format(formatter)
    }

}