package com.example.thingstodo.data.model

import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import com.example.thingstodo.util.Actions

@ExperimentalMaterialApi
data class  DialogItems<T> (
    var isOpen: Boolean = false,
    val actions: Actions,
    val item: T,
    val dismissState: DismissState?= DismissState(DismissValue.Default, { v -> false})
)
