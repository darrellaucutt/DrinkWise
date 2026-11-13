package net.aucutt.drinkwise.ui

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.MainActivity
import net.aucutt.drinkwise.MainActivity.Companion.TAG
import net.aucutt.drinkwise.R
import net.aucutt.drinkwise.data.Drink
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.math.StandardDrink
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDrinksScreen(
    onBack : () -> Unit,
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {

    //TODO filtering should be in viewmodel
    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    for (drink in drinks) {
        Log.d(TAG, "Drink ${drink.date}")
    }
    Log.d(TAG, "Number of drinks ${drinks.size}")
    if(drinks.isNotEmpty()) {
        //purge data older than a week.
        val lastDrink = drinks[drinks.size - 1]
        val cutOffDate = System.currentTimeMillis() - 7.days.inWholeMilliseconds
        if (lastDrink.date < cutOffDate) {
            //need to drop old date.
            drinkModel.trimDataByDate(cutOffDate)
        }
        for (drink in drinks) {
            Log.d(TAG, "Drink $drink")
        }
        Log.d(TAG, "Number of drinks ${drinks.size}")
        val filteredDrinks = drinks.filter { it.date > cutOffDate }.sortedBy { it.date }
        for (filteredDrink in filteredDrinks) {
            Log.d(TAG, "Filtered Drinks $filteredDrink")
        }
        Log.d(TAG, "Number of Filtered drinks ${filteredDrinks.size}")
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.list_drinks_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ListDrinksScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        )
    }
}

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDrinksScreenContent(
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {
    var selectedDrink by remember { mutableStateOf("Other") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var drinkAbv by remember { mutableStateOf("") }
    var drinkVolume  by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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
                  listOf(StandardDrink.DrinkType.entries).forEach { drinkType ->
                       drinkType.forEach { drink ->
                           drink.displayText
                           DropdownMenuItem(
                               text = { Text(drink.displayText) },
                               onClick = {
                                   selectedDrink = drink.displayText
                                   expanded = false
                                   Log.d(TAG, "Drink type $drink.displayText")
                               }
                           )
                       }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp) ) {
            OutlinedTextField( value = drinkAbv,
                onValueChange = { drinkAbv = it },
                label = { Text("ABV (%)") },
                placeholder = { Text("e.g. 5.0") },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                  singleLine = true
            )
            OutlinedTextField( value = drinkVolume,
                onValueChange = { drinkVolume = it },
                label = { Text("Ounces:") },
                placeholder = { Text("e.g. 12.0") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            val context = LocalContext.current
            Button(
                onClick = {
                    val standardDrinks = sanitizeAndCompute(
                        drinkAbv,
                        drinkVolume
                    )
                    if (selectedDrink.contentEquals(StandardDrink.DrinkType.OTHER.displayText)) {
                        selectedDrink = inferDrinkType(drinkAbv, drinkVolume).displayText
                    }
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.add_drink_compute_toast,
                            selectedDrink,
                            standardDrinks
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.weight(1f),
                content = { Text(text = stringResource(R.string.add_drink_compute)) },
                enabled = shouldButtonsBeClickable(drinkAbv, drinkVolume)
            )
            Button(
                onClick = {
                    val drinkWeight = sanitizeAndCompute(
                        drinkAbv,
                        drinkVolume
                    )
                    val newDrink = Drink( date=System.currentTimeMillis(),
                        type = selectedDrink,
                        drinkWeight = drinkWeight
                        )
                    drinkModel.addDrink(newDrink)
                    drinkAbv = ""
                    drinkVolume = ""
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.add_drink_save_toast,
                            selectedDrink,
                            drinkWeight
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.weight(1f),
                content = { Text(text = stringResource(R.string.add_drink_add)) },
                enabled = shouldButtonsBeClickable(drinkAbv, drinkVolume)
            )

        }

    }
}

private fun sanitizeAndCompute(abv: String, volume: String) : Float {
    val abvFloat = abv.toFloatOrNull()
    val volumeFloat = volume.toFloatOrNull()
    if( abvFloat == null || volumeFloat == null ) {
        return 0f
    } else {
        return StandardDrink.drinkWeight(abvFloat,volumeFloat)
    }
}
private fun inferDrinkType( abv: String, volume: String ) : StandardDrink.DrinkType {
    val abvFloat = abv.toFloatOrNull()
    val volumeFloat = volume.toFloatOrNull()
    if( abvFloat != null && volumeFloat != null ) {
        return StandardDrink.drinkInferer(abvFloat, volumeFloat)
    }
    return StandardDrink.DrinkType.OTHER
}

private fun shouldButtonsBeClickable(drinkAbv: String, drinkVolume: String) : Boolean {
    if( drinkAbv.contentEquals("") || drinkVolume.contentEquals("")) {
        return false
    }
    if( drinkAbv.toFloatOrNull() == null || drinkVolume.toFloatOrNull() == null) {
        return false //TODO separate this out and make it a friendly error
    }
    return true
}

@Preview(showBackground = true)
@Composable
fun ListDrinksScreenContentPreview() {
    DrinkWiseTheme {
        ListDrinksScreenContent(
            modifier = Modifier.padding(16.dp)
        )
    }
}