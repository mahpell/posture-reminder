package com.puntogris.posture.data.repository

import android.content.Context
import androidx.activity.result.ActivityResult
import androidx.work.WorkManager
import com.puntogris.posture.R
import com.puntogris.posture.alarm.Alarm
import com.puntogris.posture.data.datasource.local.DataStore
import com.puntogris.posture.data.datasource.local.db.UserDao
import com.puntogris.posture.data.datasource.remote.FirebaseLoginDataSource
import com.puntogris.posture.data.datasource.remote.GoogleSingInDataSource
import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.repository.LoginRepository
import com.puntogris.posture.utils.DispatcherProvider
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import com.puntogris.posture.utils.constants.Constants.SYNC_ACCOUNT_WORKER
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginRepositoryImpl(
    private val workManager: WorkManager,
    private val loginFirebase: FirebaseLoginDataSource,
    private val dataStore: DataStore,
    private val userDao: UserDao,
    private val googleSingIn: GoogleSingInDataSource,
    private val alarm: Alarm,
    private val context: Context,
    private val dispatchers: DispatcherProvider
) : LoginRepository {

    override fun serverAuthWithGoogle(result: ActivityResult): Flow<LoginResult> = flow {
        try {
            emit(LoginResult.InProgress)
            val credential = googleSingIn.getCredentialWithIntent(result.data!!)
            val authResult = loginFirebase.auth.signInWithCredential(credential).await()
            emit(LoginResult.Success(UserPrivateData.from(authResult.user)))
        } catch (e: Exception) {
            emit(LoginResult.Error)
        }
    }

    override suspend fun signOutUser() = SimpleResult.build {
        alarm.cancelAlarms()
        loginFirebase.signOut()
        googleSingIn.signOut()
        dataStore.setShowLoginPref(true)
        workManager.cancelUniqueWork(SYNC_ACCOUNT_WORKER)
    }

    override suspend fun singInAnonymously() = withContext(dispatchers.io) {
        SimpleResult.build {
            val user = UserPrivateData(username = context.getString(R.string.human))
            userDao.insert(user)
            dataStore.setShowLoginPref(false)
        }
    }

    override suspend fun getShowLoginPref() = dataStore.showLoginPref()
}