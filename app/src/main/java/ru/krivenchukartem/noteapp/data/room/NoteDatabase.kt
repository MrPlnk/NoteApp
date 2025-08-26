package ru.krivenchukartem.noteapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.krivenchukartem.noteapp.data.room.dao.AttachmentDao
import ru.krivenchukartem.noteapp.data.room.dao.NoteDao
import ru.krivenchukartem.noteapp.data.room.dao.NoteFullDao
import ru.krivenchukartem.noteapp.data.room.dao.NoteTagCrossRefDao
import ru.krivenchukartem.noteapp.data.room.dao.TagDao
import ru.krivenchukartem.noteapp.data.room.entity.AttachmentItem
import ru.krivenchukartem.noteapp.data.room.entity.NoteItem
import ru.krivenchukartem.noteapp.data.room.entity.NoteTagCrossRefItem
import ru.krivenchukartem.noteapp.data.room.entity.TagItem

@Database(
    entities = [
        NoteItem::class,
        AttachmentItem::class,
        NoteTagCrossRefItem::class,
        TagItem::class
       ],
    version = 4,
    exportSchema = false
)

abstract class NoteDatabase: RoomDatabase() {
    abstract fun itemDao(): NoteDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun noteFullDao(): NoteFullDao
    abstract fun noteTagCrossRefDao(): NoteTagCrossRefDao
    abstract fun tagDao(): TagDao

}