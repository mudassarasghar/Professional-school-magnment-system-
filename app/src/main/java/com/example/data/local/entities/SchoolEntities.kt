package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val regNo: String, // e.g. PLA-2026-0042
    val fullName: String,
    val gender: String, // Male / Female
    val dob: String,
    val gradeLevel: String, // Pre-Primary, Primary, Middle, Secondary
    val className: String, // e.g. Grade 10
    val section: String, // A, B
    val rollNo: Int,
    val parentName: String,
    val parentPhone: String,
    val parentEmail: String,
    val emergencyContact: String,
    val medicalNotes: String = "None / Fully Fit",
    val address: String = "Wan Khara",
    val admissionDate: String,
    val status: String = "Active" // Active, Alumni, Suspended
)

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val staffId: String, // PLA-EMP-01
    val name: String,
    val role: String, // Principal, Teacher, Accountant, Admin
    val designation: String,
    val email: String,
    val phone: String,
    val assignedClass: String = "All",
    val assignedSubjects: String = "General",
    val monthlySalary: Double = 45000.0,
    val joinDate: String
)

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val className: String,
    val section: String,
    val rollNo: Int,
    val date: String, // YYYY-MM-DD
    val status: String, // Present, Absent, Late, Excused
    val remarks: String = "",
    val alertSent: Boolean = false // Automated SMS / Push alert
)

@Entity(tableName = "exam_schedules")
data class ExamScheduleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val examTitle: String, // e.g. Mid-Term Examination 2026
    val gradeLevel: String,
    val className: String,
    val subject: String,
    val examDate: String,
    val startTime: String,
    val endTime: String,
    val roomNo: String,
    val totalMarks: Double = 100.0,
    val syllabusCoverage: String = "Chapters 1 - 5"
)

@Entity(tableName = "marks")
data class MarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val className: String,
    val examTitle: String,
    val subject: String,
    val marksObtained: Double,
    val totalMarks: Double = 100.0,
    val grade: String, // A+, A, B, C, D, F
    val percentage: Double,
    val gradePoint: Double, // 4.0 scale
    val teacherRemarks: String = "Satisfactory performance"
)

@Entity(tableName = "fee_structures")
data class FeeStructureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gradeLevel: String, // Pre-Primary, Primary, Middle, Secondary
    val className: String,
    val tuitionFee: Double,
    val admissionFee: Double = 5000.0,
    val examFee: Double = 1200.0,
    val transportFee: Double = 2500.0,
    val sportsFee: Double = 800.0,
    val labFee: Double = 1000.0
)

@Entity(tableName = "fee_vouchers")
data class FeeVoucherEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val voucherNo: String, // VCH-2026-08-0101
    val studentId: Long,
    val studentName: String,
    val regNo: String,
    val className: String,
    val section: String,
    val monthYear: String, // e.g. August 2026
    val issueDate: String,
    val dueDate: String,
    val tuitionFee: Double,
    val admissionFee: Double = 0.0,
    val transportFee: Double = 0.0,
    val examFee: Double = 0.0,
    val sportsFee: Double = 0.0,
    val otherCharges: Double = 0.0,
    val discount: Double = 0.0,
    val totalAmount: Double,
    val amountPaid: Double = 0.0,
    val paymentStatus: String = "Pending", // Paid, Partial, Overdue, Pending
    val paymentDate: String? = null,
    val paymentMethod: String? = null, // Cash, Bank Transfer, Online Portal, Easypaisa/JazzCash
    val transactionRef: String? = null
)

@Entity(tableName = "notices")
data class NoticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Academic, Event, Holiday, Urgent
    val content: String,
    val publishDate: String,
    val targetAudience: String = "All", // All, Parents, Teachers, Students
    val priority: String = "Normal", // High, Normal, Low
    val author: String = "Principal Office"
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderRole: String,
    val senderName: String,
    val receiverRole: String,
    val receiverName: String,
    val subject: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)

@Entity(tableName = "homework")
data class HomeworkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val className: String,
    val subject: String,
    val title: String,
    val description: String,
    val assignedDate: String,
    val dueDate: String,
    val teacherName: String,
    val submissionCount: Int = 0
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // Payroll, Utilities, Maintenance, Lab, Stationery
    val amount: Double,
    val expenseDate: String,
    val recordedBy: String,
    val receiptNo: String,
    val notes: String = ""
)
