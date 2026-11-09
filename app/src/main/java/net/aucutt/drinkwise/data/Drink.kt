package net.aucutt.drinkwise.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "drink")

data class Drink(
    @PrimaryKey val id: String,
    val date: Long,
    val volume: Float,
    val abv: Float
)