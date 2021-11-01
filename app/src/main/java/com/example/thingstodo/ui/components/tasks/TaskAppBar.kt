package com.example.thingstodo.ui.components.tasks

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.thingstodo.R
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.data.model.ToDoTask
import com.example.thingstodo.ui.components.common.ToDoAlertDialog
import com.example.thingstodo.ui.theme.topAppBarContentColor
import com.example.thingstodo.ui.theme.topBarBackGroundColor
import com.example.thingstodo.util.Actions

@Composable
fun TaskAppBar(selectedTask: ToDoTask?, navigateToListScreen: (action: Actions) -> Unit) {
    if (selectedTask == null) {
        NewTaskAppBar(navigateToListScreen = navigateToListScreen)
    } else {
        ExistingTaskAppBar(
            selectedTask = selectedTask,
            navigateToListScreen = navigateToListScreen
        )
    }
}
@Composable
private fun NewTaskAppBar(navigateToListScreen: (action: Actions) -> Unit) {
    TopAppBar(
        navigationIcon = {
            BackAction(onBackClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = stringResource(R.string.add_task),
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        backgroundColor = MaterialTheme.colors.topBarBackGroundColor,
        actions = {
            AddAction(onAddClicked = navigateToListScreen)
        }
    )
}
@Composable
private fun ExistingTaskAppBar(selectedTask: ToDoTask, navigateToListScreen: (action: Actions) -> Unit) {
    TopAppBar(
        navigationIcon = {
            CloseAction(onCloseClicked = navigateToListScreen)
        },
        title = {
            Text(
                text = selectedTask.title,
                color = MaterialTheme.colors.topAppBarContentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        backgroundColor = MaterialTheme.colors.topBarBackGroundColor,
        actions = {
            DisplayAlertDialog(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
            UpdateAction(onUpdateClicked = navigateToListScreen)
        }
    )
}
@Composable
private fun DisplayAlertDialog(selectedTask: ToDoTask, navigateToListScreen: (action: Actions) -> Unit) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    ToDoAlertDialog(
        title = stringResource(id = R.string.delete_task, selectedTask.title),
        message = stringResource(id = R.string.delete_confirmation, selectedTask.title),
        open = openDialog,
        onClose = {
            openDialog = false
        },
        onClick = {
            navigateToListScreen(Actions.DELETE)
        }
    )
    DeleteAction(onDeleteClicked = {
        openDialog = true
    })
}
@Composable
private fun CloseAction(onCloseClicked: (actions: Actions) -> Unit) {
    IconButton(onClick = { onCloseClicked(Actions.NO_ACTION)}) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(R.string.close_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}
@Composable
private fun DeleteAction(onDeleteClicked: () -> Unit) {
    IconButton(onClick = { onDeleteClicked()} ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}
@Composable
private fun UpdateAction(onUpdateClicked: (actions: Actions) -> Unit) {
    IconButton(onClick = { onUpdateClicked(Actions.UPDATE) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.update_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}
@Composable
private fun BackAction(onBackClicked: (action: Actions) -> Unit) {
    IconButton(onClick = { onBackClicked(Actions.NO_ACTION)}) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.arrow_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}
@Composable
private fun AddAction(onAddClicked: (actions: Actions) -> Unit) {
    IconButton(
        onClick = {onAddClicked(Actions.ADD) }) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.add_icon)
        )
    }
}

@Composable
@Preview
fun PreviewNewTaskAppBar() {
    NewTaskAppBar({})
}
@Composable
@Preview
fun PreviewExistingAppBar() {
    ExistingTaskAppBar(
        selectedTask = ToDoTask(1, "Hi", "Hello", Priority.MEDIUM),
        navigateToListScreen = {}
    )
}