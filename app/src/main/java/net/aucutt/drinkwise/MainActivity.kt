package net.aucutt.drinkwise

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.MainActivity.Companion.TAG
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme

class MainActivity : ComponentActivity() {
    val drinkViewModel : DrinkViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DrinkWiseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Design(
                        modifier = Modifier.padding(innerPadding),
                        drinkModel = drinkViewModel,
                    )
                }
            }
        }
    }

    companion object {
         const val TAG = "DrinkWise"
    }
}

@Composable
fun Design(
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {
    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    var idx = "0"
    for (drink in drinks) {
        println("Drink ${drink.date}")
        Log.d(TAG, "Drink ${drink.date}")
        idx = drink.id
    }
    Log.d(TAG, "Number of drinks ${drinks.size}")

    LaunchedEffect(Unit) {
        val idxAsInt = idx.toIntOrNull() ?: 0
        val drink = Drink(
            (idxAsInt + 1).toString(),
            date = System.currentTimeMillis(),
            volume = 12.0f,
            abv = 5.0f,
        )
        drinkModel.addDrink(drink)
    }

    DesignContent(
        drinksCount = drinks.size,
        modifier = modifier,
        onAddDrink = {
            val drink = Drink(
                id = (drinks.size + 1).toString(),
                date = System.currentTimeMillis(),
                volume = 12f,
                abv = 4.8f
            )
            drinkModel.addDrink(drink)
        },
        onResetDrinks = {
            drinkModel.resetDrinks()
        }
    )
}

// Stateless composable separated for preview and state hoisting
@Composable
fun DesignContent(
    drinksCount: Int,
    modifier: Modifier = Modifier,
    onAddDrink: () -> Unit = {},
    onResetDrinks: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = "Drink count: $drinksCount"
        )
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
fun DesignPreview() {
    DrinkWiseTheme {
        // Use stateless composable in Preview to avoid ViewModel instantiation issues
        DesignContent(
            drinksCount = 0
        )
    }
}
