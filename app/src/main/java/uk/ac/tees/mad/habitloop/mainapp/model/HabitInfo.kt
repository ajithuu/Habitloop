package uk.ac.tees.mad.habitloop.mainapp.model


data class HabitInfo(
    val name: String = "",
    val goal: String = "",
    val schedule: String = "",
    val completed: Boolean = false
)
