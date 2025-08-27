package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import javax.inject.Inject

/**
 * UseCase для получения потока всех заметок с тегами и вложениями.
 *
 * Возвращает: [Flow] со списком [NoteFull].
 */
class GetAllNotesFullUseCaseStream @Inject constructor(
    private val noteFullRepo: NoteQueryRepo
) {
    operator fun invoke() : Flow<List<NoteFull>>{
        return noteFullRepo.getAllNotesFull()
    }
}