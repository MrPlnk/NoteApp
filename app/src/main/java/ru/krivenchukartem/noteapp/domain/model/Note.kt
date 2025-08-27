package ru.krivenchukartem.noteapp.domain.model

/**
 * Доменная модель заметки.
 *
 * @property id идентификатор заметки
 * @property title заголовок
 * @property body текст
 * @property createdAt время создания (epoch millis)
 * @property updatedAt время последнего изменения (epoch millis)
 * @property isPinned флаг закрепления
 */
data class Note(
    val id: Long = 0,
    val title: String,
    val body: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isPinned: Boolean
)