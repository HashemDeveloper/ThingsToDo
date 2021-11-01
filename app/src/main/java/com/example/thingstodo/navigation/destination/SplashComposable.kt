package com.example.thingstodo.navigation.destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.example.thingstodo.ui.splash.SplashScreen
import com.example.thingstodo.util.Constants

@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.splashComposable(
    navigateToTaskScreen: () -> Unit
) {
    composable(
        route = Constants.SPLASH_SCREEN,
        exitTransition = {_, _ ->
            slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(2000)
            )
        }
    ) {
        SplashScreen(navigateToTaskScreen = navigateToTaskScreen)
    }
}