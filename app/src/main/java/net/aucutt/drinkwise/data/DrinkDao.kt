package net.aucutt.drinkwise.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface DrinkDao {

    @Query("SELECT * FROM drink ORDER by date DESC")
    fun observeDrink() : Flow<List<Drink>>

    @Upsert
    suspend fun upsert(drink: Drink)

    @Query("SELECT * FROM drink ORDER by date DESC")
    suspend fun returnDrinks() : List<Drink>

    @Delete("DELETE * FROM drink")
    sups
}