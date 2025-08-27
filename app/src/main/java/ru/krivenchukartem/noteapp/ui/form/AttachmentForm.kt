package ru.krivenchukartem.noteapp.ui.form

/**
 * UI-форма вложения.
 *
 * Используется для отображения и редактирования вложений в слое UI.
 *
 * @property id идентификатор вложения
 * @property noteId идентификатор заметки
 * @property uri путь к файлу (URI)
 * @property createdAt время создания (epoch millis)
 */
data class AttachmentForm(
    val id: Long = 0L,
    val noteId: Long = 0L,
    val uri: String = "",
    val createdAt: Long = 0L
)