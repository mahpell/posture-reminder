package com.puntogris.posture.data.repository.sync

import com.puntogris.posture.model.UserPrivateData
import com.puntogris.posture.utils.SimpleResult

interface ISyncRepository {
    suspend fun syncFirestoreAccountWithRoom(loginUser: UserPrivateData): SimpleResult
    suspend fun syncUserExperienceInFirestoreWithRoom()
}