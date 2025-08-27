package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.Flow
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.repo.NoteQueryRepo
import javax.inject.Inject

/**
 * UseCase для поиска заметок по строке и тегам.
 *
 * Возвращает: [Flow] со списком [NoteFull].
 */
class SearchNoteUseCaseStream @Inject constructor(
    private val noteQueryRepo: NoteQueryRepo
) {
    operator fun invoke(query: String = "", tags: List<String> = listOf()): Flow<List<NoteFull>>{
        return noteQueryRepo.filterNotesFull(
            query = query,
            tags = tags,
        )
    }
}