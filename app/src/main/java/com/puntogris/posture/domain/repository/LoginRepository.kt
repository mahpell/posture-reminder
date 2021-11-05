package com.puntogris.posture.domain.repository

import androidx.activity.result.ActivityResult
import com.puntogris.posture.utils.LoginResult
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun serverAuthWithGoogle(result: ActivityResult): Flow<LoginResult>
    suspend fun signOutUser(): SimpleResult
    suspend fun singInAnonymously(): SimpleResult
    suspend fun getShowLoginPref(): Boolean
}