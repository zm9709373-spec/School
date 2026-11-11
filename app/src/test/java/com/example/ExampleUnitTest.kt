package com.example

import com.example.data.local.entity.Complaint
import com.example.util.CommunicationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testOfficialNoticeFormatting() {
        val complaint = Complaint(
            id = 1,
            studentId = 101,
            studentName = "Hamza Tariq",
            studentRoll = "NS-101",
            grade = "Grade 9-Science",
            parentPhone = "+923155261941",
            teacherId = 1,
            teacherName = "Sir Muhammad Tariq",
            subject = "Mathematics",
            category = "Academic Performance",
            severity = "Moderate",
            title = "Low Test Score",
            description = "Scored 11/50 in Algebra test.",
            expectedAction = "Check daily diary",
            incidentDate = "10 Nov 2026"
        )

        val notice = CommunicationHelper.buildOfficialNoticeText(complaint)
        assertTrue(notice.contains("THE NEO SUFFAH MODEL SCHOOL"))
        assertTrue(notice.contains("Garhi Daulat Zai, Tango Adda Garhi Kapoora Mardan"))
        assertTrue(notice.contains("+92 3155261941"))
        assertTrue(notice.contains("Hamza Tariq"))
        assertTrue(notice.contains("Mathematics"))
    }

    @Test
    fun testPhoneNumberSanitization() {
        val formatted = CommunicationHelper.sanitizePhoneNumber("0315-5261941")
        assertEquals("+923155261941", formatted)
    }
}
