package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.krivenchukartem.noteapp.domain.model.Note

data class NoteFullItem(
    @Embedded val note: Note,
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
    val attachments: List<Attachment>
)
