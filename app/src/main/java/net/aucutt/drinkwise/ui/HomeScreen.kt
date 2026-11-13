package net.aucutt.drinkwise.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.MainActivity
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme

@Composable
fun HomeScreen(
    onAddDrink: () -> Unit,
    onListDrinks: () -> Unit,
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {
    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    for (drink in drinks) {
        Log.d(MainActivity.TAG, "Drink ${drink.date}")
    }
    Log.d(MainActivity.TAG, "Number of drinks ${drinks.size}")

    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { padding ->
        HomeScreenContent(
            drinksCount = drinks.size,
            modifier = Modifier
                .padding(padding),
            onAddDrink = onAddDrink,
            onResetDrinks = onListDrinks
        )
    }
}

// Stateless composable separated for preview and state hoisting
@Composable
fun HomeScreenContent(
    drinksCount: Int,
    modifier: Modifier = Modifier,
    onAddDrink: () -> Unit = {},
    onResetDrinks: () -> Unit = {},
    drinkModel: DrinkViewModel = viewModel()
) {
    Column(modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Drink count: $drinksCount"
        )
        Button(
            onClick = onResetDrinks,
            content = { Text(text = "List Drinks") }
        )
        Button(
            onClick = onAddDrink,
            content = { Text(text = "Add a drink") },
        )
        Button(
            onClick = {drinkModel.generateTestData()},
            content = { Text(text = "Generate test data") },
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