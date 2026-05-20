package com.example.kashtakala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kashtakala.ui.catalog.CatalogScreen
import com.example.kashtakala.ui.ai.AiToolsScreen
import com.example.kashtakala.ui.estimator.EstimatorScreen
import com.example.kashtakala.ui.portfolio.PortfolioScreen
import com.example.kashtakala.ui.quote.QuoteScreen
import com.example.kashtakala.ui.theme.KashtaKalaTheme

sealed class Screen(val route: String, val label: String) {
    object Catalog   : Screen("catalog",   "Catalog")
    object Estimator : Screen("estimator", "Estimator")
    object Quote     : Screen("quote",     "Quotes")
    object Portfolio : Screen("portfolio", "Portfolio")
    object Ai        : Screen("ai",        "AI")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KashtaKalaTheme {
                val navController = rememberNavController()
                val items = listOf(
                    Triple(Screen.Catalog,   Icons.Filled.Home,  "Catalog"),
                    Triple(Screen.Estimator, Icons.Filled.Settings, "Estimator"),
                    Triple(Screen.Quote,     Icons.Filled.Info,  "Quotes"),
                    Triple(Screen.Portfolio, Icons.Filled.AccountBox,"Portfolio"),
                    Triple(Screen.Ai,        Icons.Filled.Info,  "AI"),
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFF4A2C0A)
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { (screen, icon, label) ->
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label) },
                                    selected = currentDestination?.hierarchy?.any {
                                        it.route == screen.route
                                    } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor   = Color(0xFFE8A44A),
                                        selectedTextColor   = Color(0xFFE8A44A),
                                        unselectedIconColor = Color(0xFFD4956A),
                                        unselectedTextColor = Color(0xFFD4956A),
                                        indicatorColor      = Color(0xFF8B5E3C)
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Catalog.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Catalog.route)   { CatalogScreen() }
                        composable(Screen.Estimator.route) { EstimatorScreen() }
                        composable(Screen.Quote.route)     { QuoteScreen() }
                        composable(Screen.Portfolio.route) { PortfolioScreen() }
                        composable(Screen.Ai.route)        { AiToolsScreen() }
                    }
                }
            }
        }
    }
}
