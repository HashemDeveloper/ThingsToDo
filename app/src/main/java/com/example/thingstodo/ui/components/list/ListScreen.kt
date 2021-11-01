package com.example.thingstodo.ui.components.list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.thingstodo.R
import com.example.thingstodo.data.model.DialogItems
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.data.model.ToDoTask
import com.example.thingstodo.ui.components.common.ToDoAlertDialog
import com.example.thingstodo.ui.theme.fabBackgroundColor
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.SearchAppBarState
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListScreen(action: Actions, navigateToTaskScreen: (id: Int) -> Unit, sharedViewModel: SharedViewModel) {
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchTask by sharedViewModel.searchTask.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTask by sharedViewModel.lowPriorityTask.collectAsState()
    val highPriorityTask by sharedViewModel.highPriorityTask.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val defaultTask = DialogItems(false, Actions.NO_ACTION, ToDoTask(-1, "", "", Priority.NONE))
    var dialogState by remember {
        mutableStateOf(defaultTask)
    }
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }
    LaunchedEffect(key1 = action) {
        sharedViewModel.handleDatabaseAction(action)
    }

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete= { sharedViewModel.action.value = it },
        onUndoClicked = { sharedViewModel.action.value = it },
        taskTitle = sharedViewModel.title.value,
        action = action
    )
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                searchTask,
                allTasks,
                navigateToTaskScreen,
                searchAppBarState, sortState, lowPriorityTask, highPriorityTask,
                onSwipeToDelete = { actions, toDoTask, dismissState ->
                    val newDialogState = DialogItems(true, actions = actions, toDoTask, dismissState)
                    dialogState = newDialogState
                },
                sharedViewModel = sharedViewModel
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
    val item = dialogState.item
    ToDoAlertDialog(
        title = stringResource(R.string.delete_task, item.title),
        message = stringResource(R.string.delete_all, item.title),
        open = dialogState.isOpen,
        onClose = {
            sharedViewModel.dismissAlertDialogFromState(true)
            dialogState = defaultTask
        },
        onClick = {
            sharedViewModel.action.value = dialogState.actions
            sharedViewModel.updateTaskField(selectedTask = item)
            dialogState = defaultTask
        }
    )
}
@Composable
fun ListFab(onFabClicked: (id: Int) -> Unit) {
    FloatingActionButton(onClick = {
        onFabClicked(-1)
    }, backgroundColor = MaterialTheme.colors.fabBackgroundColor) {
        Icon(imageVector = Icons.Filled.Add,
            contentDescription = stringResource(id = R.string.add_button),
            tint = Color.White)
    }
}
@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    onComplete:(Actions) -> Unit,
    onUndoClicked: (Actions) -> Unit,
    taskTitle: String,
    action: Actions
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Actions.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setSnackBarMessage(action, taskTitle),actionLabel = setSnackBarLabel(action = action)
                )
                undoDeletedTask(action = action, snackBarResult = snackBarResult) {
                    onUndoClicked(it)
                }
            }
            onComplete(Actions.NO_ACTION)
        }
    }
}
private fun setSnackBarMessage(action: Actions, title: String): String {
    return when (action) {
        Actions.DELETE_ALL -> {
            "All Tasks Removed"
        } else -> "${action.name}: $title"
    }
}
private fun setSnackBarLabel(action: Actions): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}
private fun undoDeletedTask(
    action: Actions,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Actions)-> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Actions.DELETE) {
        onUndoClicked(Actions.UNDO)
    }
}