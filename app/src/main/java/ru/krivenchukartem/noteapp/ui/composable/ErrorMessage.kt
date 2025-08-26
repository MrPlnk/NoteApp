package ru.krivenchukartem.noteapp.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import ru.krivenchukartem.noteapp.R

@Composable
fun ErrorMessage(
    errorMessage: String,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                errorMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}