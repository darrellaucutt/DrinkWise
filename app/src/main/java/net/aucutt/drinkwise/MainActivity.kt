package net.aucutt.drinkwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.AddDrinkScreen
import net.aucutt.drinkwise.ui.HomeScreen
import net.aucutt.drinkwise.ui.ListDrinksScreen
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme


@Serializable
private data object HomeRoute

@Serializable
private data object AddDrinkRoute

@Serializable
private data object ListDrinksRoute
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DrinkWiseTheme {
                DrinkWiseApp()
            }
        }
    }

    @Composable
    private fun DrinkWiseApp(drinkViewModel : DrinkViewModel = viewModel()) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute> {
                HomeScreen(
                    { navController.navigate(AddDrinkRoute) },
                    onListDrinks = {navController.navigate(ListDrinksRoute)},
                    drinkModel = drinkViewModel
                )
            }
            composable<AddDrinkRoute> {
                AddDrinkScreen(
                    onBack =  { navController.popBackStack() }
                )
            }
            composable<ListDrinksRoute> {
                ListDrinksScreen(
                    onBack =  { navController.popBackStack() }
                )
            }
        }
    }


    companion object {
         const val TAG = "DrinkWise"  //TODO move this to something more global
    }
}

