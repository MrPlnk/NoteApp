package ru.krivenchukartem.noteapp.ui.destinations.note

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.composable.StubScreen
import ru.krivenchukartem.noteapp.ui.navigation.Note

/**
 * Точка назначения для экрана деталей заметки.
 *
 * @property noteId идентификатор заметки
 */
@Serializable
data class NoteDetailsDestination(val noteId: Long)

/**
 * Точка назначения для экрана просмотра вложений заметки.
 *
 * @property noteId идентификатор заметки
 * @property currentIndex индекс текущего вложения
 */
@Serializable
data class NoteAttachmentDestination(val noteId: Long, val currentIndex: Int)

/**
 * Заглушка-экран, используется как стартовая точка в графе `note`.
 */
@Serializable
object StubDestination

/**
 * Конфигурация графа навигации для раздела `note`.
 *
 * @param navigateBack callback для возврата назад
 * @param navigateToNoteAttachmentScreen переход к экрану вложений по id заметки и индексу
 */
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.noteDestination(
    navigateBack: () -> Unit,
    navigateToNoteAttachmentScreen: (Long, Int) -> Unit,
){
    navigation<Note>(startDestination = StubDestination){
        noteDetailScreenDestination(
            navigateBack = navigateBack,
            navigateToNoteAttachmentScreen = navigateToNoteAttachmentScreen
        )
        noteAttachmentScreenDestination(
            navigateBack = navigateBack
        )
        stubScreenDestination()
    }
}

/**
 * Экран деталей заметки в графе навигации.
 *
 * @param navigateBack callback для возврата назад
 * @param navigateToNoteAttachmentScreen переход к экрану вложений
 */
@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.noteDetailScreenDestination(
    navigateBack: () -> Unit,
    navigateToNoteAttachmentScreen: (Long, Int) -> Unit
){
    composable<NoteDetailsDestination>{ backStack ->
        NoteDetailsScreen(
            viewModel = hiltViewModel(backStack),
            navigateBack = navigateBack,
            navigateToNoteAttachmentScreen = navigateToNoteAttachmentScreen
        )
    }
}

/**
 * Экран вложений заметки в графе навигации.
 *
 * @param navigateBack callback для возврата назад
 */
fun NavGraphBuilder.noteAttachmentScreenDestination(
    navigateBack: () -> Unit,
){
    composable<NoteAttachmentDestination>{ backStackEntry ->
        NoteAttachmentScreen(
            viewModel = hiltViewModel(backStackEntry),
            navigateBack = navigateBack,
        )
    }
}

/**
 * Экран-заглушка в графе навигации.
 */
fun NavGraphBuilder.stubScreenDestination(){
    composable<StubDestination> {
        StubScreen()
    }
}

/**
 * Навигация к экрану вложений заметки.
 *
 * @param nodeId идентификатор заметки
 * @param currentIndex индекс текущего вложения
 */
fun NavController.navigateToNoteAttachmentScreen(nodeId: Long, currentIndex: Int){
    navigate(
        NoteAttachmentDestination(noteId = nodeId, currentIndex = currentIndex)
    )
}