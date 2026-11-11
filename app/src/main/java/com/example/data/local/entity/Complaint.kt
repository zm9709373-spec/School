package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complaints")
data class Complaint(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val studentRoll: String,
    val grade: String,
    val parentPhone: String,
    val teacherId: Long,
    val teacherName: String,
    val subject: String,
    val category: String, // e.g. "Behavior", "Academic", "Attendance", "Homework", "Discipline"
    val severity: String, // "Low", "Moderate", "Critical"
    val title: String,
    val description: String,
    val expectedAction: String, // e.g. "Visit School for Teacher Meeting", "Supervise Homework"
    val status: String = "Pending / Sent", // "Pending / Sent", "Parent Acknowledged", "Meeting Scheduled", "Resolved"
    val parentFeedback: String = "",
    val incidentDate: String, // e.g. "10 Nov 2026"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isResolved: Boolean = false
)
