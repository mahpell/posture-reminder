package com.puntogris.posture.data.repository.ticket

import com.puntogris.posture.data.DispatcherProvider
import com.puntogris.posture.data.datasource.remote.FirebaseDataSource
import com.puntogris.posture.model.Ticket
import com.puntogris.posture.utils.Constants.TICKET_COLLECTION
import com.puntogris.posture.utils.SimpleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val firebase: FirebaseDataSource,
    private val dispatchers: DispatcherProvider
): ITicketRepository {

    override suspend fun fillTicketWithUserDataAndSend(ticket: Ticket): SimpleResult {
        val firebaseUser = firebase.getCurrentUser()
        ticket.apply {
            username = firebaseUser?.displayName.toString()
            email = firebaseUser?.email.toString()
            uid = firebaseUser?.uid.toString()
        }
        return sendTicketToFirestore(ticket)
    }

    private suspend fun sendTicketToFirestore(ticket: Ticket): SimpleResult = withContext(dispatchers.io){
        SimpleResult.build {
            firebase.firestore.collection(TICKET_COLLECTION).add(ticket).await()
        }
    }
}