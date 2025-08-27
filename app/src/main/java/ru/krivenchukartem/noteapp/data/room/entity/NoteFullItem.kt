package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Композитная модель заметки с тегами и вложениями.
 *
 * Используется для выборок с объединением:
 * - [tags] связаны через таблицу `NoteTagCrossRefItem`.
 * - [attachments] связаны по внешнему ключу `noteId`.
 */
data class NoteFullItem(
    @Embedded val note: NoteItem,
    
    @Relation(
        parentColumn = "id",
        entity = TagItem::class,
        entityColumn = "id",
        associateBy = Junction(
            value = NoteTagCrossRefItem::class,
            parentColumn = "noteId",
            entityColumn = "tagId"
        )
    )
    val tags: List<TagItem>,

    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val attachments: List<AttachmentItem>
)
