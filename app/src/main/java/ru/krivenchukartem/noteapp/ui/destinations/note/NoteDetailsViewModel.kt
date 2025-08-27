package ru.krivenchukartem.noteapp.ui.destinations.note

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import javax.inject.Inject
import ru.krivenchukartem.noteapp.domain.useCase.DeleteNoteUseCase
import ru.krivenchukartem.noteapp.domain.useCase.UpsertNoteUseCase
import ru.krivenchukartem.noteapp.ui.form.AttachmentForm
import ru.krivenchukartem.noteapp.ui.form.NoteFullForm
import ru.krivenchukartem.noteapp.ui.mapper.toForm
import ru.krivenchukartem.noteapp.ui.mapper.toModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * ViewModel для экрана деталей заметки.
 *
 * - Загружает заметку по id через [GetNoteByIdUseCaseSnapshot].
 * - Сохраняет/обновляет заметку через [UpsertNoteUseCase].
 * - Удаляет заметку ([DeleteNoteUseCase]) и вложения ([DeleteAttachmentByIdUseCase]).
 * - Управляет состоянием UI ([NoteDetailsUIState]).
 *
 * Возвращает: [NoteDetailsUIState] через [uiState].
 */
@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val getNoteById: GetNoteByIdUseCaseSnapshot,
    private val upsertNote: UpsertNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val deleteAttachment: DeleteAttachmentByIdUseCase,
    savedStateHandle: SavedStateHandle,
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
                    NoteDetailsUIState.Success(
                        noteFullForm = NoteFullForm(),
                        isSaving = true
                    )
                } else{
                    NoteDetailsUIState.Success(
                        noteFullForm = note.toForm()
                    )
                }
            } catch (t: Throwable){
                NoteDetailsUIState.Error(t.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun changeTitle(newTitle: String) {
        val s = uiState as? NoteDetailsUIState.Success ?: return
        val note = s.noteFullForm.noteForm
        uiState = s.copy(
            s.noteFullForm.copy(
                noteForm = note.copy(title = newTitle)
            ),
            isSaving = false
        )
    }

    fun changeBody(newBody: String) {
        val s = uiState as? NoteDetailsUIState.Success ?: return
        val note = s.noteFullForm.noteForm
        uiState = s.copy(
            s.noteFullForm.copy(
                noteForm = note.copy(body = newBody)
            ),
            isSaving = false
        )
    }

    fun deleteNote(){
        viewModelScope.launch {
            val s = uiState as? NoteDetailsUIState.Success ?: return@launch
            val note = s.noteFullForm.noteForm
            try {
                deleteNote.invoke(note.id)
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
            val note = s.noteFullForm.noteForm
            viewModelScope.launch {
                try {
                    if (!note.title.isBlank() || !note.body.isBlank()) {
                        upsertNote.invoke(s.noteFullForm.toModel())
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
        val note = s.noteFullForm.noteForm
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
        if (s.noteFullForm.noteForm.id == 0L) return ""
        val time = Instant.ofEpochMilli(s.noteFullForm.noteForm.updatedAt)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
        val formatted = time.format(formatter)
        return "$defaultText $formatted"
    }

    fun changePinState(){
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(
            noteFullForm = s.noteFullForm.copy(
                s.noteFullForm.noteForm.copy(isPinned = !s.noteFullForm.noteForm.isPinned),
            ),
            isSaving = false
        )
    }

    fun addAttachment(uris: List<Uri>){
        val s = uiState as? NoteDetailsUIState.Success ?: return
        uiState = s.copy(
            noteFullForm = s.noteFullForm.copy(
                attachmentsForm = s.noteFullForm.attachmentsForm + uris.map { uri ->
                    AttachmentForm(uri = uri.toString())
                },
            ),
            isSaving = false
        )
        saveNote()
    }

    fun delAttachment(id: Long) {
        viewModelScope.launch {
            val s = uiState as? NoteDetailsUIState.Success ?: return@launch
            deleteAttachment(id)

            uiState = s.copy(
                noteFullForm = s.noteFullForm.copy(
                    attachmentsForm = s.noteFullForm.attachmentsForm
                        .filterNot { it.id == id }
                )
            )

            saveNote()
        }
    }
}

/**
 * UI-состояние экрана деталей заметки.
 */
sealed interface NoteDetailsUIState{

    /** Данные загружаются. */
    object Loading : NoteDetailsUIState

    /** Ошибка загрузки или сохранения. */
    data class Error(val message: String) : NoteDetailsUIState

    /** Успешное состояние с заметкой. */
    data class Success(
        val noteFullForm: NoteFullForm,
        val isSaving: Boolean = false
    ) : NoteDetailsUIState
}
