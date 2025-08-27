package ru.krivenchukartem.noteapp.domain.model

/**
 * Доменная модель связи заметки и тега (многие-ко-многим).
 *
 * @property noteId идентификатор заметки
 * @property tagId идентификатор тега
 */
data class NoteTagCrossRef(
    val noteId: Long,
    val tagId: Long,
)