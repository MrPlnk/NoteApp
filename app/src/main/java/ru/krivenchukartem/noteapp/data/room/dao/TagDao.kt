package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.TagItem

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: TagItem): Long

    @Query("""
        SELECT * FROM tags
        ORDER BY name
        """)
    fun getAllTags(): Flow<List<TagItem>>

    @Query("""
        SELECT * FROM tags
        WHERE name = :name LIMIT 1
        """)
    fun getTagByName(name: String): Flow<TagItem?>
}