package ru.krivenchukartem.noteapp.ui.form


data class NoteFullForm(
    val noteForm: NoteForm = NoteForm(),
    val tagsForm: List<TagForm> = listOf(),
    val attachmentsForm: List<AttachmentForm> = listOf()
)
