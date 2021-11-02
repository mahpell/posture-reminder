package com.puntogris.posture.ui.reminders.manage

import androidx.lifecycle.ViewModel
import com.puntogris.posture.data.repository.reminder.ReminderRepository
import com.puntogris.posture.data.repository.user.UserRepository
import com.puntogris.posture.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ManageRemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val userRepository: UserRepository
): ViewModel() {

    fun getAllReminders() = reminderRepository.getAllLocalRemindersLiveData()

    suspend fun deleteReminder(reminder: Reminder) = reminderRepository.deleteReminder(reminder)

    suspend fun insertReminder(reminder: Reminder) = reminderRepository.insertReminder(reminder)

    suspend fun updateCurrentReminder(reminder: Reminder) = userRepository.updateLocalActiveReminder(reminder)

}