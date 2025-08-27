package ru.krivenchukartem.noteapp.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.krivenchukartem.noteapp.R

/**
 * Экран отображения ошибки.
 *
 * Содержит [NoteTopAppBar] с кнопкой «назад» и сообщение об ошибке в [ErrorMessage].
 *
 * @param navigateBack callback для возврата назад
 * @param errorMessage текст ошибки
 * @param modifier модификатор Compose
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorScreen(
    navigateBack: () -> Unit,
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            NoteTopAppBar(
                title = stringResource(R.string.label_error),
                canNavigateBack = true,
                navigateBack = navigateBack,
            )
        }
    ) { innerPadding ->
        ErrorMessage(
            errorMessage = errorMessage,
            modifier = Modifier
                .padding(innerPadding)
                .padding(dimensionResource(R.dimen.padding_small))
        )
    }
}