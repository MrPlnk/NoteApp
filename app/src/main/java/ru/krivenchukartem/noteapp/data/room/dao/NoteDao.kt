package ru.krivenchukartem.noteapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.room.entity.NoteItem

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteItem: NoteItem): Long

    @Query("""
        UPDATE NoteItem
        SET title=:title, body=:body, updatedAt=:updatedAt, isPinned=:isPinned
        WHERE id=:id
    """)
    suspend fun updateNote(id: Long, title: String, body: String, updatedAt: Long, isPinned: Boolean): Int

    @Query("DELETE FROM NoteItem WHERE id=:id")
    suspend fun deleteNote(id: Long)

    @Query("SELECT * FROM NoteItem WHERE id = :reqId")
    suspend fun getNoteById(reqId: Long): NoteItem?

    @Query("""
        SELECT * FROM NoteItem
        ORDER BY isPinned DESC, createdAt DESC
        """)
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("""
        SELECT * FROM NoteItem WHERE
        title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%'
        ORDER BY isPinned DESC, createdAt DESC
        """)
    fun search(query: String): Flow<List<NoteItem>>
}