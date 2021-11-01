package com.example.thingstodo.ui.components.tasks

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.thingstodo.R
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.data.model.ToDoTask
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Actions

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (actions: Actions) -> Unit
) {
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority
    val context = LocalContext.current
    BackHandler(
        onBackPressed = { navigateToListScreen(Actions.NO_ACTION) }
    )
    Scaffold(
        topBar = {
            TaskAppBar(selectedTask, navigateToListScreen = {action ->
                when {
                    action == Actions.NO_ACTION -> {
                        navigateToListScreen(action)
                    }
                    sharedViewModel.validateField() -> {
                        navigateToListScreen(action)
                    }
                    else -> {
                        displayToast(context = context)
                    }
                }
            })
        },
        content = {
            TaskContent(
                title = title,
                onTitleChanged = {
                    sharedViewModel.title.value =it
                },
                description = description,
                onDescriptionChanged = {
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    sharedViewModel.priority.value = it
                }
            )
        }
    )
}
private fun displayToast(context: Context) {
    Toast.makeText(context, context.getString(R.string.empty_fields), Toast.LENGTH_SHORT).show()
}
@Composable
private fun BackHandler(
    onBackPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    val currentOnBackPressed by rememberUpdatedState(
        newValue = onBackPressed
    )
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    DisposableEffect(key1 = onBackPressedDispatcher) {
        onBackPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}