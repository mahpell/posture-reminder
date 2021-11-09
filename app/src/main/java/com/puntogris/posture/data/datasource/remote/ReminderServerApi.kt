package com.puntogris.posture.data.datasource.remote

import com.puntogris.posture.domain.model.Reminder

interface ReminderServerApi {
    suspend fun deleteReminder(reminderId: String)
    suspend fun saveReminder(reminder: Reminder)
    suspend fun saveReminders(reminders: List<Reminder>)
    suspend fun getReminders(): List<Reminder>
}