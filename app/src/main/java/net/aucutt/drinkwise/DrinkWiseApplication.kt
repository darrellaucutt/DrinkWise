package net.aucutt.drinkwise

import android.app.Application
import net.aucutt.drinkwise.data.DrinkDatabase
import net.aucutt.drinkwise.data.DrinkRepository

class DrinkWiseApplication : Application() {

    lateinit var database: DrinkDatabase
      private set

    lateinit var drinkRepository: DrinkRepository
        private set

    override fun onCreate() {
        super.onCreate()
        //Moved database and repository to onCreate to get rid of nullables.
        database = DrinkDatabase.getDatabase(this)
        drinkRepository = DrinkRepository(database.drinkDao())
    }
}
