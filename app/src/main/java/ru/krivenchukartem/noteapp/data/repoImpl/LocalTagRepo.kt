package ru.krivenchukartem.noteapp.data.repoImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.krivenchukartem.noteapp.data.room.dao.TagDao
import ru.krivenchukartem.noteapp.data.room.entity.TagItem
import ru.krivenchukartem.noteapp.data.room.mapper.toEntity
import ru.krivenchukartem.noteapp.data.room.mapper.toModel
import ru.krivenchukartem.noteapp.domain.model.Tag
import ru.krivenchukartem.noteapp.domain.repo.TagRepo

class LocalTagRepo(
    private val tagDao: TagDao
): TagRepo {
    override suspend fun insertTag(tag: Tag): Long {
        return tagDao.insertTag(tag.toEntity())

    }

    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags()
            .map { list -> list.map { it.toModel() } }
    }

    override suspend fun getTagByName(name: String): Flow<Tag?> {
        return tagDao.getTagByName(name)
            .map { it?.toModel() }
    }

}