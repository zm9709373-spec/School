package com.example

import com.example.data.local.entity.Student
import com.example.data.local.entity.Teacher
import org.junit.Assert.assertEquals
import org.junit.Test

class SchoolDataUnitTest {

    @Test
    fun testStudentCreation() {
        val student = Student(
            id = 1,
            rollNumber = "NS-101",
            name = "Hamza Tariq",
            grade = "Grade 9-Science",
            fatherName = "Tariq Mehmood",
            parentPhone = "+923155261941"
        )
        assertEquals("Hamza Tariq", student.name)
        assertEquals("NS-101", student.rollNumber)
        assertEquals("Grade 9-Science", student.grade)
    }

    @Test
    fun testTeacherCreation() {
        val teacher = Teacher(
            id = 1,
            name = "Sir Muhammad Tariq",
            subject = "Mathematics",
            assignedGrade = "Grade 9 & 10",
            phone = "+92 3155261941"
        )
        assertEquals("Sir Muhammad Tariq", teacher.name)
        assertEquals("Mathematics", teacher.subject)
    }
}
