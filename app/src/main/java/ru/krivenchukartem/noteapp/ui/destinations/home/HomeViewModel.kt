package ru.krivenchukartem.noteapp.ui.destinations.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.krivenchukartem.noteapp.domain.useCase.GetAllNotesUseCaseObservable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getAllNotesUseCaseObservable: GetAllNotesUseCaseObservable
): ViewModel() {

    val homeUiState: StateFlow<HomeUiState> =
        getAllNotesUseCaseObservable.invoke()
            .map { list ->
                HomeUiState.Success(local = HomeLocalState(
                    list.map { NoteState(it.id, it.title, it.body) }
                    )
                ) as HomeUiState
            }
            .onStart { emit(HomeUiState.Loading) }
            .catch{ emit(HomeUiState.Error(it.message ?: "Unknown"))}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState.Loading
            )

    companion object{
        const val TIMEOUT_MILLIS = 5_000L
    }
}

sealed interface HomeUiState{
    data class Success(val local: HomeLocalState) : HomeUiState
    object Loading : HomeUiState
    data class Error(val message: String): HomeUiState
}

data class HomeLocalState(
    val notes: List<NoteState> = listOf()
)

data class NoteState(
    val id: Long = 0,
    val title: String = "Unnamed",
    val body: String = "Some text"
)