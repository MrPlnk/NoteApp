package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Inject

class GetAllNotesUseCaseStream @Inject constructor(
    private val noteRepo: NoteRepo
) {
    operator fun invoke() : Flow<List<Note>>{
        return noteRepo.getAllNotes()
    }
}