package com.sukajee.studyhub.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sukajee.design.components.StudyHubBottomNavigation
import com.sukajee.design.components.BottomNavItem
import com.sukajee.feature.auth.presentation.login.LoginScreen
import com.sukajee.feature.auth.presentation.register.RegisterScreen
import com.sukajee.feature.home.presentation.HomeScreen
import com.sukajee.feature.library.presentation.LibraryScreen
import com.sukajee.feature.reader.presentation.ReaderScreen
import com.sukajee.feature.progress.presentation.ProgressScreen
import com.sukajee.feature.profile.presentation.ProfileScreen

private val bottomNavRoutes = listOf(
    Route.Home,
    Route.Library,
    Route.Progress,
    Route.Profile
)

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomNav = currentRoute?.let { route ->
        bottomNavRoutes.any { route.contains(it::class.qualifiedName ?: "") }
    } ?: false

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                StudyHubBottomNavigation(
                    currentRoute = currentRoute ?: "",
                    onItemSelected = { item ->
                        val route = when (item) {
                            BottomNavItem.HOME -> Route.Home
                            BottomNavItem.LIBRARY -> Route.Library
                            BottomNavItem.PROGRESS -> Route.Progress
                            BottomNavItem.PROFILE -> Route.Profile
                        }
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Login,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(300)) +
                        slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(300)) +
                        slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            }
        ) {
            composable<Route.Login> {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Route.Register) }
                )
            }

            composable<Route.Register> {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Route.Home) {
                            popUpTo(Route.Login) { inclusive = true }
                        }
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.Home> {
                HomeScreen(
                    onBookClick = { bookId -> navController.navigate(Route.Reader(bookId)) }
                )
            }

            composable<Route.Library> {
                LibraryScreen(
                    onBookClick = { bookId -> navController.navigate(Route.Reader(bookId)) }
                )
            }

            composable<Route.Reader> { backStackEntry ->
                val route: Route.Reader = backStackEntry.toRoute()
                ReaderScreen(
                    bookId = route.bookId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable<Route.Progress> {
                ProgressScreen()
            }

            composable<Route.Profile> {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Route.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
