package com.example.thingstodo.data.repo

import com.example.thingstodo.data.ToDoDao
import com.example.thingstodo.data.model.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TodoRepo @Inject constructor(private val todoDao: ToDoDao) {
    val getAllTasks: Flow<List<ToDoTask>> = this.todoDao.getAllTasks()
    val sortByLowPriority: Flow<List<ToDoTask>> = this.todoDao.sortByLowPriority()
    val sortByHighPriority: Flow<List<ToDoTask>> = this.todoDao.sortByHighPriority()

    fun getSelectedTask(id: Int): Flow<ToDoTask> {
        return this.todoDao.getSelectedTask(id)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        this.todoDao.addATask(toDoTask)
    }
    suspend fun updateTask(toDoTask: ToDoTask) {
        this.todoDao.updateTask(toDoTask)
    }
    suspend fun deleteTask(toDoTask: ToDoTask) {
        this.todoDao.deleteTask(toDoTask)
    }
    suspend fun deleteAll() {
        this.todoDao.deleteAllTask()
    }
    fun search(query: String): Flow<List<ToDoTask>> {
        return this.todoDao.search(query)
    }
}