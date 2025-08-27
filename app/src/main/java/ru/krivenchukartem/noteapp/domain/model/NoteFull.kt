package ru.krivenchukartem.noteapp.domain.model

/**
 * Доменная модель заметки с тегами и вложениями.
 *
 * @property note сама заметка
 * @property tags список связанных тегов
 * @property attachments список вложений
 */
data class NoteFull(
    val note: Note,
    val tags: List<Tag>,
    val attachments: List<Attachment>
)
