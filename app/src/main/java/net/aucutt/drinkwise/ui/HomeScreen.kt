package net.aucutt.drinkwise.ui

import android.text.Layout.Alignment
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.MainActivity
import net.aucutt.drinkwise.R
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.math.StandardDrink
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme
import kotlin.time.Duration.Companion.days
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle

@Composable
fun HomeScreen(
    onAddDrink: () -> Unit,
    onListDrinks: () -> Unit,
    onGenerateTestData: () -> Unit,
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
            drinks = drinks,
            modifier = Modifier
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            onAddDrink = onAddDrink,
            onResetDrinks = onListDrinks,
            onGenerateTestData = onGenerateTestData
        )
    }
}

// Stateless composable separated for preview and state hoisting
@Composable
fun HomeScreenContent(
    drinks: List<Drink>,
    modifier: Modifier = Modifier,
    onAddDrink: () -> Unit = {},
    onResetDrinks: () -> Unit = {},
    onGenerateTestData: () -> Unit = {}
) {
    val standardDrinks = StandardDrink.computeTotalForWeek(drinks)
    var isDataTextExpanded by remember { mutableStateOf(false) }
    var isData1TextExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_screen_week_totals, standardDrinks)

        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onResetDrinks,
                modifier = Modifier.weight(1f),
                content = { Text(text = "List Drinks") }
            )
            Button(
                onClick = onAddDrink,
                modifier = Modifier.weight(1f),
                content = { Text(text = "Add a drink") },
            )
        }
        Column(
            modifier = Modifier
                .clickable { isDataTextExpanded = !isDataTextExpanded }
                .animateContentSize() // Smooth height animation
        ) {
            Text(
                text =
                    when (isDataTextExpanded) {
                        true -> stringResource(R.string.home_screen_data)
                        false -> stringResource(R.string.home_screen_data_short)
                    },
                fontStyle = FontStyle.Italic
            )
        }
        Button(
            onClick = onGenerateTestData,
            content = { Text(text = "Generate test data") },
        )

    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    DrinkWiseTheme {
        // Use stateless composable in Preview to avoid ViewModel instantiation issues

        val drinks = listOf<Drink>(
            Drink(
                date = System.currentTimeMillis(),
                type = StandardDrink.DrinkType.WINE.displayText,
                drinkWeight = 1.1f
            ),
            Drink(
                date = System.currentTimeMillis() - 1.days.inWholeMilliseconds,
                type = StandardDrink.DrinkType.BEER.displayText,
                drinkWeight = 1.6f
            )
        )
        HomeScreenContent(
            drinks
        )
    }
}