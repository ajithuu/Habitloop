package uk.ac.tees.mad.habitloop.mainapp.model



sealed class FirebaseResponse {
    object Idle: FirebaseResponse()
    object Loading: FirebaseResponse()
    object Success: FirebaseResponse()
    data class Failure(val message: String): FirebaseResponse()
}
