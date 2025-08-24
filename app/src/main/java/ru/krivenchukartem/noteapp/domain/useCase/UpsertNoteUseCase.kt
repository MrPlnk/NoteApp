package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.data.repoImpl.LocalNoteRepo
import ru.krivenchukartem.noteapp.domain.model.Note
import javax.inject.Inject

class UpsertNoteUseCase @Inject constructor(
    private val noteRepo: LocalNoteRepo
) {
    suspend operator fun invoke(id: Long, title: String, body: String, isPinned: Boolean): Long{
        val trimTitle = title.trim()
        val trimBody = body.trim()
        return if (id == 0L){
            noteRepo.saveNote(title = trimTitle, body = trimBody, isPinned = isPinned)
        }
        else{
            val updated = noteRepo.updateNote(id, trimTitle, trimBody, isPinned)
            if (updated == 0) noteRepo.saveNote(title = trimTitle, body = trimBody, isPinned = isPinned) else id
        }
    }
}