package ru.krivenchukartem.noteapp.ui.mapper

import ru.krivenchukartem.noteapp.domain.model.Attachment
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.model.Tag
import ru.krivenchukartem.noteapp.ui.form.AttachmentForm
import ru.krivenchukartem.noteapp.ui.form.NoteForm
import ru.krivenchukartem.noteapp.ui.form.NoteFullForm
import ru.krivenchukartem.noteapp.ui.form.TagForm

fun Note.toForm(): NoteForm = NoteForm(
    id = id,
    title = title,
    body = body,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned
)

fun NoteFull.toForm(): NoteFullForm = NoteFullForm(
    noteForm = note.toForm(),
    tagsForm = tags.map { it.toForm() },
    attachmentsForm = attachments.map { it.toForm() },
)

fun Tag.toForm(): TagForm = TagForm(
    id = id,
    name = name
)

fun Attachment.toForm(): AttachmentForm = AttachmentForm(
    id = id,
    noteId = noteId,
    uri = uri,
    createdAt = createdAt
)