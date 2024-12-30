package uk.ac.tees.mad.habitloop.mainapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.habitloop.mainapp.model.HabitInfo
import javax.inject.Inject


@HiltViewModel
class HabitViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ViewModel() {

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
                    .addOnSuccessListener {
                        val fetchedHabit = listOf(it.toObject(HabitInfo::class.java))
                        _allHabits.value = fetchedHabit
                        Log.i("The response: ", "The list is $fetchedHabit")
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
                val userId = currUser.uid
                firestore.collection("habits")
                    .document(userId)
                    .set(habit)
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