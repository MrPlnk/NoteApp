package ru.krivenchukartem.noteapp.domain.model

data class NoteFull(
    val note: Note,
    val tags: List<Tag>,
    val attachments: List<Attachment>
)
