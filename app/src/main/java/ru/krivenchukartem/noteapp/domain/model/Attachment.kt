package ru.krivenchukartem.noteapp.domain.model

data class Attachment(
    val id: Long = 0,
    val noteId: Long,
    val uri: String,
    val createdAt: Long
)