package com.example.projet_session3.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

data class Trip(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val heureDebut: String = "",
    val heureFin: String = "",
    val positions: List<Position> = emptyList()
)

data class Position(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val type: String = "" // "début" ou "fin"
) {

}

class TripViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val tripsCollection = db.collection("trips_v3") // Firestore

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    private val _currentTrip = MutableStateFlow<Trip?>(null)
    val currentTrip: StateFlow<Trip?> = _currentTrip

    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips

    init {
        loadTrips()
    }

    private fun loadTrips() {
        tripsCollection
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("Firestore", "Erreur lors de l'écoute des voyages", e)
                    return@addSnapshotListener
                }

                val tripsList = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Trip::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                _trips.value = tripsList
            }
    }

    fun startRecording(startPosition: LatLng) {
        _isRecording.value = true
        val trip = Trip(
            id = UUID.randomUUID().toString(),
            date = Date().toString(),
            heureDebut = Date().toString(),
            positions = listOf(Position(startPosition.latitude, startPosition.longitude, "début"))
        )
        _currentTrip.value = trip
    }

    fun stopRecording(endPosition: LatLng, title: String, description: String) {
        _isRecording.value = false
        val currentTrip = _currentTrip.value ?: return
        
        val updatedTrip = currentTrip.copy(
            title = title,
            description = description,
            heureFin = Date().toString(),
            positions = currentTrip.positions + Position(endPosition.latitude, endPosition.longitude, "fin")
        )

        tripsCollection.document(updatedTrip.id)
            .set(updatedTrip)
            .addOnSuccessListener {
                Log.d("Firestore", "Document ajouté avec ID: ${updatedTrip.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erreur d'écriture", e)
            }

        _currentTrip.value = null
    }

    fun deleteTrip(tripId: String) {
        tripsCollection.document(tripId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Voyage supprimé avec succès")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erreur lors de la suppression", e)
            }
    }

    fun updateTrip(trip: Trip) {
        tripsCollection.document(trip.id)
            .set(trip)
            .addOnSuccessListener {
                Log.d("Firestore", "Voyage mis à jour avec succès")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Erreur lors de la mise à jour", e)
            }
    }
}