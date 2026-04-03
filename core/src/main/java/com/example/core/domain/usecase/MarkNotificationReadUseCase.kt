package com.example.core.domain.usecase


import com.example.core.domain.repository.NotificationRepository
import javax.inject.Inject

class MarkNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: String){
        return repository.markAsRead(id)
    }
}