package ru.krivenchukartem.noteapp.ui.form

/**
 * UI-форма тега.
 *
 * Используется для отображения и редактирования тегов в слое UI.
 *
 * @property id идентификатор тега
 * @property name название
 */
data class TagForm(
    val id: Long = 0L,
    val name: String = "",
)