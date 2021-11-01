package com.puntogris.posture.data.repository.user

import androidx.work.WorkManager
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.datasource.local.room.dao.UserDao
import com.puntogris.posture.data.datasource.local.room.db.AppDatabase
import com.puntogris.posture.data.datasource.remote.FirebaseUserDataSource
import com.puntogris.posture.model.Reminder
import com.puntogris.posture.utils.Constants
import com.puntogris.posture.utils.Constants.USER_NAME_FIELD
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val firebaseUser: FirebaseUserDataSource,
    private val alarm: Alarm,
    private val dispatchers: DispatcherProvider,
    private val appDatabase: AppDatabase
) : UserRepository {

    override fun isUserLoggedIn() = firebaseUser.getCurrentUser() != null

    override fun getLocalUserLiveData() = appDatabase.userDao().getUserLiveData()

    override suspend fun getLocalUser() = appDatabase.userDao().getUser()

    override suspend fun updateLocalAndServerUsername(name: String) = withContext(dispatchers.io) {
        SimpleResult.build {
            firebaseUser.runBatch().apply {
                update(firebaseUser.getUserPrivateDataRef(), USER_NAME_FIELD, name)
                update(firebaseUser.getUserPublicProfileRef(), USER_NAME_FIELD, name)
            }.commit().await()
            appDatabase.userDao().updateUsername(name)
        }
    }

    override suspend fun updateLocalActiveReminder(reminder: Reminder) {
        appDatabase.userDao().updateCurrentUserReminder(reminder.reminderId)
        alarm.refreshAlarms(reminder)
    }

    override suspend fun deleteUserAccountData(): SimpleResult = withContext(dispatchers.io) {
        SimpleResult.build {
            firebaseUser.runBatch().apply {
                delete(firebaseUser.getUserPublicProfileRef())
                delete(firebaseUser.getUserPrivateDataRef())
            }.commit().await()
            appDatabase.clearAllTables()
        }
    }
}