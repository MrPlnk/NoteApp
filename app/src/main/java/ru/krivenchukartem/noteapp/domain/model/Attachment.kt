package ru.krivenchukartem.noteapp.domain.model

/**
 * Доменная модель вложения.
 *
 * @property id идентификатор вложения
 * @property noteId идентификатор связанной заметки
 * @property uri путь к файлу (URI)
 * @property createdAt время создания (epoch millis)
 */
data class Attachment(
    val id: Long = 0,
    val noteId: Long,
    val uri: String,
    val createdAt: Long
)