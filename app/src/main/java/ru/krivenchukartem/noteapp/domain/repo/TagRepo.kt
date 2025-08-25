package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Tag

interface TagRepo {
    suspend fun insertTag(tag: Tag): Long
    fun getAllTags(): Flow<List<Tag>>
    suspend fun getTagByName(name: String): Flow<Tag?>
}