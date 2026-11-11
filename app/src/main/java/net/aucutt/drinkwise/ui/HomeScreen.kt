package net.aucutt.drinkwise.ui

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
import net.aucutt.drinkwise.MainActivity
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme

@Composable
fun HomeScreen(
    onAddDrink: () -> Unit,
    modifier: Modifier = Modifier.Companion,
    drinkModel: DrinkViewModel = viewModel()
) {
    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    for (drink in drinks) {
        Log.d(MainActivity.TAG, "Drink ${drink.date}")
    }
    Log.d(MainActivity.TAG, "Number of drinks ${drinks.size}")

    HomeScreenContent(
        drinksCount = drinks.size,
        modifier = modifier,
        onAddDrink = onAddDrink,
        onResetDrinks = {
            drinkModel.resetDrinks()
        }
    )
}

// Stateless composable separated for preview and state hoisting
@Composable
fun HomeScreenContent(
    drinksCount: Int,
    modifier: Modifier = Modifier.Companion,
    onAddDrink: () -> Unit = {},
    onResetDrinks: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = "Drink count: $drinksCount"
        )
        Button(
            onClick = onResetDrinks,
            content = { Text(text = "Reset drinks") }
        )
        Button(
            onClick = onAddDrink,
            content = { Text(text = "Add a drink") },
        )

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    DrinkWiseTheme {
        // Use stateless composable in Preview to avoid ViewModel instantiation issues
        HomeScreenContent(
            drinksCount = 0
        )
    }
}