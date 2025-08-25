package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.NoteTagCrossRefItem

@Dao
interface NoteTagCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun attachTag(ref: NoteTagCrossRefItem)

    @Query("""
        DELETE FROM note_tag
        WHERE noteId = :noteId AND tagId = :tagId
    """)
    suspend fun detachTag(noteId: Long, tagId: Long)

    @Query("""
        SELECT tagId FROM note_tag 
        WHERE noteId = :noteId
        """)
    fun tagIdsForNote(noteId: Long): Flow<List<Long>>
}