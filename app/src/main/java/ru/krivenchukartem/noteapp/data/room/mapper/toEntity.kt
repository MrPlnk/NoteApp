package ru.krivenchukartem.noteapp.data.room.mapper

import ru.krivenchukartem.noteapp.data.room.entity.NoteItem
import ru.krivenchukartem.noteapp.domain.model.Note

fun Note.toEntity(): NoteItem = NoteItem(id, title, body)