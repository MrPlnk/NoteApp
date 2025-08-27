package ru.krivenchukartem.noteapp.ui.destinations.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.krivenchukartem.noteapp.ui.navigation.Home

/**
 * Точка назначения для экрана «Home» в графе навигации.
 */
@Serializable
object HomeScreenDestination

/**
 * Конфигурация навигации для раздела Home.
 *
 * @param navigateBack callback для возврата назад
 * @param navigateToNoteDetails переход к экрану деталей заметки по id
 */
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

/**
 * Экран Home внутри графа навигации.
 *
 * @param navigateToNoteDetails переход к экрану деталей заметки по id
 */
fun NavGraphBuilder.homeScreenDestination(
    navigateToNoteDetails: (Long) -> Unit,
){
    composable<HomeScreenDestination>{
        HomeScreen(
            navigateToNoteDetails = navigateToNoteDetails
        )
    }
}
