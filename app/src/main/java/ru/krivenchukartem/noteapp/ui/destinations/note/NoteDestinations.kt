package ru.krivenchukartem.noteapp.ui.destinations.note

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.composable.StubScreen
import ru.krivenchukartem.noteapp.ui.navigation.Note


@Serializable
data class NoteDetailsDestination(val noteId: Long)

@Serializable
object StubDestination

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.noteDestination(
    navigateBack: () -> Unit,
){
    navigation<Note>(startDestination = StubDestination){
        noteDetailScreenDestination(
            navigateBack = navigateBack
        )
        stubScreenDestination()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.noteDetailScreenDestination(
    navigateBack: () -> Unit,
){
    composable<NoteDetailsDestination>{ backStack ->
        NoteDetailsScreen(
            viewModel = hiltViewModel(backStack),
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.stubScreenDestination(){
    composable<StubDestination> {
        StubScreen()
    }
}