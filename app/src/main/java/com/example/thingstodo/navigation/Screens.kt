package com.example.thingstodo.navigation

import androidx.navigation.NavController
import com.example.thingstodo.util.Actions
import com.example.thingstodo.util.Constants.LIST_SCREEN
import com.example.thingstodo.util.Constants.SPLASH_SCREEN

class Screens(navController: NavController) {
    val splash: () -> Unit = {
        navController.navigate(route = "list/${Actions.NO_ACTION.name}") {
            popUpTo(SPLASH_SCREEN) {inclusive = true}
        }
    }
    val list: (Actions) -> Unit = {actions ->
        navController.navigate("list/${actions.name}") {
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }
    val task: (Int) -> Unit = {id ->
        navController.navigate("task/${id}")
    }
}