package net.aucutt.drinkwise.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.MainActivity
import net.aucutt.drinkwise.MainActivity.Companion.TAG
import net.aucutt.drinkwise.R
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme

val drinkTypes = listOf("Wine", "Beer", "Hard Alcohol", "Cider", "Seltzer", "Other")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDrinkScreen(
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {
    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    for (drink in drinks) {
        Log.d(TAG, "Drink ${drink.date}")
    }
    Log.d(TAG, "Number of drinks ${drinks.size}")
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_drink_title)) }
            )
        }
    ) { innerPadding ->
        AddDrinkScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDrinkScreenContent(
    modifier: Modifier = Modifier,
    onAddDrink: () -> Unit = {},
    onResetDrinks: () -> Unit = {}
) {
    var selectedDrink by remember { mutableStateOf("") }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedDrink,
                onValueChange = { selectedDrink = it },
                label = { Text(stringResource(R.string.select_drink)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            if (expanded) {
                ExposedDropdownMenu(
                    expanded = true,
                    onDismissRequest = { expanded = false }
                ) {
                    drinkTypes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedDrink = option
                                expanded = false
                                Log.d(TAG, "Drink type $option")
                            }
                        )
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp) ) {
                Button(
                    onClick = onAddDrink,
                    modifier = Modifier.weight(1f),
                    content = { Text(text = stringResource(R.string.add_drink_compute)) },
                )
                Button(
                    onClick = onAddDrink,
                    modifier = Modifier.weight(1f),
                    content = { Text(text = "Add a drink") },
                )

        }
        Button(
            onClick = onResetDrinks,
            modifier = Modifier.fillMaxWidth(),
            content = { Text(text = "Reset drinks") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddDrinkScreenPreview() {
    DrinkWiseTheme {
        AddDrinkScreenContent(
            modifier = Modifier.padding(16.dp)
        )
    }
}