package ru.krivenchukartem.noteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteRepo
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoBindModule {
    @Binds
    @Singleton
    abstract fun bindNoteRepo(
        impl: LocalNoteRepo
    ): NoteRepo
}