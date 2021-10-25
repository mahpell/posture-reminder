package com.puntogris.posture.data.repository.main

import com.puntogris.posture.data.datasource.remote.FirebaseDataSource
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): IMainRepository{

    override fun isUserLoggedIn() = firebaseDataSource.getCurrentUser() != null
}