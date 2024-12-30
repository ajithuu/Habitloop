package uk.ac.tees.mad.habitloop.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.habitloop.room.dao.HabitDao
import uk.ac.tees.mad.habitloop.room.entity.HabitInfoEnt


@Database(
    entities = [HabitInfoEnt::class],
    version = 1
)
abstract class HabitDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao
}
