package com.example.thingstodo.util

enum class Actions {
    ADD,UPDATE,DELETE,DELETE_ALL,UNDO,NO_ACTION
}
fun String?.toAction(): Actions {
    return when {
        this == "ADD" -> {
            Actions.ADD
        }
        this == "UPDATE" -> {
            Actions.UPDATE
        }
        this == "DELETE" -> {
            Actions.DELETE
        }
        this == "DELETE_ALL" -> {
            Actions.DELETE_ALL
        }
        this == "UNDO" -> {
            Actions.UNDO
        }
        else -> Actions.NO_ACTION
    }
}