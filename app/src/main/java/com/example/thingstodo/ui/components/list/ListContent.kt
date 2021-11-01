package com.example.thingstodo.ui.components.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thingstodo.R
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.data.model.ToDoTask
import com.example.thingstodo.ui.theme.*
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.RequestState
import com.example.thingstodo.util.SearchAppBarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListContent(
    searchTask: RequestState<List<ToDoTask>>,
    allTasks: RequestState<List<ToDoTask>>,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    searchAppBarState: SearchAppBarState,
    sortState: RequestState<Priority>,
    lowPriorityTask: List<ToDoTask>,
    highPriorityTask: List<ToDoTask>,
    onSwipeToDelete: (Actions, ToDoTask, DismissState) -> Unit,
    sharedViewModel: SharedViewModel,
) {
    if (sortState is RequestState.Success) {
        when {
            searchAppBarState == SearchAppBarState.TRIGGERED -> {
                if (searchTask is RequestState.Success) {
                    HandleListContent(
                        tasks = searchTask.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
            sortState.data == Priority.NONE -> {
                if (allTasks is RequestState.Success) {
                    HandleListContent(
                        tasks = allTasks.data,
                        onSwipeToDelete = onSwipeToDelete,
                        navigateToTaskScreen = navigateToTaskScreen,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
            sortState.data == Priority.LOW -> {
                HandleListContent(
                    tasks = lowPriorityTask,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen,
                    sharedViewModel = sharedViewModel
                )
            }
            sortState.data == Priority.HIGH -> {
                HandleListContent(
                    tasks = highPriorityTask,
                    onSwipeToDelete = onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}
@Composable
fun RedBackground(degrees: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HighPriorityColor)
            .padding(LARGE_PADDING),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id = R.string.delete_icon),
            tint = Color.White
        )
    }
}
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun HandleListContent(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Actions, ToDoTask, DismissState) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    if (tasks.isEmpty()) {
        EmptyContent()
    } else {
        DisplayTasks(
            toDoTask = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun DisplayTasks(
    toDoTask: List<ToDoTask>,
    onSwipeToDelete: (Actions, ToDoTask, DismissState) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LazyColumn {
        items(
            items = toDoTask,
            key = {task ->
                task.id
            }
        ) { tasks ->
            val coroutineScope = rememberCoroutineScope()
            val dialogState by sharedViewModel.dismissDialogState.collectAsState()
            val dismissStat = rememberDismissState()
            val dismissDirection = dismissStat.dismissDirection
            val isDismissed = dismissStat.isDismissed(DismissDirection.EndToStart)
            LaunchedEffect(key1 = isDismissed) {
                launch(Dispatchers.IO) {
                    if (isDismissed && dismissDirection == DismissDirection.EndToStart) {
                        coroutineScope.launch {
                            delay(300)
                            onSwipeToDelete(Actions.DELETE, tasks, dismissStat)
                        }
                    }
                }
            }
            coroutineScope.launch {
                if (dialogState) {
                    dismissStat.reset()
                    sharedViewModel.dismissAlertDialogFromState(false)
                }
            }
            val degrees by animateFloatAsState(
                targetValue = if (dismissStat.targetValue == DismissValue.Default) 0f else -45f
            )
            var itemAppeared by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = true) {
                itemAppeared = true
            }
            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )
            ) {
                SwipeToDismiss(
                    state = dismissStat,
                    background = { RedBackground(degrees = degrees)},
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(0.2f)},
                    dismissContent = {
                        TaskItem(toDoTask = tasks, navigateToTaskScreen = navigateToTaskScreen)
                    }
                )
            }
        }
    }
}
@ExperimentalMaterialApi
@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    Surface(
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape, elevation = TASK_VALUE_ITEM,
        color = MaterialTheme.colors.taskItemBackgroundColor,
    ) {
        Column(
            modifier = Modifier
                .padding(LARGE_PADDING)
                .fillMaxWidth()
        ) {
            Row {
               Text(
                   modifier = Modifier.weight(8f),
                   text = toDoTask.title,
                   color = MaterialTheme.colors.taskItemTextColor,
                   style = MaterialTheme.typography.h5,
                   fontWeight = FontWeight.Bold,
                   maxLines = 1
               )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Canvas(
                        modifier = Modifier
                            .size(PRIORITY_INDICATOR_SIZE)) {
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    }
                }
            }
            Text(
                modifier= Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = MaterialTheme.colors.taskItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
@ExperimentalMaterialApi
@Composable
@Preview
fun PreviewListContent() {
    TaskItem(toDoTask = ToDoTask(1, "Hello", "What's up", Priority.HIGH), navigateToTaskScreen = {} )
}
@Composable
@Preview
fun PreviewRedBackground() {
    Column(modifier = Modifier.height(100.dp)) {
        RedBackground(degrees = 0f)
    }
}