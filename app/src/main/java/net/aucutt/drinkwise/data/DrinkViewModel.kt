package net.aucutt.drinkwise.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.aucutt.drinkwise.DrinkWiseApplication
import net.aucutt.drinkwise.math.StandardDrink
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

class DrinkViewModel(application: Application) : AndroidViewModel(application) {

    private val drinkWiseApplication = application as DrinkWiseApplication
    private val drinkRepository : DrinkRepository = drinkWiseApplication.drinkRepository
    val drinks: StateFlow<List<Drink>> = drinkRepository.drinks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addDrink(drink: Drink) {
        viewModelScope.launch {
            drinkRepository.addDrink(drink)
        }
    }

    fun resetDrinks() {
        viewModelScope.launch{
            drinkRepository.resetDatabase()
        }
    }

    fun trimDataByDate(deleteFrom : Long) {
        viewModelScope.launch {
            drinkRepository.trimDrinksByDate(deleteFrom)
        }
    }


    fun generateTestData() {
        val currentTime =  System.currentTimeMillis()
        val oldestTime = currentTime - (14.days.inWholeMilliseconds)
        val rando  =  Random( oldestTime)
        viewModelScope.launch {
        for(idx in 1..( rando.nextInt(1, 20))) {
            val drink = Drink(
                date = rando.nextLong(oldestTime,currentTime),
                type = StandardDrink.DrinkType.entries[rando.nextInt(0, StandardDrink.DrinkType.entries.size)].name,
                drinkWeight = rando.nextFloat() + rando.nextFloat()
            )
            drinkRepository.addDrink(drink)
        }
        }
    }
}
