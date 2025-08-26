package ru.krivenchukartem.noteapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.krivenchukartem.noteapp.data.repoImpl.LocalAttachmentRepo
import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteQueryRepo
import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteRepo
import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteTagCrossRefRepo
import ru.krivenchukartem.noteapp.data.repoImpl.LocalTagRepo
import ru.krivenchukartem.noteapp.domain.repo.AttachmentRepo
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import ru.krivenchukartem.noteapp.domain.repo.NoteTagCrossRefRepo
import ru.krivenchukartem.noteapp.domain.repo.TagRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoBindModule {
    @Singleton @Binds abstract fun bindNoteRepo(impl: LocalNoteRepo): NoteRepo
    @Singleton @Binds abstract fun bindTagRepo(impl: LocalTagRepo): TagRepo
    @Singleton @Binds abstract fun bindAttachRepo(impl: LocalAttachmentRepo): AttachmentRepo
    @Singleton @Binds abstract fun bindNoteTagRepo(impl: LocalNoteTagCrossRefRepo): NoteTagCrossRefRepo
    @Singleton @Binds abstract fun bindNoteQueryRepo(impl: LocalNoteQueryRepo): NoteQueryRepo
}