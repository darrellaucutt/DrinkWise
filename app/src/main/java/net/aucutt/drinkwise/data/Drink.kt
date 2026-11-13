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
        return "Date: ${ formatTimestamp(date)} Type: $type  weight: $drinkWeight"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTimestamp(timestampMs: Long): String {
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy hh:mma", Locale.US)
        return Instant.ofEpochMilli(timestampMs).atZone(ZoneId.systemDefault()).format(formatter) .lowercase()
    }
}