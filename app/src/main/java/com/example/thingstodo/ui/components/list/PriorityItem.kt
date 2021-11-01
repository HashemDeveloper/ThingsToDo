package com.example.thingstodo.ui.components.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thingstodo.data.model.Priority
import com.example.thingstodo.ui.theme.LARGE_PADDING
import com.example.thingstodo.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.thingstodo.ui.theme.Typography

@Composable
fun PriorityItem(priority: Priority) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(PRIORITY_INDICATOR_SIZE)) {
            drawCircle(color = priority.color)
        }
        Text(modifier = Modifier.padding(LARGE_PADDING),
            text = priority.name,
            style = Typography.subtitle2,
            color = MaterialTheme.colors.onSurface)
    }
}
@Composable
@Preview
fun PriorityItemPreview() {
    PriorityItem(priority = Priority.MEDIUM)
}