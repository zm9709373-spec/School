package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.local.entity.Complaint
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object CommunicationHelper {

    const val SCHOOL_NAME = "The Neo Suffah Model School"
    const val SCHOOL_ADDRESS = "Garhi Daulat Zai, Tango Adda Garhi Kapoora Mardan"
    const val SCHOOL_PHONE = "+92 3155261941"
    const val SCHOOL_PHONE_DIAL = "+923155261941"

    fun buildOfficialNoticeText(complaint: Complaint): String {
        return """
🏫 *THE NEO SUFFAH MODEL SCHOOL*
📍 $SCHOOL_ADDRESS
📞 Official Contact: $SCHOOL_PHONE
────────────────────────
📋 *OFFICIAL STUDENT CONDUCT & PERFORMANCE REPORT*

👤 *Student Name:* ${complaint.studentName}
🔢 *Roll No:* ${complaint.studentRoll}
🎓 *Class / Grade:* ${complaint.grade}
👨‍🏫 *Reporting Teacher:* ${complaint.teacherName} (${complaint.subject})
⚠️ *Issue Category:* ${complaint.category}
🚨 *Priority Level:* ${complaint.severity.uppercase()}
📅 *Date:* ${complaint.incidentDate}

📌 *Subject / Title:*
${complaint.title}

📝 *Teacher's Statement:*
${complaint.description}

🎯 *Required Action by Parent:*
${complaint.expectedAction.ifBlank { "Please inspect child's daily school diary and contact school administration if required." }}

Current Status: ${complaint.status}

Kindly acknowledge this notice. For appointment or inquiries, please contact:
📞 School Administration: $SCHOOL_PHONE
────────────────────────
_Knowledge is Power • علم بڑی دولت ہے_
""".trimIndent()
    }

    fun sanitizePhoneNumber(raw: String): String {
        val digits = raw.replace(Regex("[^0-9+]"), "")
        return if (digits.startsWith("0")) {
            // Convert Pakistani local format 03xx to +923xx
            "+92" + digits.substring(1)
        } else if (!digits.startsWith("+") && digits.startsWith("92")) {
            "+$digits"
        } else {
            digits
        }
    }

    fun openWhatsAppNotice(context: Context, complaint: Complaint) {
        val rawPhone = complaint.parentPhone.ifBlank { SCHOOL_PHONE }
        val sanitized = sanitizePhoneNumber(rawPhone).replace("+", "")
        val message = buildOfficialNoticeText(complaint)
        try {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$sanitized&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to generic share
            shareText(context, message)
        }
    }

    fun dialParent(context: Context, rawPhone: String) {
        val phone = rawPhone.ifBlank { SCHOOL_PHONE_DIAL }
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open phone dialer", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendSmsNotice(context: Context, complaint: Complaint) {
        val rawPhone = complaint.parentPhone.ifBlank { SCHOOL_PHONE_DIAL }
        val message = buildOfficialNoticeText(complaint)
        try {
            val uri = Uri.parse("smsto:$rawPhone")
            val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
                putExtra("sms_body", message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            shareText(context, message)
        }
    }

    fun shareText(context: Context, text: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(intent, "Share Student Report via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to share message", Toast.LENGTH_SHORT).show()
        }
    }
}
