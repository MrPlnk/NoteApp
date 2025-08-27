package ru.krivenchukartem.noteapp.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Tag

/**
 * Контракт доступа к тегам (Domain слой).
 */
interface TagRepo {
    /** Добавляет тег и возвращает его id. */
    suspend fun insertTag(tag: Tag): Long

    /** Возвращает поток всех тегов. */
    fun getAllTags(): Flow<List<Tag>>

    /** Возвращает тег по имени или null, если не найден. */
    fun getTagByName(name: String): Flow<Tag?>
}