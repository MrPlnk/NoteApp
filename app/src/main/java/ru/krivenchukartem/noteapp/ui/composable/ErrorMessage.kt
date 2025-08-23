package ru.krivenchukartem.noteapp.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorMessage(
    errorMessage: String,
){
    Card(
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(errorMessage)
        }
    }
}