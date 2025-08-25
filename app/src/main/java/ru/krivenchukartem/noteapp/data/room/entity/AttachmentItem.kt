package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments",
    indices = [Index("noteId")],
    foreignKeys = [
        ForeignKey(NoteItem::class, parentColumns = ["id"], childColumns = ["noteId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class AttachmentItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val noteId: Long,
    val uri: String,
    val mime: String?,
    val width: Int?,
    val height: Int?,
    val createdAt: Long
)

