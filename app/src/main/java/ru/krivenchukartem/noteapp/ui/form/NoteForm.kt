package ru.krivenchukartem.noteapp.ui.form

/**
 * UI-форма заметки.
 *
 * Используется для отображения и редактирования заметок в слое UI.
 *
 * @property id идентификатор заметки
 * @property title заголовок
 * @property body текст
 * @property updatedAt время последнего изменения (epoch millis)
 * @property createdAt время создания (epoch millis)
 * @property isPinned флаг закрепления
 */
data class NoteForm(
    val id: Long = 0L,
    val title: String = "",
    val body: String = "",
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L,
    val isPinned: Boolean = false
)