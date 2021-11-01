package com.example.thingstodo.navigation.destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.thingstodo.ui.components.tasks.TaskScreen
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.Constants.TASK_ARG_KEY
import com.example.thingstodo.util.Constants.TASK_SCREEN

@ExperimentalAnimationApi
fun NavGraphBuilder.taskComposable(navigationToListScreen: (action: Actions) -> Unit, sharedViewModel: SharedViewModel) {
    composable(
        enterTransition = {_, _ ->
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 300
                )
            )
        },
        route = TASK_SCREEN,
        arguments = listOf(navArgument(TASK_ARG_KEY) {
        type = NavType.IntType
    })
    ) {navBackStackEntry ->
        val taskId = navBackStackEntry.arguments?.getInt(TASK_ARG_KEY)
        LaunchedEffect(key1 =taskId) {
            taskId?.let { sharedViewModel.getSelectedTask(taskId = it) }
        }
        val selectedTask by sharedViewModel.selectedTask.collectAsState()
        LaunchedEffect(key1 = selectedTask) {
            if (selectedTask != null || taskId == -1) {
                sharedViewModel.updateTaskField(selectedTask)
            }
        }
        TaskScreen(
            selectedTask,
            sharedViewModel,
            navigateToListScreen = navigationToListScreen
        )
    }
}