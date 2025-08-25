package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.NoteItem

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteItem: NoteItem): Long

    @Query("""
        UPDATE notes
        SET title=:title, body=:body, updatedAt=:updatedAt, isPinned=:isPinned
        WHERE id=:id
    """)
    suspend fun updateNote(id: Long, title: String, body: String, updatedAt: Long, isPinned: Boolean): Int

    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun deleteNote(id: Long)

    @Query("""
        SELECT * FROM notes
        ORDER BY isPinned DESC, createdAt DESC
    """)
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("""
        SELECT * FROM notes
        WHERE id = :id
        ORDER BY isPinned DESC, createdAt DESC
    """)
    fun getNoteById(id: Long): Flow<NoteItem?>
}