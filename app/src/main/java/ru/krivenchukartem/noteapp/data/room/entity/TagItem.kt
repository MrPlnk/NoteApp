package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Сущность тега для таблицы `tags`.
 *
 * Уникальность обеспечивается индексом по [name].
 */
@Entity(
    tableName = "tags",
    indices = [Index(value = ["name"], unique = true)]
)
data class TagItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
)