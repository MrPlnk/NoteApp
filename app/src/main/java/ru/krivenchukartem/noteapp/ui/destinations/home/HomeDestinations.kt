package ru.krivenchukartem.noteapp.ui.destinations.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.navigation.Home

@Serializable
object HomeScreenDestination

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.homeDestination(
    navigateBack: () -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
){
    navigation<Home>(startDestination = HomeScreenDestination){
        homeScreenDestination(
            navigateToNoteDetails = navigateToNoteDetails
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
