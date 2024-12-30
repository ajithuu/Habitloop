package uk.ac.tees.mad.habitloop.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class HabitInfoEnt(
    @PrimaryKey
    val id: Int,
    val name: String = "",
    val goal: String = "",
    val schedule: String = "",
    val completed: Boolean = false
)