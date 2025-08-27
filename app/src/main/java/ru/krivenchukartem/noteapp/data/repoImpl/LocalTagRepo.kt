package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.TagDao
import ru.krivenchukartem.noteapp.data.room.entity.TagItem
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.Tag
import ru.krivenchukartem.noteapp.domain.repo.TagRepo
import javax.inject.Inject

/**
 * Репозиторий тегов, работает с локальной БД через [TagDao].
 */
class LocalTagRepo @Inject constructor(
    private val tagDao: TagDao
): TagRepo {
    /** Добавляет тег и возвращает его id. */
    override suspend fun insertTag(tag: Tag): Long {
        return tagDao.insertTag(tag.toEntity())

    }

    /** Возвращает поток всех тегов. */
    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags()
            .map { list -> list.map { it.toModel() } }
    }

    /** Возвращает тег по имени или null, если не найден. */
    override fun getTagByName(name: String): Flow<Tag?> {
        return tagDao.getTagByName(name)
            .map { it?.toModel() }
    }

}