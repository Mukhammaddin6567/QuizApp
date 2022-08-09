package uz.gita.quizappMBF.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity")
data class Entity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "coins")
    val coins: Int,
    @ColumnInfo(name ="spin_count")
    val dailySpinCount: Int
)