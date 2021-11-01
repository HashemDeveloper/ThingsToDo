package com.example.thingstodo.ui.components.common

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.thingstodo.R

@Composable
fun ToDoAlertDialog(
    title: String,
    message: String,
    open: Boolean,
    onClose: () -> Unit,
    onClick: () -> Unit
) {
    if (open) {
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                   Text(
                       text = message,
                       fontSize = MaterialTheme.typography.subtitle1.fontSize,
                       fontWeight = FontWeight.Normal
                   )
            },
            onDismissRequest = { onClose() },
            confirmButton = {
                Button(
                    onClick = {
                        onClick()
                        onClose()
                    }
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        onClose()
                    }
                ) {
                    Text(text = stringResource(R.string.no))
                }
            }
        )
    }
}