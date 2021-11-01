package com.example.thingstodo.data.model

import androidx.compose.ui.graphics.Color
import com.example.thingstodo.ui.theme.HighPriorityColor
import com.example.thingstodo.ui.theme.LowPriorityColor
import com.example.thingstodo.ui.theme.MediumPriorityColor
import com.example.thingstodo.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}