package com.example.thingstodo.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.thingstodo.navigation.destination.listComposable
import com.example.thingstodo.navigation.destination.splashComposable
import com.example.thingstodo.navigation.destination.taskComposable
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Constants.LIST_SCREEN
import com.example.thingstodo.util.Constants.SPLASH_SCREEN
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val screen = remember(navController) {
        Screens(navController)
    }
    AnimatedNavHost(navController = navController, startDestination = SPLASH_SCREEN) {
        // this is where we are defining our navigation graph
        splashComposable(
            navigateToTaskScreen = screen.splash
        )
        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
        taskComposable(navigationToListScreen = screen.list, sharedViewModel = sharedViewModel)
    }
}