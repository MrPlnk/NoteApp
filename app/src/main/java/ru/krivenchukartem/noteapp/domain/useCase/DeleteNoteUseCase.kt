package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.domain.model.Note
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val noteRepo: NoteRepo
) {
    suspend operator fun invoke(id: Long, title: String, body: String){
        val note = Note(id, title, body)
        noteRepo.deleteNote(note)
    }
}