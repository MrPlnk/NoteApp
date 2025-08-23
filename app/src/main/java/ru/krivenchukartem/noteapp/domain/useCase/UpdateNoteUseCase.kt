package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteRepo
import ru.krivenchukartem.noteapp.domain.model.Note
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepo: LocalNoteRepo
) {
    suspend operator fun invoke(id: Long, title: String, body: String){
        noteRepo.updateNote(id, title, body)
    }
}