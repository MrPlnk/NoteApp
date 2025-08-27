package ru.krivenchukartem.noteapp.ui.form

/**
 * UI-форма заметки с тегами и вложениями.
 *
 * Используется для отображения и редактирования комплексных данных заметки.
 *
 * @property noteForm данные заметки
 * @property tagsForm список тегов
 * @property attachmentsForm список вложений
 */
data class NoteFullForm(
    val noteForm: NoteForm = NoteForm(),
    val tagsForm: List<TagForm> = listOf(),
    val attachmentsForm: List<AttachmentForm> = listOf()
)
