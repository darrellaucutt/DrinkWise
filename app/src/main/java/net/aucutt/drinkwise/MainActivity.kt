package net.aucutt.drinkwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme


@Serializable
private data object HomeRoute

@Serializable
private data object AddDrinkRoute


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
                    {navController.navigate(AddDrinkRoute)},
                    drinkModel = drinkViewModel
                )
            }
            composable<AddDrinkRoute> {
                AddDrinkScreen(
                    drinkModel = drinkViewModel
                )
            }
        }
    }


    companion object {
         const val TAG = "DrinkWise"  //TODO move this to something more global
    }
}

