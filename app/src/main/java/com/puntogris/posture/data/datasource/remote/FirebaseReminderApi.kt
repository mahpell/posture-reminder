package com.puntogris.posture.data.datasource.remote

import com.puntogris.posture.domain.model.Reminder
import com.puntogris.posture.utils.constants.Constants.REMINDERS_COLLECTION
import com.puntogris.posture.utils.constants.Constants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseReminderApi @Inject constructor(
    private val firebase: FirebaseClients
) : ReminderServerApi {

    private fun reminderCollection() = firebase.firestore
        .collection(USERS_COLLECTION)
        .document(requireNotNull(firebase.currentUid))
        .collection(REMINDERS_COLLECTION)

    override suspend fun deleteReminder(reminderId: String) {
        reminderCollection()
            .document(reminderId)
            .delete()
            .await()
    }

    override suspend fun saveReminder(reminder: Reminder) {
        reminderCollection()
            .document(reminder.reminderId)
            .set(reminder)
            .await()
    }

    override suspend fun saveReminders(reminders: List<Reminder>) {
        firebase.firestore.runBatch {
            for (reminder in reminders) {
                val ref = reminderCollection().document(reminder.reminderId)
                it.set(ref, reminder)
            }
        }.await()
    }

    override suspend fun getReminders(): List<Reminder> {
        return reminderCollection()
            .get()
            .await()
            .toObjects(Reminder::class.java)
    }
}