package ru.krivenchukartem.noteapp.ui.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.krivenchukartem.noteapp.R

/**
 * Верхняя панель приложения для экрана заметок.
 *
 * Использует [CenterAlignedTopAppBar].
 *
 * @param title заголовок панели
 * @param modifier модификатор Compose
 * @param scrollBehavior поведение прокрутки (опционально)
 * @param canNavigateBack если true — отображается кнопка «назад»
 * @param navigateBack callback при нажатии на кнопку «назад»
 * @param actionsButtons слот для дополнительных action-кнопок справа
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    canNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    actionsButtons: @Composable (RowScope.() -> Unit) = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack){
                IconButton(
                    onClick = navigateBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.button_back)
                    )
                }
            }
        },
        actions = actionsButtons
    )
}