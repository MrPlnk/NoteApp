package ru.krivenchukartem.noteapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.krivenchukartem.noteapp.data.room.dao.NoteDao
import ru.krivenchukartem.noteapp.data.room.entity.NoteItem

@Database(
    entities = [NoteItem::class],
    version = 3,
    exportSchema = false
)

abstract class NoteDatabase: RoomDatabase() {
    abstract fun itemDao(): NoteDao
}