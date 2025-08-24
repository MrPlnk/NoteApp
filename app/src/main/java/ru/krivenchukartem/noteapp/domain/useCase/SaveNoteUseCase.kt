package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteRepo
import ru.krivenchukartem.noteapp.domain.model.Note
import javax.inject.Inject

class SaveNoteUseCase @Inject constructor(
    private val noteRepo: LocalNoteRepo
) {
    suspend operator fun invoke(title: String, body: String, isPinned: Boolean){
        noteRepo.saveNote(title, body, isPinned)
    }
}