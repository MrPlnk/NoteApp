package ru.krivenchukartem.noteapp.ui.destinations.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.navigation.Home

@Serializable
object HomeScreenDestination

@Serializable
data class NoteDetailsScreenDestination(val noteId: Long)

fun NavGraphBuilder.homeDestination(
    navigateBack: () -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
){
    navigation<Home>(startDestination = HomeScreenDestination){
        homeScreenDestination(
            navigateToNoteDetails = navigateToNoteDetails
        )
        noteDetailDestination(
            navigateBack = navigateBack
        )
    }
}

fun NavGraphBuilder.homeScreenDestination(
    navigateToNoteDetails: (Long) -> Unit,
){
    composable<HomeScreenDestination>{
        HomeScreen(
            navigateToNoteDetails = navigateToNoteDetails
        )
    }
}

fun NavGraphBuilder.noteDetailDestination(
    navigateBack: () -> Unit,
){
    composable<NoteDetailsScreenDestination>{ backStack ->
        NoteDetailsScreen(
            viewModel = hiltViewModel(backStack),
            navigateBack = navigateBack
        )
    }
}

fun NavController.navigateToHomeScreen(){
    navigate(route = HomeScreenDestination)
}

fun NavController.navigateToNoteDetailsScreen(id: Long){
    navigate(route = NoteDetailsScreenDestination(noteId = id))
}