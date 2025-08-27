package ru.krivenchukartem.noteapp.data.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
/**
 * Сущность заметки для таблицы `notes`.
 *
 * Хранит основные данные заметки: заголовок, текст, дату создания/обновления и статус закрепления.
 */
@Entity(tableName = "notes", indices = [Index("createdAt")])
data class NoteItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val body: String,
    val createdAt: Long,
    val updatedAt: Long,
    val isPinned: Boolean
)