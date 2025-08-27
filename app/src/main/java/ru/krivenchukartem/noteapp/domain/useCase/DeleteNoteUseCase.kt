package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Inject

/**
 * UseCase для удаления заметки по id.
 */
class DeleteNoteUseCase @Inject constructor(
    private val noteRepo: NoteRepo
) {
    suspend operator fun invoke(id: Long){
        noteRepo.deleteNote(id)
    }
}