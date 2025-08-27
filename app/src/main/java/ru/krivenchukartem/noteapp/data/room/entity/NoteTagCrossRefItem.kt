package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Связующая таблица «многие-ко-многим» между заметками и тегами.
 *
 * - [noteId] ссылается на [NoteItem], при удалении заметки связи удаляются (CASCADE).
 * - [tagId] ссылается на [TagItem], при удалении тега связи удаляются (CASCADE).
 *
 * Первичный ключ составной — пара (noteId, tagId).
 */
@Entity(
    tableName = "note_tag",
    primaryKeys = ["noteId", "tagId"],
    indices = [Index("tagId"), Index("noteId")],
    foreignKeys = [
        ForeignKey(entity = NoteItem::class, parentColumns = ["id"], childColumns = ["noteId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TagItem::class, parentColumns = ["id"], childColumns = ["tagId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class NoteTagCrossRefItem(
    val noteId: Long,
    val tagId: Long,
)