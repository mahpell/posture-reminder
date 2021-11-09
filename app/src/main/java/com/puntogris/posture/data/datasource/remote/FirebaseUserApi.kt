package com.puntogris.posture.data.datasource.remote

import com.puntogris.posture.domain.model.UserPrivateData
import com.puntogris.posture.domain.model.UserPublicProfile
import com.puntogris.posture.utils.constants.Constants
import com.puntogris.posture.utils.constants.Constants.PUBLIC_PROFILE_COL_GROUP
import com.puntogris.posture.utils.constants.Constants.PUBLIC_PROFILE_DOC
import com.puntogris.posture.utils.constants.Constants.USERS_COLLECTION
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserApi @Inject constructor(
    private val firebase: FirebaseClients
) : UserServerApi {

    private fun privateProfileRef() = firebase.firestore
        .collection(USERS_COLLECTION)
        .document(requireNotNull(firebase.currentUid))

    private fun publicProfileRef() = privateProfileRef()
        .collection(PUBLIC_PROFILE_COL_GROUP)
        .document(PUBLIC_PROFILE_DOC)

    override suspend fun getUser(): UserPrivateData? {
        return privateProfileRef()
            .get()
            .await()
            .toObject(UserPrivateData::class.java)
    }

    override suspend fun createUser(user: UserPrivateData) {
        firebase.firestore.runBatch {
            it.set(privateProfileRef(), user)
            it.set(publicProfileRef(), UserPublicProfile.from(user))
        }.await()
    }

    override suspend fun deleteAccount() {
        firebase.firestore.runBatch {
            it.delete(privateProfileRef())
            it.delete(publicProfileRef())
        }.await()
    }

    override suspend fun updateUsername(username: String) {
        firebase.firestore.runBatch {
            it.update(privateProfileRef(), Constants.USER_NAME_FIELD, username)
            it.update(publicProfileRef(), Constants.USER_NAME_FIELD, username)
        }.await()
    }

    override suspend fun updateExperience(experience: Int) {
        firebase.firestore.runBatch {
            it.update(
                privateProfileRef(), Constants.USER_NAME_FIELD,
                Constants.EXPERIENCE_FIELD, experience
            )
            it.update(
                publicProfileRef(), Constants.USER_NAME_FIELD,
                Constants.EXPERIENCE_FIELD, experience
            )
        }.await()
    }
}