package uk.ac.tees.mad.habitloop.mainapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.habitloop.mainapp.model.HabitDocument
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import uk.ac.tees.mad.habitloop.room.HabitDatabase
import javax.inject.Inject


@HiltViewModel
class HabitViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    application: Application
): ViewModel() {

    private val habitDb = Room.databaseBuilder(
        application,
        HabitDatabase::class.java,
        "habit.db"
    ).build()

    private val habit = habitDb.habitDao()

    private val _allHabits = MutableStateFlow<List<HabitInfo?>>(emptyList())
    val allHabits = _allHabits.asStateFlow()

    init {
        getAllHabits()
    }

    //Retrieving all the habits from the database
    fun getAllHabits(){
        viewModelScope.launch {
            val currUser = auth.currentUser
            if(currUser != null){
                val userId  = currUser.uid
                firestore.collection("habits")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val habitList = documentSnapshot.toObject(HabitDocument::class.java)?.habits ?: emptyList()
                            _allHabits.value = habitList
                            Log.i("The response: ", "The list is $documentSnapshot")
                        } else {
                            Log.i("The response: ", "No such document")
                        }
                    }
                    .addOnFailureListener {
                        Log.i("The response: ", "The error is $it")
                    }
            }
        }
    }

    //Adding a new habit to the database
    fun addNewHabit(
        habit: HabitInfo
    ){
        viewModelScope.launch {
            val currUser = auth.currentUser
            if (currUser != null) {

                val updatedList = _allHabits.value + habit

                val habitData = mapOf("habits: " to updatedList)

                val userId = currUser.uid
                firestore.collection("habits")
                    .document(userId)
                    .set(habitData)
                    .addOnSuccessListener {
                        getAllHabits()
                        Log.i("New Habit: ", "The new habit is added to database.")
                    }
                    .addOnFailureListener {
                        getAllHabits()
                        Log.i("New Habit: ", "The new habit is not added to database.")
                    }
            }
        }
    }

}