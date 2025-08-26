package ru.krivenchukartem.noteapp.ui.form

data class AttachmentForm(
    val id: Long = 0L,
    val noteId: Long = 0L,
    val uri: String = "",
    val createdAt: Long = 0L
)