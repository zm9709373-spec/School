package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teachers")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String, // e.g. "Sir Muhammad Tariq"
    val subject: String, // e.g. "Mathematics & Physics"
    val assignedGrade: String, // e.g. "Grade 9 & 10"
    val phone: String = "+92 3155261941",
    val email: String = "",
    val employeeCode: String = "TCH-01"
)
