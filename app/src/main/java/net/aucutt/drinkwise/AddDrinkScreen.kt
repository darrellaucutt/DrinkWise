package net.aucutt.drinkwise

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme

@Composable
fun AddDrinkScreen(
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {
    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    for (drink in drinks) {
        Log.d(MainActivity.TAG, "Drink ${drink.date}")
    }
    Log.d(MainActivity.TAG, "Number of drinks ${drinks.size}")

    AddDrinkScreenContent(
        modifier = modifier,
        onAddDrink = {
            val drink = Drink(
                date = System.currentTimeMillis(),
                volume = 12f,
                abv = 4.8f,
            )
            drinkModel.addDrink(drink)
        },
        onResetDrinks = {
            drinkModel.resetDrinks()
        }
    )
}

@Composable
fun AddDrinkScreenContent(
    modifier: Modifier = Modifier,
    onAddDrink: () -> Unit = {},
    onResetDrinks: () -> Unit = {}
) {
    Column(modifier = modifier) {

        Button(
            onClick = onAddDrink,
            content = { Text(text = "Add a drink") },
        )
        Button(
            onClick = onResetDrinks,
            content = { Text(text = "Reset drinks") }
        )
    }
}

@Preview
@Composable
fun AddDrinkScreenPreview() {
    DrinkWiseTheme {
        // Use stateless composable in Preview to avoid ViewModel instantiation issues
        AddDrinkScreenContent()
    }
}