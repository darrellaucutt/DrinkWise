package net.aucutt.drinkwise.ui

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import net.aucutt.drinkwise.MainActivity.Companion.TAG
import net.aucutt.drinkwise.R
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme
import kotlin.time.Duration.Companion.days

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDrinksScreen(
    onBack : () -> Unit,
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {


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
            drinkModel.trimDataByDate(cutOffDate)
            //Important to call out here, the drinks should hold the modified data thanks to Room being reactive.
        }

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

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDrinksScreenContent(
    modifier: Modifier = Modifier,
    drinkModel: DrinkViewModel = viewModel()
) {

    val drinks by drinkModel.drinks.collectAsStateWithLifecycle()
    if (drinks.isNotEmpty()) {
        var currentDate = drinks[0].getDay()

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row {
                    Text(
                        fontWeight = FontWeight.Bold,
                        text = drinks[0].getDay()
                    )
                }
            }

            for (drink in drinks) {
                if (!drink.getDay().contentEquals(currentDate)) {
                    currentDate = drink.getDay()
                    item {
                        Row {
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = drink.getDay()
                            )
                        }
                    }
                }
                item {

                    val dismissState = rememberSwipeToDismissBoxState(
                        SwipeToDismissBoxValue.Settled,
                        confirmValueChange  ={ dismissValue -> if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                            Log.d(TAG, "Delete the thing ${drink.getFormattedRow()}")
                            drinkModel.deleteById( drink.id)
                            true
                        }
                        else {
                            Log.d(TAG, "Don't Delete the thing ${drink.getFormattedRow()}")
                            false
                        }
                        }
                    )
                        SwipeToDismissBox( state = dismissState, backgroundContent = {
                            val color = when (dismissState.dismissDirection) {
                                SwipeToDismissBoxValue.EndToStart -> Color.Red
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(end = 16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.White
                                )
                            }
                        },
                            content =
                                { Row(modifier = Modifier.padding(start = 16.dp)) {
                                    Text(drink.getFormattedRow()) }
                                }
                        )

                }
            }
            item {
                Row {
                   Text( text = stringResource(R.string.list_drinks_disclaimer),
                       fontStyle = FontStyle.Italic)
                }
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ListDrinksScreenContentPreview() {
    DrinkWiseTheme {
        ListDrinksScreenContent(
            modifier = Modifier.padding(16.dp)
        )
    }
}