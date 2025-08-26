package ru.krivenchukartem.noteapp.ui.destinations.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.krivenchukartem.noteapp.domain.useCase.GetAllNotesFullUseCaseStream
import ru.krivenchukartem.noteapp.domain.useCase.SearchNoteUseCaseStream
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchNote: SearchNoteUseCaseStream
): ViewModel() {


    private val _searchState = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> =
        searchState.flatMapLatest { state ->
            val src = searchNote(query = state.query, tags = state.tags)
            src
        }
        .map { list ->
            HomeUiState.Success(
                HomeLocalState(
                    notes = list.map { noteFull ->
                        NoteState(
                            noteFull.note.id,
                            noteFull.note.title,
                            noteFull.note.body,
                            noteFull.note.isPinned,
                            tags = noteFull.tags.map{ it.name }
                        )
                    },
                    firstUnpinnedIndex = list.indexOfFirst{ !it.note.isPinned }
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
        _searchState.value = _searchState.value.copy(query = newText)
    }
}

sealed interface HomeUiState{
    data class Success(val local: HomeLocalState) : HomeUiState
    object Loading : HomeUiState
    data class Error(val message: String): HomeUiState
}

data class HomeLocalState(
    val notes: List<NoteState> = listOf(),
    val firstUnpinnedIndex: Int = 0
)

data class NoteState(
    val id: Long = 0,
    val title: String = "",
    val body: String = "",
    val isPinned: Boolean = false,
    val tags: List<String> = listOf()
)

data class SearchState(
    val query: String = "",
    val tags: List<String> = listOf()
)