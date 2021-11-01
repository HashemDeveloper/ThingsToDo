package com.example.thingstodo.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.data.model.ToDoTask
import com.example.thingstodo.data.repo.LocalDataStore
import com.example.thingstodo.data.repo.TodoRepo
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.RequestState
import com.example.thingstodo.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repo: TodoRepo, private val localStoreRepo: LocalDataStore): ViewModel() {
    val id: MutableState<Int> = mutableStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)
    val action: MutableState<Actions> = mutableStateOf(Actions.NO_ACTION)


    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    private val _searchTask = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchTask: StateFlow<RequestState<List<ToDoTask>>> = _searchTask

    private val _dismissDialogState = MutableStateFlow(false)
    val dismissDialogState: StateFlow<Boolean> = _dismissDialogState

    fun dismissAlertDialogFromState(value: Boolean) {
        _dismissDialogState.value = value
    }
    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState
    val lowPriorityTask: StateFlow<List<ToDoTask>> = repo.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    val highPriorityTask: StateFlow<List<ToDoTask>> = repo.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO) {
                localStoreRepo.readSortState.map {
                    Priority.valueOf(it)
                }.collect {
                    _sortState.value = RequestState.Success(it)
                }
            }
        } catch (ex: Exception) {
            _sortState.value = RequestState.Error(ex)
        }
    }

    fun persistSortingState(priority: Priority) {
        viewModelScope.launch(Dispatchers.IO) {
            localStoreRepo.persistSortState(priority = priority)
        }
    }
    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repo.addTask(toDoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }
    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repo.updateTask(toDoTask)
        }
    }
    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            repo.deleteTask(toDoTask)
        }
    }
    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
        }
    }
    fun handleDatabaseAction(actions: Actions) {
        when(actions) {
            Actions.ADD -> {
                addTask()
            }
            Actions.UPDATE -> {
                updateTask()
            }
            Actions.DELETE -> {
                deleteTask()
            }
            Actions.DELETE_ALL -> {
                deleteAll()
            }
            Actions.UNDO -> {
                addTask()
            }
            else -> {

            }
        }
    }
    fun searchDataBase(query: String) {
        _searchTask.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repo.search(query = "%$query%").collect {
                    _searchTask.value = RequestState.Success(it)
                }
            }
        } catch (ex: Exception) {
            _searchTask.value = RequestState.Error(ex)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }
    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repo.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (ex: Exception) {
            this._allTasks.value = RequestState.Error(ex)
        }
    }
    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repo.getSelectedTask(taskId).collect {task ->
                _selectedTask.value = task
            }
        }
    }
    fun updateTaskField(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            this.id.value = selectedTask.id
            this.title.value = selectedTask.title
            this.description.value = selectedTask.description
            this.priority.value = selectedTask.priority
        } else {
            this.id.value = 0
            this.title.value = ""
            this.description.value = ""
            this.priority.value = Priority.LOW
        }
    }
    fun validateField(): Boolean {
        return this.title.value.isNotEmpty() && this.description.value.isNotEmpty()
    }
}