package ru.krivenchukartem.noteapp.ui.destinations.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.krivenchukartem.noteapp.domain.useCase.GetAllNotesUseCaseStream
import ru.krivenchukartem.noteapp.domain.useCase.GetNotesByTitleUseCaseStream
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNotes: GetAllNotesUseCaseStream,
    private val getNotesByTitle: GetNotesByTitleUseCaseStream
): ViewModel() {

    val query = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> =
        query.flatMapLatest { q ->
            val src = if (q.isEmpty()) getAllNotes() else getNotesByTitle(q)
            src
        }
        .map { list ->
            HomeUiState.Success(
                HomeLocalState(
                    notes = list.map {
                        NoteState(it.id, it.title, it.body)
                    }
                )
            ) as HomeUiState
        }
        .catch{ emit(HomeUiState.Error(it.message ?: "Unknown"))}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState.Loading
        )

    companion object{
        const val TIMEOUT_MILLIS = 5_000L
    }

    fun changeQuery(newText: String){
        query.value = newText
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