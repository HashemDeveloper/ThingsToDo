package com.example.thingstodo.ui.components.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.thingstodo.R
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.ui.components.common.ToDoAlertDialog
import com.example.thingstodo.ui.theme.LARGE_PADDING
import com.example.thingstodo.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.thingstodo.ui.theme.topAppBarContentColor
import com.example.thingstodo.ui.theme.topBarBackGroundColor
import com.example.thingstodo.ui.viewmodels.SharedViewModel
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.SearchAppBarState
import com.example.thingstodo.util.TrailingIconState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
) {
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                   sharedViewModel.searchAppBarState.value = SearchAppBarState.OPENED
                },
                onSortClicked ={
                    sharedViewModel.persistSortingState(priority = it)
                               },
                onDeleteAllClicked = { sharedViewModel.action.value = Actions.DELETE_ALL }
            )
        } else -> {
            SearchAppBar(
                query = searchTextState,
                onTextChanged = {
                    sharedViewModel.searchTextState.value = it
                },
                onCloseClicked = {
                    sharedViewModel.searchAppBarState.value = SearchAppBarState.CLOSED
                    sharedViewModel.searchTextState.value = ""
                },
                onSearchClicked = { query ->
                    sharedViewModel.searchDataBase(query = query)
                }
            )
        }
    }
}

@Composable
private fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (priority: Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.tasks), color = MaterialTheme.colors.topAppBarContentColor)
        },
        actions = {
            ListBarActions(
                onSearchClicked = onSearchClicked,
                onnSortClicked = onSortClicked,
                onDeleteAllClicked = onDeleteAllClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topBarBackGroundColor
    )
}

@Composable
private fun ListBarActions(
    onSearchClicked: () -> Unit,
    onnSortClicked: (priority: Priority) -> Unit,
    onDeleteAllClicked: () -> Unit
) {
    var openDialog by remember {
        mutableStateOf(false)
    }
    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onnSortClicked)

    ToDoAlertDialog(
        title = stringResource(id = R.string.delete_task, "All"),
        message = stringResource(id = R.string.delete_confirmation, "All"),
        open = openDialog,
        onClose = { openDialog = false},
        onClick = {
            openDialog = false
            onDeleteAllClicked()
        }
    )

    DeleteAllAction(onDeleteAllClicked = {
        openDialog = true
    })
}

@Composable
private fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = { onSearchClicked() }) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search_task),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
private fun SortAction(onSortClicked: (priority: Priority) -> Unit) {
    var isExpand by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { isExpand = true }) {
        Icon(
            painter = painterResource(id = R.drawable.filter_list_icon_24),
            contentDescription = stringResource(
                id = R.string.search_icon
            ),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(expanded = isExpand, onDismissRequest = {
            isExpand = false
        }) {
            DropdownMenuItem(onClick = {
                isExpand = false
                onSortClicked(Priority.LOW)
            }) {
                PriorityItem(priority = Priority.LOW)
            }
            DropdownMenuItem(onClick = {
                isExpand = false
                onSortClicked(Priority.HIGH)
            }) {
                PriorityItem(priority = Priority.HIGH)
            }
            DropdownMenuItem(onClick = {
                isExpand = false
                onSortClicked(Priority.NONE)
            }) {
                PriorityItem(priority = Priority.NONE)
            }
        }
    }
}

@Composable
private fun DeleteAllAction(onDeleteAllClicked: () -> Unit) {
    var isExpend by remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { isExpend = true }) {
        Icon(
            painter = painterResource(id = R.drawable.more_icon),
            contentDescription = stringResource(
                id = R.string.delete_all_icon
            ),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(expanded = isExpend, onDismissRequest = {
            isExpend = false
        }) {
            DropdownMenuItem(onClick = {
                isExpend = false
                onDeleteAllClicked()
            }) {
                Text(
                    modifier = Modifier.padding(start = LARGE_PADDING),
                    text = stringResource(R.string.delete_all),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}
@Composable
private fun SearchAppBar(
    query: String,
    onTextChanged: (value: String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (query: String) -> Unit
) {
    var trailingIconState by remember {
        mutableStateOf(TrailingIconState.READY_TO_DELETE)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topBarBackGroundColor
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = query,
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = {
                Text(modifier = Modifier.alpha(ContentAlpha.disabled), text = stringResource(R.string.search_placeholder), color = Color.White)
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.alpha(ContentAlpha.disabled),
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            }, trailingIcon = {
                IconButton(onClick = {
                    when (trailingIconState) {
                        TrailingIconState.READY_TO_DELETE -> {
                            trailingIconState = if (query.isNotEmpty()) {
                                onTextChanged("")
                                TrailingIconState.READY_TO_CLOSE
                            } else {
                                onCloseClicked()
                                TrailingIconState.READY_TO_DELETE
                            }
                        }
                        TrailingIconState.READY_TO_CLOSE -> {
                            if (query.isNotEmpty()) {
                                onTextChanged("")
                            } else {
                                onCloseClicked()
                                trailingIconState = TrailingIconState.READY_TO_DELETE
                            }
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.close_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor)
                }
            }, keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ), keyboardActions = KeyboardActions(
                onSearch = {onSearchClicked(query)}
            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppBar({}, {}, {})
}
@Composable
@Preview
private fun SearchAppBarPreview() {
    SearchAppBar(query = "Search", onTextChanged = {}, onCloseClicked = {}) {

    }
}