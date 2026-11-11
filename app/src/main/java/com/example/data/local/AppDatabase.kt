package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ComplaintDao
import com.example.data.local.dao.StudentDao
import com.example.data.local.dao.TeacherDao
import com.example.data.local.entity.Complaint
import com.example.data.local.entity.Student
import com.example.data.local.entity.Teacher

@Database(
    entities = [Student::class, Teacher::class, Complaint::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun teacherDao(): TeacherDao
    abstract fun complaintDao(): ComplaintDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "neo_suffah_school.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
