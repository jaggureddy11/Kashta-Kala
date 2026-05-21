package com.example.kashtakala

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kashtakala.ui.SharedViewModel
import com.example.kashtakala.ui.catalog.CatalogScreen
import com.example.kashtakala.ui.ai.AiToolsScreen
import com.example.kashtakala.ui.estimator.EstimatorScreen
import com.example.kashtakala.ui.portfolio.PortfolioScreen
import com.example.kashtakala.ui.quote.QuoteScreen
import com.example.kashtakala.ui.theme.KashtaKalaTheme
import com.example.kashtakala.ui.splash.SplashScreen
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.collectAsState
import com.example.kashtakala.util.TranslationHelper

sealed class Screen(val route: String, val label: String) {
    object Splash    : Screen("splash",    "Splash")
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
                val sharedViewModel: SharedViewModel = viewModel()
                val currentLang by sharedViewModel.selectedLanguage.collectAsState()
                
                val items = listOf(
                    Triple(Screen.Catalog,   Icons.Filled.Home,         "Catalog"),
                    Triple(Screen.Estimator, Icons.Filled.Calculate,    "Estimator"),
                    Triple(Screen.Quote,     Icons.Filled.RequestQuote, "Quotes"),
                    Triple(Screen.Portfolio, Icons.Filled.PhotoLibrary, "Portfolio"),
                    Triple(Screen.Ai,        Icons.Filled.AutoAwesome,  "AI"),
                )

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val showBottomBar = currentDestination?.route != Screen.Splash.route

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
                                containerColor = Color(0xFF4A2C0A)
                            ) {
                                items.forEach { (screen, icon, label) ->
                                    val localizedLabel = when(screen) {
                                        Screen.Catalog -> TranslationHelper.getString("nav_catalog", currentLang)
                                        Screen.Estimator -> TranslationHelper.getString("nav_estimator", currentLang)
                                        Screen.Quote -> TranslationHelper.getString("nav_quotes", currentLang)
                                        Screen.Portfolio -> TranslationHelper.getString("nav_portfolio", currentLang)
                                        Screen.Ai -> TranslationHelper.getString("nav_ai", currentLang)
                                        else -> label
                                    }
                                    NavigationBarItem(
                                        icon = { Icon(icon, contentDescription = localizedLabel) },
                                        label = { Text(localizedLabel) },
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
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding),
                        enterTransition = { slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400)) },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400)) }
                    ) {
                        composable(
                            route = Screen.Splash.route,
                            exitTransition = { fadeOut(animationSpec = tween(600)) }
                        ) {
                            SplashScreen(onNavigateToHome = {
                                navController.navigate(Screen.Catalog.route) {
                                    popUpTo(Screen.Splash.route) { inclusive = true }
                                }
                            })
                        }
                        composable(
                            route = Screen.Catalog.route,
                            enterTransition = { fadeIn(animationSpec = tween(600)) }
                        ) {
                            CatalogScreen(
                                sharedViewModel = sharedViewModel,
                                onNavigateToScreen = { route ->
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                        composable(Screen.Estimator.route) { EstimatorScreen(sharedViewModel = sharedViewModel) }
                        composable(Screen.Quote.route)     { QuoteScreen(sharedViewModel = sharedViewModel) }
                        composable(Screen.Portfolio.route) { PortfolioScreen(sharedViewModel = sharedViewModel) }
                        composable(Screen.Ai.route)        { AiToolsScreen(sharedViewModel = sharedViewModel) }
                    }
                }
            }
        }
    }
}
