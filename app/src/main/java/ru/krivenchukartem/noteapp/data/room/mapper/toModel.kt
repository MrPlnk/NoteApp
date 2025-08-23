package ru.krivenchukartem.noteapp.data.room.mapper

import ru.krivenchukartem.noteapp.data.room.entity.NoteItem
import ru.krivenchukartem.noteapp.domain.model.Note

fun NoteItem.toModel(): Note = Note(id, title, body)