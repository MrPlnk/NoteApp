package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "note_tag",
    primaryKeys = ["notedId", "tagId"],
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