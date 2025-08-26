package ru.krivenchukartem.noteapp.data.room.mapper

import ru.krivenchukartem.noteapp.data.room.entity.AttachmentItem
import ru.krivenchukartem.noteapp.data.room.entity.NoteFullItem
import ru.krivenchukartem.noteapp.data.room.entity.NoteItem
import ru.krivenchukartem.noteapp.data.room.entity.NoteTagCrossRefItem
import ru.krivenchukartem.noteapp.data.room.entity.TagItem
import ru.krivenchukartem.noteapp.domain.model.Attachment
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.model.NoteTagCrossRef
import ru.krivenchukartem.noteapp.domain.model.Tag

fun NoteItem.toModel(): Note = Note(
    id = id,
    title = title,
    body = body,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned
)

fun TagItem.toModel(): Tag = Tag(
    id = id,
    name = name
)

fun AttachmentItem.toModel(): Attachment = Attachment(
    id = id,
    noteId = noteId,
    uri = uri,
    createdAt = createdAt
)

fun NoteTagCrossRefItem.toModel(): NoteTagCrossRef = NoteTagCrossRef(
    noteId = noteId,
    tagId = tagId
)

fun NoteFullItem.toModel(): NoteFull = NoteFull(
    note = note.toModel(),
    tags = tags.map { it.toModel() },
    attachments = attachments.map { it.toModel() },
)