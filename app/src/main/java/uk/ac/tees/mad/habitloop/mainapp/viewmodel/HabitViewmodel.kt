package uk.ac.tees.mad.habitloop.mainapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.habitloop.authentication.model.AuthResponse
import uk.ac.tees.mad.habitloop.mainapp.model.FirebaseResponse
import uk.ac.tees.mad.habitloop.mainapp.model.HabitDocument
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import uk.ac.tees.mad.habitloop.room.HabitDatabase
import javax.inject.Inject


@HiltViewModel
class HabitViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    application: Application,
) : ViewModel() {


    private val _authState = MutableStateFlow<FirebaseResponse>(FirebaseResponse.Idle)
    val authState = _authState.asStateFlow()

    private val habitDb = Room.databaseBuilder(
        application,
        HabitDatabase::class.java,
        "habit.db"
    ).build()

    private val habitDao = habitDb.habitDao()

    private val _allHabits = MutableStateFlow<List<HabitInfo?>>(emptyList())
    val allHabits = _allHabits.asStateFlow()


    init {
        getAllHabits()
    }

    // Retrieving all the habits from Firestore
    fun getAllHabits() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                firestore.collection("habit")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        val habits =
                            document.get("habits") as? List<Map<String, Any>> ?: emptyList()
                        _authState.value = FirebaseResponse.Success
                        _allHabits.value = habits.mapNotNull { habitMap ->
                            try {
                                HabitInfo(
                                    name = habitMap["name"] as? String ?: "",
                                    goal = habitMap["goal"] as? String ?: "",
                                    schedule = habitMap["schedule"] as? String ?: "",
                                    completed = habitMap["completed"] as? Boolean ?: false
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        _authState.value = FirebaseResponse.Failure(
                            exception.message ?: "Failed to update"
                        )
                        _allHabits.value = emptyList() // Handle errors gracefully
                    }
            }
        }
    }

    // Adding a new habit to Firestore
    fun addNewHabit(habit: HabitInfo) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                val habitData = mapOf(
                    "habits" to FieldValue.arrayUnion(habit)
                )

                firestore.collection("habit")
                    .document(userId)
                    .set(habitData, SetOptions.merge()) // Use merge to create or update
                    .addOnSuccessListener {
                        getAllHabits()
                        _authState.value = FirebaseResponse.Success
                        Log.i("Adding new habit", "Success")
                    }
                    .addOnFailureListener { exception ->
                        _authState.value = FirebaseResponse.Failure(
                            exception.message ?: "Failed to update"
                        )
                        Log.e("Adding new habit", "Failed", exception)
                    }
            }
        }
    }


    //Delete the habit from the firebase.
    fun deleteHabit(habit: HabitInfo) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                firestore.collection("habit")
                    .document(userId)
                    .update("habits", FieldValue.arrayRemove(habit))
                    .addOnSuccessListener {
                        _authState.value = FirebaseResponse.Success
                        getAllHabits() // Refresh the list after deletion
                    }
                    .addOnFailureListener { exception ->
                        _authState.value = FirebaseResponse.Failure(
                            exception.message ?: "Failed to update"
                        )   // Handle errors here (e.g., logging or showing a message)
                    }
            }
        }
    }

    // Toggling the completion status of a habit
    fun toggleHabitCompletion(habit: HabitInfo) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                // Create a copy of the habit with the 'completed' field toggled
                val updatedHabit = habit.copy(completed = !habit.completed)

                // Remove the old habit and add the updated one
                firestore.collection("habit")
                    .document(userId)
                    .update(
                        "habits",
                        FieldValue.arrayRemove(habit) // Remove the old habit
                    ).addOnSuccessListener {
                        firestore.collection("habit")
                            .document(userId)
                            .update(
                                "habits",
                                FieldValue.arrayUnion(updatedHabit) // Add the updated habit
                            ).addOnSuccessListener {
                                _authState.value = FirebaseResponse.Success
                                getAllHabits() // Refresh the list after the update
                            }
                            .addOnFailureListener { exception ->
                                // Handle failure when adding the updated habit
                                _authState.value = FirebaseResponse.Failure(
                                    exception.message ?: "Failed to update"
                                )
                                Log.e("FirestoreError", "Failed to add updated habit", exception)
                            }
                    }
                    .addOnFailureListener { exception ->
                        // Handle failure when removing the old habit
                        Log.e("FirestoreError", "Failed to remove old habit", exception)
                        _authState.value = FirebaseResponse.Failure(
                            exception.message ?: "Failed to update"
                        )
                    }
            }
        }
    }


}