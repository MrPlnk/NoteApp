package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteRepo
import ru.krivenchukartem.noteapp.domain.model.Note
import javax.inject.Inject

class GetNotesByTitleUseCaseStream @Inject constructor(
    private val noteRepo: LocalNoteRepo
) {
    operator fun invoke(searchedText: String): Flow<List<Note>>{
        return noteRepo.getNotesByTitle(searchedText)
    }
}