package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import javax.inject.Inject

class GetAllNotesFullUseCaseStream @Inject constructor(
    private val noteFullRepo: NoteQueryRepo
) {
    operator fun invoke() : Flow<List<NoteFull>>{
        return noteFullRepo.getAllNotesFull()
    }
}