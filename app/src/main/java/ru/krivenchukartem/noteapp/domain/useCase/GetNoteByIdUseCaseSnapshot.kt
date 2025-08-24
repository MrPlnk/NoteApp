package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Inject

class GetNoteByIdUseCaseSnapshot @Inject constructor(
    private val noteRepo: NoteRepo
) {
    suspend operator fun invoke(id: Long): Note? {
        if (id == 0L) return Note(0, "", "", 0L, 0L, false)
        val existingNote = noteRepo.getNoteById(id)
        return existingNote

    }
}