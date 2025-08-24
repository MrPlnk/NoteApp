package ru.krivenchukartem.noteapp.ui.destinations.home

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import ru.krivenchukartem.noteapp.domain.useCase.UpsertNoteUseCase
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
                        isSaving = true
                    )
                }
            } catch (t: Throwable){
                NoteDetailsUIState.Error(t.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun changeTitle(newTitle: String) {
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(
            form = s.form.copy(title = newTitle),
            isSaving = false
        )
    }

    fun changeBody(newBody: String) {
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(
            form = s.form.copy(body = newBody),
            isSaving = false
        )
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
        val s = uiState as? NoteDetailsUIState.Success ?: return
        if (!s.isSaving) {
            viewModelScope.launch {
                try {
                    if (!s.form.title.isBlank() || !s.form.body.isBlank()) {
                        upsertNote.invoke(
                            id = s.noteId,
                            title = s.form.title,
                            body = s.form.body,
                            isPinned = s.form.isPinned
                        )
                        uiState = s.copy(isSaving = true)
                    }
                } catch (t: Throwable) {
                    uiState = NoteDetailsUIState.Error(t.message ?: "Неизвестная ошибка")
                }
            }
        }
    }

    fun buildShareText(defaultText: String = ""): Intent?{
        val s = uiState as? NoteDetailsUIState.Success ?: return null
        val note = s.form
        val parts = listOf(note.title, note.body)
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val message = if (parts.isEmpty()) defaultText else parts.joinToString("\n\n")
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        return intent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getLastDateUpdate(defaultText: String = ""): String{
        val s = uiState as? NoteDetailsUIState.Success ?: return ""
        if (s.noteId == 0L) return ""
        val time = Instant.ofEpochMilli(s.updatedAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
        val formatted = time.format(formatter)
        return "$defaultText $formatted"
    }

    fun changePinState(){
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(
            form = s.form.copy(isPinned = !s.form.isPinned),
            isSaving = false
        )
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
    val isPinned: Boolean
)

private fun Note.toNoteForm(): NoteForm = NoteForm(
    title = title,
    body = body,
    isPinned = isPinned
)