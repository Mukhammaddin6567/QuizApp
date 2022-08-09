package uz.gita.quizappMBF.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    /*@Query("select * from entity")
    fun getAllHistory(): List<Entity>*/

    @Insert
    fun addNewData(data: Entity)

    @Query("select * from entity")
    fun getAllData(): List<Entity>

    @Query("select * from entity order by date desc")
    fun getAllDataBySortingDate(): List<Entity>

    @Query("update entity set coins = coins + :value where date=:date")
    fun updateCoinsByDate(date: String, value: Int)

    @Query("update entity set spin_count = spin_count - 1 where date=:date")
    fun updateSpinCountByDate(date: String)

    @Query("select exists(select * from entity where date=(:date))")
    fun isCurrentDayAvailable(date: String): Boolean

    @Query("select spin_count from entity where date =:date ")
    fun getCurrentSpinCount(date: String): Int

    @Query("select SUM(coins) as total from entity")
    fun getTotalSum(): Int

    @Query("delete from entity")
    fun clearData()
}