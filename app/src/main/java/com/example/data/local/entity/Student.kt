package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rollNumber: String,
    val name: String,
    val grade: String, // e.g. "Grade 9-A"
    val fatherName: String,
    val parentPhone: String, // e.g. "+923155261941" or parent mobile
    val secondaryPhone: String = "",
    val gender: String = "Male",
    val address: String = "Garhi Kapoora, Mardan",
    val emergencyContactName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
