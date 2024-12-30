package uk.ac.tees.mad.habitloop.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import uk.ac.tees.mad.habitloop.room.entity.HabitInfoEnt


@Dao
interface HabitDao {

    @Upsert
    suspend fun addHabit(habitInfo: List<HabitInfoEnt>)

    @Delete
    suspend fun deleteHabit(habit: HabitInfoEnt)

    @Query("SELECT * FROM habitinfoent")
    suspend fun getAllHabits(): List<HabitInfoEnt>

}