package ru.krivenchukartem.noteapp.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val body: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isPinned: Boolean
)