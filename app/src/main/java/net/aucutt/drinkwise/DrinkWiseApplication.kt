package net.aucutt.drinkwise

import android.app.Application
import net.aucutt.drinkwise.data.DrinkDatabase
import net.aucutt.drinkwise.data.DrinkRepository

class DrinkWiseApplication : Application() {

    val database: DrinkDatabase by lazy { DrinkDatabase.getDatabase(this) }
    val drinkRepository: DrinkRepository by lazy { DrinkRepository(database.drinkDao()) }
}
