package ru.krivenchukartem.noteapp.domain.model

/**
 * Доменная модель тега.
 *
 * @property id идентификатор тега
 * @property name название
 */
data class Tag(
    val id: Long = 0,
    val name: String,
)