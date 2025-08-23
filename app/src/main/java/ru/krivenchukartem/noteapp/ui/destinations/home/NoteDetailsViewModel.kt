package ru.krivenchukartem.noteapp.ui.destinations.home

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.krivenchukartem.noteapp.R
import ru.krivenchukartem.noteapp.domain.useCase.GetNoteByIdUseCaseSnapshot
import javax.inject.Inject
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.useCase.DeleteNoteUseCase
import ru.krivenchukartem.noteapp.domain.useCase.SaveNoteUseCase
import ru.krivenchukartem.noteapp.domain.useCase.UpdateNoteUseCase
import ru.krivenchukartem.noteapp.domain.useCase.UpsertNoteUseCase

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val getNoteById: GetNoteByIdUseCaseSnapshot,
    private val upsertNote: UpsertNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf<NoteDetailsUIState>(NoteDetailsUIState.Loading)
        private set

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    init {
        load()
    }

    private fun load(){
        viewModelScope.launch {
            uiState = NoteDetailsUIState.Loading
            uiState = try {
                val note = getNoteById.invoke(noteId)
                if (note == null){
                    NoteDetailsUIState.Error("Заметка не найдена")
                } else{
                    NoteDetailsUIState.Success(
                        noteId = note.id,
                        updatedAt = note.updatedAt,
                        createdAt = note.createdAt,
                        form = note.toNoteForm(),
                        isSaving = false
                    )
                }
            } catch (t: Throwable){
                NoteDetailsUIState.Error(t.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun changeTitle(newTitle: String) {
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(form = s.form.copy(title = newTitle))
    }

    fun changeBody(newBody: String) {
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(form = s.form.copy(body = newBody))
    }

    fun deleteNote(){
        viewModelScope.launch {
            val s = uiState as? NoteDetailsUIState.Success ?: return@launch
            try {
                deleteNote.invoke(s.noteId)
                uiState = NoteDetailsUIState.Loading
            }
            catch (t: Throwable){
                uiState = NoteDetailsUIState.Error(t.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            val s = uiState as? NoteDetailsUIState.Success ?: return@launch
            uiState = s.copy(isSaving = true)
            try {
                if (!s.form.title.isBlank() || !s.form.body.isBlank()){
                    upsertNote.invoke(s.noteId, s.form.title, s.form.body)
                    uiState = s.copy(isSaving = false)
                }
            } catch (t: Throwable) {
                uiState = NoteDetailsUIState.Error(t.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun buildShareText(state: NoteDetailsUIState.Success, defaultText: String = ""): String{
        val note = state.form
        val parts = listOf(note.title, note.body)
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        return if (parts.isEmpty()) defaultText else parts.joinToString("\n\n")
    }

}

sealed interface NoteDetailsUIState{
    object Loading : NoteDetailsUIState
    data class Error(val message: String) : NoteDetailsUIState
    data class Success(
        val noteId: Long,
        val updatedAt: Long,
        val createdAt: Long,
        val form: NoteForm,
        val isSaving: Boolean = false
    ) : NoteDetailsUIState
}

data class NoteForm(
    val title: String,
    val body: String,
)

private fun Note.toNoteForm(): NoteForm = NoteForm(
    title = title,
    body = body
)