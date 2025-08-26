package ru.krivenchukartem.noteapp.ui.destinations.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.krivenchukartem.noteapp.domain.useCase.DeleteAttachmentByIdUseCase
import ru.krivenchukartem.noteapp.domain.useCase.GetNoteByIdUseCaseSnapshot
import ru.krivenchukartem.noteapp.ui.form.AttachmentForm
import ru.krivenchukartem.noteapp.ui.mapper.toForm
import javax.inject.Inject

@HiltViewModel
class NoteAttachmentViewModel @Inject constructor(
    private val getNoteById: GetNoteByIdUseCaseSnapshot,
    private val deleteAttachment: DeleteAttachmentByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val noteId: Long = requireNotNull(savedStateHandle["noteId"])
    val currentIndex: Int = requireNotNull(savedStateHandle["currentIndex"])

    var uiState by mutableStateOf<AttachmentsUiState>(AttachmentsUiState.Loading)
        private set

    init {
        load()
    }

    private fun load(){
        viewModelScope.launch{
            uiState = try {
                val note = getNoteById(noteId)
                if (note == null){
                    AttachmentsUiState.Error("Заметка еще не была создана! Перед просмотром фото, сохраните заметку.")
                } else{
                    AttachmentsUiState.Success(
                        localState = LocalUiState(
                            attachments = note.attachments.map{ it.toForm() },
                            currentIdx = currentIndex
                        )
                    )
                }
            }
            catch (t: Throwable){
                AttachmentsUiState.Error(message = t.message ?: "Unknown error")
            }
        }
    }

    fun delAttachment(id: Long){
        viewModelScope.launch {
            val s = uiState as? AttachmentsUiState.Success ?: return@launch
            deleteAttachment(id)
        }
    }

    fun indexUpdate(newIdx: Int){
        val s = uiState as? AttachmentsUiState.Success ?: return
        uiState = s.copy(
            localState = s.localState.copy(
                currentIdx = newIdx
            )
        )
    }
}

sealed interface AttachmentsUiState{
    data class Error(val message: String) : AttachmentsUiState
    data class Success(val localState: LocalUiState) : AttachmentsUiState
    object Loading : AttachmentsUiState
}

data class LocalUiState(
    val attachments: List<AttachmentForm> = listOf(),
    val currentIdx: Int = 0
)
