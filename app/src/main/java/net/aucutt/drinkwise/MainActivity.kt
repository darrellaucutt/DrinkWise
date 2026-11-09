package net.aucutt.drinkwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import net.aucutt.drinkwise.data.DrinkViewModel
import net.aucutt.drinkwise.ui.theme.DrinkWiseTheme


@Serializable
private data object HomeRoute

@Serializable
private data object AddWineRoute


class MainActivity : ComponentActivity() {
    val drinkViewModel : DrinkViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DrinkWiseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
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

