package ru.krivenchukartem.noteapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.destinations.home.homeDestination
import ru.krivenchukartem.noteapp.ui.destinations.home.navigateToNoteDetailsScreen

@Serializable
object Home

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
            navigateToNoteDetails = navHostController::navigateToNoteDetailsScreen,
        )
    }
}