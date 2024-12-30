package uk.ac.tees.mad.habitloop.authentication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.habitloop.authentication.model.AuthResponse
import uk.ac.tees.mad.habitloop.authentication.model.UserInfo
import javax.inject.Inject


@HiltViewModel
class AuthViewmodel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
):ViewModel() {

    private val _authState = MutableStateFlow<AuthResponse>(AuthResponse.Idle)
    val authState = _authState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<UserInfo?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        checkIfUserExist()
    }


    fun checkIfUserExist(){
        val currUser = auth.currentUser
        if (currUser!=null){
            _isLoggedIn.value = true
        }else{
            _isLoggedIn.value = false
        }
    }

    fun LoginUser(email: String, password: String){
        viewModelScope.launch {
            _authState.value = AuthResponse.Loading

            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        _authState.value = AuthResponse.Success
                        Log.i("The User: ", "")
                    }
                    .addOnFailureListener {
                        _authState.value = AuthResponse.Failure("Error encountered ${it.message}")
                        Log.i("Error Login: ", "Error encountered ${it.message}")
                    }
            }catch (e: Exception){
                _authState.value = AuthResponse.Failure("Error encountered ${e.message}")
                Log.i("Error Login: ", "Error encountered ${e.message}")
            }
        }
    }

    fun RegisterUser(user: UserInfo, password: String){
        viewModelScope.launch {
            _authState.value = AuthResponse.Loading

            try {
                auth.createUserWithEmailAndPassword(user.email, password)
                    .addOnSuccessListener {
                        val currUser = auth.currentUser
                        if (currUser != null){
                            val userId = currUser.uid
                            firestore.collection("users")
                                .document(userId)
                                .set(user)
                                .addOnSuccessListener {
                                    _authState.value = AuthResponse.Success
                                    Log.i("Registration User: ", "The user registered successfully")
                                }
                                .addOnFailureListener {
                                    Log.i("Register User: ", "The user is not registered.")
                                }
                        }
                    }
                    .addOnFailureListener {
                        _authState.value = AuthResponse.Failure("Failed to create new User in auth.")
                    }
            }catch (e: Exception){
                _authState.value = AuthResponse.Failure("Can't register the user ${e.message.toString()}")
                Log.i("Error in Registering:", e.message.toString())
            }
        }
    }

    fun logout(){
        auth.signOut()
    }

    fun fetchCurrentUser(){
        viewModelScope.launch {
            val currUser = auth.currentUser
            if (currUser != null){
                val userId = currUser.uid
                firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener {user->
                        if(user!=null){
                            val userInfo = user.toObject(UserInfo::class.java)
                            _currentUser.value = userInfo
                        }
                    }
                    .addOnFailureListener{
                        Log.i("The User: ", "User can't fetched from the database.")
                    }

            }
        }
    }

    fun updateUserInformation(userInfo: UserInfo){
        viewModelScope.launch {
            val currUser = auth.currentUser
            if (currUser!=null){
                val userId = currUser.uid
                val updatedUser = UserInfo(
                    name = userInfo.name,
                    email = userInfo.email,
                    profilePictureUrl = userInfo.profilePictureUrl,
                )

                firestore.collection("users")
                    .document(userId)
                    .set(updatedUser)
                    .addOnSuccessListener {
                        fetchCurrentUser()
                        Log.i("The User update: ", "User updated successfully!")
                    }
                    .addOnFailureListener{
                        Log.i("The User update: ", "User is not updated successfully.")
                    }

            }
        }
    }

    fun updateProfileImage(uri: Uri){
        val currentUser = auth.currentUser
        if (currentUser!=null){
            val userId = currentUser.uid
            val imageRef = storage.reference.child("users/${userId}/profile.jpg")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        val imageLink = it.toString()

                        val userData = hashMapOf(
                            "profilePicture" to imageLink)
                        firestore.collection("users")
                            .document(userId)
                            .update(userData as Map<String, Any>)
                            .addOnSuccessListener {
                                fetchCurrentUser()
                            }
                        Log.i("The profile picture: ", "The picture is updated successfully.")
                    }
                }
                .addOnFailureListener{
                    Log.i("Error Encountered: ", "Unable to update the profile picture. ${it}")
                }
        }else{
            Log.i("Error update:", "Current User is null")
        }
    }
}