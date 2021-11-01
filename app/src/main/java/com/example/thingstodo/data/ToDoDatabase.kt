package com.example.thingstodo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.thingstodo.data.model.ToDoTask

@Database(entities = [ToDoTask::class], version = 1, exportSchema = false)
abstract class ToDoDatabase: RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}