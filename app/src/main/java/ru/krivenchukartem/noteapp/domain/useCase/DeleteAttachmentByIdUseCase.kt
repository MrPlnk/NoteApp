package ru.krivenchukartem.noteapp.domain.useCase

import ru.krivenchukartem.noteapp.domain.repo.AttachmentRepo
import javax.inject.Inject

class DeleteAttachmentByIdUseCase @Inject constructor(
    private val attachmentRepo: AttachmentRepo
) {
    suspend operator fun invoke(id: Long){
        attachmentRepo.deleteAttachment(id)
    }
}