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

fun Note.toEntity(): NoteItem = NoteItem(
    id = id,
    title = title,
    body = body,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isPinned = isPinned
)

fun NoteFull.toEntity(): NoteFullItem = NoteFullItem(
    note = note,
    tags = tags,
    attachmentItems = attachmentItems
)

fun Tag.toEntity(): TagItem = TagItem(
    id = id,
    name = name
)

fun Attachment.toEntity(): AttachmentItem = AttachmentItem(
    id = id,
    noteId = noteId,
    uri = uri,
    mime = mime,
    width = width,
    height = height,
    createdAt = createdAt
)

fun NoteTagCrossRef.toEntity(): NoteTagCrossRefItem = NoteTagCrossRefItem(
    noteId = noteId,
    tagId = tagId
)


