package ru.krivenchukartem.noteapp.domain.useCase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import ru.krivenchukartem.noteapp.domain.model.NoteFull
import ru.krivenchukartem.noteapp.domain.model.NoteTagCrossRef
import ru.krivenchukartem.noteapp.domain.repo.AttachmentRepo
import ru.krivenchukartem.noteapp.domain.repo.NoteRepo
import ru.krivenchukartem.noteapp.domain.repo.NoteTagCrossRefRepo
import ru.krivenchukartem.noteapp.domain.repo.TagRepo
import javax.inject.Inject

class UpsertNoteUseCase @Inject constructor(
    private val noteRepo: NoteRepo,
    private val tagRepo: TagRepo,
    private val noteTagCrossRefRepo: NoteTagCrossRefRepo,
    private val attachmentRepo: AttachmentRepo
) {
    suspend operator fun invoke(noteFull: NoteFull): Long{
        val noteId = if (noteFull.note.id == 0L){
            noteRepo.saveNote(
                title = noteFull.note.title.trim(),
                body = noteFull.note.body.trim(),
                isPinned = noteFull.note.isPinned,
            )
        }
        else{
            noteRepo.updateNote(
                id = noteFull.note.id,
                title = noteFull.note.title.trim(),
                body = noteFull.note.body.trim(),
                isPinned = noteFull.note.isPinned,
            )
            noteFull.note.id
        }

        val currentTagIds = noteTagCrossRefRepo.tagIdsForNote(noteId).first()
        val newTagIds = mutableListOf<Long>()
        for (tag in noteFull.tags){
            val tagId = if (tag.id == 0L){
                val existingTag = tagRepo.getTagByName(tag.name).firstOrNull()
                existingTag?.id ?: tagRepo.insertTag(tag.copy(name = tag.name.trim()))
            }
            else{
                tag.id
            }
            newTagIds.add(tagId)
        }

        val tagsToRemove = currentTagIds - newTagIds.toSet()
        for (tagIdToRemove in tagsToRemove){
            noteTagCrossRefRepo.detachTag(noteId, tagIdToRemove)
        }

        val tagsToAdd = newTagIds - currentTagIds.toSet()
        for (tagIdToAdd in tagsToAdd){
            noteTagCrossRefRepo.attachTag(
                NoteTagCrossRef(noteId, tagIdToAdd)
            )
        }

        val currentAttachments = attachmentRepo.getAttachmentsByNote(noteId).first()

        for (attachment in currentAttachments) {
            attachmentRepo.deleteAttachment(attachment.id)
        }

        for (attachment in noteFull.attachments) {
            attachmentRepo.insertAttachment(
                attachment.copy(noteId = noteId, createdAt = System.currentTimeMillis())
            )
        }

        return noteId
    }
}