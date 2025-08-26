package ru.krivenchukartem.noteapp.ui.destinations.note

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.composable.StubScreen
import ru.krivenchukartem.noteapp.ui.navigation.Note


@Serializable
data class NoteDetailsDestination(val noteId: Long)

@Serializable
data class NoteAttachmentDestination(val noteId: Long, val currentIndex: Int)

@Serializable
object StubDestination

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

fun NavGraphBuilder.stubScreenDestination(){
    composable<StubDestination> {
        StubScreen()
    }
}

fun NavController.navigateToNoteAttachmentScreen(nodeId: Long, currentIndex: Int){
    navigate(
        NoteAttachmentDestination(noteId = nodeId, currentIndex = currentIndex)
    )
}