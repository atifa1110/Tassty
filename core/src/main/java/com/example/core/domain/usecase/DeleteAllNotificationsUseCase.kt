package com.example.core.domain.usecase

import com.example.core.domain.repository.NotificationRepository
import javax.inject.Inject

class DeleteAllNotificationsUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
){
    suspend operator fun invoke(): Int {
        return notificationRepository.deleteAllNotifications()
    }
}