package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.NoteFullItem

@Dao
interface NoteFullDao {
    @Transaction
    @Query("SELECT * FROM notes WHERE id = :reqId")
    fun getNoteFullById(reqId: Long): Flow<NoteFullItem?>

    @Transaction
    @Query("""
        SELECT * FROM notes
        ORDER BY isPinned DESC, createdAt DESC
        """)
    fun getAllNotesFull(): Flow<List<NoteFullItem>>

    @Transaction
    @Query("""
        SELECT n.* FROM notes n
        WHERE(
            :query IS NULL OR :query = '' OR n.title LIKE '%' || :query || '%' OR n.body LIKE '%' || :query || '%'
        )
        AND NOT EXISTS(
            SELECT 1
            FROM (
                SELECT id AS tagId FROM tags WHERE name IN (:selectedTagNames)
            ) sel
            LEFT JOIN note_tag nt
                ON nt.tagId = sel.tagId
                AND nt.noteId = n.id
            WHERE nt.noteId IS NULL
        ) 
        ORDER BY isPinned DESC, createdAt DESC
        """)
    fun filterNotesFull(selectedTagNames: List<String>, query: String): Flow<List<NoteFullItem>>
}