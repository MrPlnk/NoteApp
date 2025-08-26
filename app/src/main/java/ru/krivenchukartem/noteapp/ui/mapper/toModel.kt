package ru.krivenchukartem.noteapp.ui.mapper

import ru.krivenchukartem.noteapp.domain.model.Attachment
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.model.Tag
import ru.krivenchukartem.noteapp.ui.form.AttachmentForm
import ru.krivenchukartem.noteapp.ui.form.NoteForm
import ru.krivenchukartem.noteapp.ui.form.NoteFullForm
import ru.krivenchukartem.noteapp.ui.form.TagForm

fun NoteForm.toModel(): Note = Note(
    id = id,
    title = title,
    body = body,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned
)

fun TagForm.toModel(): Tag = Tag(
    id = id,
    name = name
)

fun AttachmentForm.toModel(): Attachment = Attachment(
    id = id,
    noteId = noteId,
    uri = uri,
    createdAt = createdAt
)

fun NoteFullForm.toModel(): NoteFull = NoteFull(
    note = noteForm.toModel(),
    tags = tagsForm.map { it.toModel() },
    attachments = attachmentsForm.map { it.toModel() },
)