package com.example.thingstodo.data

import androidx.room.*
import com.example.thingstodo.data.model.ToDoTask
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("select * from todo_table order by id asc")
    fun getAllTasks(): Flow<List<ToDoTask>>

    @Query("select * from todo_table where id=:id")
    fun getSelectedTask(id: Int): Flow<ToDoTask>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addATask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("delete from todo_table")
    suspend fun deleteAllTask()

    @Query("select * from todo_table where title like :query or `details`like :query")
    fun search(query: String): Flow<List<ToDoTask>>

    @Query("select * from todo_table order by case when priority like 'L%' then 1 when priority like 'M%' then 2 when priority like 'H%' then 3 end")
    fun sortByLowPriority(): Flow<List<ToDoTask>>
    @Query("select * from todo_table order by case when priority like 'H%' then 1 when priority like 'M%' then 2 when priority like 'L%' then 3 end")
    fun sortByHighPriority(): Flow<List<ToDoTask>>
    @Query("select * from todo_table order by case when priority like 'M%' then 1 when priority like 'H%' then 2 when priority like 'L%' then 3 end")
    fun sortByMediumPriority(): Flow<List<ToDoTask>>
}