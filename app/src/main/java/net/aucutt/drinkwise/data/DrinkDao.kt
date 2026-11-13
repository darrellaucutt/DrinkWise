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

    @Query("DELETE FROM drink")
    suspend fun deleteAllDrinks()

    @Query(value = "DELETE FROM drink where date < :deleteFrom")
    suspend fun trimDrinksByDate(deleteFrom : Long)

    @Query(value = "DELETE FROM drink where id = :id")
    suspend fun deleteById(id : Long)

}