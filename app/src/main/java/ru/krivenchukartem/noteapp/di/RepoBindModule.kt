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
    @Binds abstract fun bindNoteRepo(impl: LocalNoteRepo): NoteRepo
    @Binds abstract fun bindTagRepo(impl: LocalTagRepo): TagRepo
    @Binds abstract fun bindAttachRepo(impl: LocalAttachmentRepo): AttachmentRepo
    @Binds abstract fun bindNoteTagRepo(impl: LocalNoteTagCrossRefRepo): NoteTagCrossRefRepo
    @Binds abstract fun bindNoteQueryRepo(impl: LocalNoteQueryRepo): NoteQueryRepo
}