package ru.krivenchukartem.noteapp.ui.form

data class NoteForm(
    val id: Long = 0L,
    val title: String = "",
    val body: String = "",
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L,
    val isPinned: Boolean = false
)