package com.puntogris.posture.data.repository.rankings

import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.datasource.remote.FirebaseRankingDataSource
import com.puntogris.posture.model.UserPublicProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.puntogris.posture.utils.Result

class RankingsRepository @Inject constructor(
    private val rankingsFirebase: FirebaseRankingDataSource,
    private val dispatchers: DispatcherProvider
): IRankingsRepository {

    override suspend fun getAllRankingsFirestore(): Result<Exception, List<UserPublicProfile>> = withContext(dispatchers.io) {
        Result.build {
            rankingsFirebase.getRankingsQueryWithLimit()
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)
        }
    }

    override suspend fun getTopThreeRankingsFirestore(): Result<Exception, List<UserPublicProfile>> = withContext(dispatchers.io) {
        Result.build {
            rankingsFirebase.getRankingsQueryWithLimit(3)
                .get()
                .await()
                .toObjects(UserPublicProfile::class.java)
        }
    }

}