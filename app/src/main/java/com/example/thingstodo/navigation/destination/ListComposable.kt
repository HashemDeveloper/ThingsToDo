package com.example.thingstodo.navigation.destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.thingstodo.ui.components.list.ListScreen
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.Constants.LIST_ARG_KEY
import com.example.thingstodo.util.Constants.LIST_SCREEN
import com.example.thingstodo.util.toAction

@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (id: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(route = LIST_SCREEN, arguments = listOf(navArgument(LIST_ARG_KEY) {
        type = NavType.StringType
    })){navBackStack ->
        val action: Actions = navBackStack.arguments?.getString(LIST_ARG_KEY).toAction()
        LaunchedEffect(key1 = action) {
            sharedViewModel.action.value = action
        }
        val databaseAction by sharedViewModel.action
        ListScreen(databaseAction, navigateToTaskScreen = navigateToTaskScreen, sharedViewModel = sharedViewModel)
    }
}