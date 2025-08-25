package ru.krivenchukartem.noteapp.domain.model

data class Attachment(
    val id: Long = 0,
    val noteId: Long,
    val uri: String,
    val mime: String?,
    val width: Int?,
    val height: Int?,
    val createdAt: Long
)