package ru.krivenchukartem.noteapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.destinations.home.HomeScreenDestination
import ru.krivenchukartem.noteapp.ui.destinations.home.homeDestination
import ru.krivenchukartem.noteapp.ui.destinations.note.noteDestination
import ru.krivenchukartem.noteapp.ui.destinations.note.NoteDetailsDestination
import ru.krivenchukartem.noteapp.ui.destinations.note.navigateToNoteAttachmentScreen

/**
 * Корневой раздел графа навигации — Home.
 */
@Serializable
object Home

/**
 * Корневой раздел графа навигации — Note.
 */
@Serializable
object Note

/**
 * Основной граф навигации приложения.
 *
 * Содержит разделы:
 * - [Home] — главный экран
 * - [Note] — экраны заметок (детали, вложения)
 *
 * @param navHostController контроллер навигации (по умолчанию создаётся через [rememberNavController])
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteNavGraph(
    navHostController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navHostController,
        startDestination = Home
    ){
        homeDestination(
            navigateBack = navHostController::popBackStack,
            navigateToNoteDetails = navHostController::navigateToNote,
        )
        noteDestination(
            navigateBack = navHostController::popBackStack,
            navigateToNoteAttachmentScreen = navHostController::navigateToNoteAttachmentScreen
        )
    }
}

/**
 * Переход к экрану Home.
 */
fun NavController.navigateToHome(){
    navigate(route = HomeScreenDestination)
}

/**
 * Переход к экрану деталей заметки.
 *
 * @param id идентификатор заметки
 */
fun NavController.navigateToNote(id: Long){
    navigate(route = NoteDetailsDestination(noteId = id))
}