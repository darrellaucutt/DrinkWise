package net.aucutt.drinkwise.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.aucutt.drinkwise.DrinkWiseApplication

class DrinkViewModel(application: Application) : AndroidViewModel(application) {

    private val drinkWiseApplication = application as DrinkWiseApplication
    private val drinkRepository : DrinkRepository = drinkWiseApplication.drinkRepository
    val drinks: StateFlow<List<Drink>> = drinkRepository.drinks
        ?.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        ) ?: MutableStateFlow(emptyList())

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
}
