package com.puntogris.posture.ui.reminders.manage

import androidx.lifecycle.ViewModel
import com.puntogris.posture.Notifications
import com.puntogris.posture.data.repo.reminder.ReminderRepository
import com.puntogris.posture.data.repo.user.UserRepository
import com.puntogris.posture.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageRemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun getAllReminders() = reminderRepository.getAllRemindersFromRoomLiveData()

    suspend fun deleteReminder(reminder: Reminder) = reminderRepository.deleteReminder(reminder)

    suspend fun insertReminder(reminder: Reminder) = reminderRepository.insertReminder(reminder)

    suspend fun updateCurrentReminder(reminderId: String) = userRepository.updateActiveReminderUserRoom(reminderId)
}