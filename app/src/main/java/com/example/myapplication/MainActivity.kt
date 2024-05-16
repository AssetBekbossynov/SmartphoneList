package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.screens.detailsScreen.SmartphoneDetailScreen
import com.example.myapplication.ui.screens.smartphoneList.MainScreen
import com.example.myapplication.ui.screens.smartphoneList.MainScreenViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    AppNavHost(navController, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier) {
    val viewModel: MainScreenViewModel = koinViewModel()
    NavHost(
        navController,
        modifier = modifier.fillMaxSize(),
        startDestination = "main"
    ) {

        composable("main") {
            MainScreen(viewModel) {
                navController.navigate("detail/${it}")
            }
        }
        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            SmartphoneDetailScreen(viewModel, smartphoneId = id)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}