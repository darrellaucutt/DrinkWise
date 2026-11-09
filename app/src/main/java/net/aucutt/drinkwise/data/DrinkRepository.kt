package net.aucutt.drinkwise.data

import kotlinx.coroutines.flow.Flow

class DrinkRepository(private val drinkDao: DrinkDao) {

    suspend fun addDrink(drink: Drink) {
        drinkDao.upsert(drink)
    }

    suspend fun getDrinks() : List<Drink> {
        return drinkDao.returnDrinks()
    }

    suspend fun resetDatabase() {

    }

    val drinks: Flow<List<Drink>> = drinkDao.observeDrink()
}