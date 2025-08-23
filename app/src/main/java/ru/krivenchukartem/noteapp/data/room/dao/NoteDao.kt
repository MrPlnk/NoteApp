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

    @Update
    suspend fun updateNote(noteItem: NoteItem): Int

    @Delete
    suspend fun deleteNote(noteItem: NoteItem)

    @Query("SELECT * FROM NoteItem WHERE id = :reqId")
    suspend fun getNoteById(reqId: Long): NoteItem?

    @Query("SELECT * FROM NoteItem ORDER BY title")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM NoteItem WHERE title LIKE :reqTitle || '%'")
    fun getNotesByTitle(reqTitle: String): Flow<List<NoteItem>>
}