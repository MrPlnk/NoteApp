package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.first
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import javax.inject.Inject

class GetNoteByIdUseCaseSnapshot @Inject constructor(
    private val noteQueryRepo: NoteQueryRepo
) {
    suspend operator fun invoke(id: Long): NoteFull? {
        if (id == 0L) return null
        val existingNote = noteQueryRepo.getNoteFullById(id).first()
        return existingNote
    }
}