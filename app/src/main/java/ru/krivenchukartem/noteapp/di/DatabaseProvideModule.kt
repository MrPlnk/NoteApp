package ru.krivenchukartem.noteapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.krivenchukartem.noteapp.data.room.NoteDatabase
import ru.krivenchukartem.noteapp.data.room.dao.AttachmentDao
import ru.krivenchukartem.noteapp.data.room.dao.NoteDao
import ru.krivenchukartem.noteapp.data.room.dao.NoteFullDao
import ru.krivenchukartem.noteapp.data.room.dao.NoteTagCrossRefDao
import ru.krivenchukartem.noteapp.data.room.dao.TagDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseProvideModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NoteDatabase{
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "notes.db",
        ).fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideNoteDao(
        noteDatabase: NoteDatabase
    ): NoteDao = noteDatabase.itemDao()

    @Provides
    fun provideAttachmentDao(
        noteDatabase: NoteDatabase
    ): AttachmentDao = noteDatabase.attachmentDao()

    @Provides
    fun provideNoteFullDao(
        noteDatabase: NoteDatabase
    ): NoteFullDao = noteDatabase.noteFullDao()

    @Provides
    fun provideNoteTagCrossRefDao(
        noteDatabase: NoteDatabase
    ): NoteTagCrossRefDao = noteDatabase.noteTagCrossRefDao()

    @Provides
    fun provideTagDao(
        noteDatabase: NoteDatabase
    ): TagDao = noteDatabase.tagDao()
}