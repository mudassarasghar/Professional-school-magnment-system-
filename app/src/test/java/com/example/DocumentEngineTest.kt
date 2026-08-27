package com.example

import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.MarkEntity
import com.example.data.local.entities.StaffEntity
import com.example.data.local.entities.StudentEntity
import com.example.ui.components.DocumentHtmlGenerator
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentEngineTest {

    private val sampleStudent = StudentEntity(
        id = 1,
        regNo = "PLA-2026-001",
        fullName = "Muhammad Ali",
        gender = "Male",
        dob = "2010-05-15",
        gradeLevel = "Secondary",
        className = "Class 10",
        section = "A",
        rollNo = 101,
        parentName = "Tariq Mahmood",
        parentPhone = "+92 300 1234567",
        parentEmail = "tariq@gmail.com",
        emergencyContact = "+92 300 9876543",
        admissionDate = "2020-04-01"
    )

    private val sampleVoucher = FeeVoucherEntity(
        voucherNo = "VCH-2026-08-001",
        studentId = 1,
        regNo = "PLA-2026-001",
        studentName = "Muhammad Ali",
        className = "Class 10",
        section = "A",
        monthYear = "August 2026",
        issueDate = "01-Aug-2026",
        dueDate = "10-Aug-2026",
        tuitionFee = 3500.0,
        transportFee = 1200.0,
        sportsFee = 300.0,
        admissionFee = 0.0,
        examFee = 500.0,
        otherCharges = 200.0,
        discount = 500.0,
        totalAmount = 5200.0,
        amountPaid = 5200.0,
        paymentStatus = "Paid"
    )

    private val sampleStaff = StaffEntity(
        id = 1,
        staffId = "PLA-STAFF-01",
        name = "Prof. Rauf Ahmad",
        designation = "Principal / Senior Physics Faculty",
        role = "Admin",
        email = "principal@paradiselittleangels.edu.pk",
        phone = "+92 300 4892100",
        assignedClass = "Class 10-A",
        assignedSubjects = "Physics, Mathematics",
        monthlySalary = 85000.0,
        joinDate = "2015-08-01"
    )

    @Test
    fun testGenerateStudentIdCardHtml() {
        val html = DocumentHtmlGenerator.generateStudentIdCardHtml(sampleStudent)
        assertTrue(html.contains("PARADISE LITTLE ANGELS SECONDARY SCHOOL"))
        assertTrue(html.contains("Muhammad Ali"))
        assertTrue(html.contains("PLA-2026-001"))
        assertTrue(html.contains("Class 10"))
        assertTrue(html.contains("Tariq Mahmood"))
        assertTrue(html.contains("@media print"))
    }

    @Test
    fun testGenerateFeeVoucherHtml() {
        val html = DocumentHtmlGenerator.generateFeeVoucherHtml(sampleVoucher)
        assertTrue(html.contains("PARADISE LITTLE ANGELS SECONDARY SCHOOL"))
        assertTrue(html.contains("VCH-2026-08-001"))
        assertTrue(html.contains("Rs. 5200"))
        assertTrue(html.contains("August 2026"))
        assertTrue(html.contains("Triplicate System"))
    }

    @Test
    fun testGenerateReportCardHtml() {
        val marks = listOf(
            MarkEntity(studentId = 1, studentName = "Muhammad Ali", className = "Class 10", examTitle = "Mid-Term Examination 2026", subject = "Physics", totalMarks = 100.0, marksObtained = 92.0, grade = "A+", percentage = 92.0, gradePoint = 4.0, teacherRemarks = "Outstanding mastery"),
            MarkEntity(studentId = 1, studentName = "Muhammad Ali", className = "Class 10", examTitle = "Mid-Term Examination 2026", subject = "Mathematics", totalMarks = 100.0, marksObtained = 95.0, grade = "A+", percentage = 95.0, gradePoint = 4.0, teacherRemarks = "Excellent calculus work")
        )
        val html = DocumentHtmlGenerator.generateReportCardHtml(sampleStudent, marks)
        assertTrue(html.contains("Detailed Marks Certificate"))
        assertTrue(html.contains("Muhammad Ali"))
        assertTrue(html.contains("Physics"))
        assertTrue(html.contains("Mathematics"))
        assertTrue(html.contains("Grade: A+"))
        assertTrue(html.contains("Prof. Rauf Ahmad"))
    }

    @Test
    fun testGenerateCharacterCertificateHtml() {
        val html = DocumentHtmlGenerator.generateCharacterCertificateHtml(sampleStudent)
        assertTrue(html.contains("Character & Conduct Certificate"))
        assertTrue(html.contains("Muhammad Ali"))
        assertTrue(html.contains("EXCELLENT and exemplary"))
        assertTrue(html.contains("Prof. Rauf Ahmad"))
    }

    @Test
    fun testGenerateAdmitCardHtml() {
        val html = DocumentHtmlGenerator.generateAdmitCardHtml(sampleStudent)
        assertTrue(html.contains("Examination Roll No Slip"))
        assertTrue(html.contains("Muhammad Ali"))
        assertTrue(html.contains("#101"))
        assertTrue(html.contains("Main Academic Hall, Wan Khara"))
    }

    @Test
    fun testGenerateSalarySlipHtml() {
        val html = DocumentHtmlGenerator.generateSalarySlipHtml(sampleStaff)
        assertTrue(html.contains("Faculty Salary Slip"))
        assertTrue(html.contains("Prof. Rauf Ahmad"))
        assertTrue(html.contains("PLA-STAFF-01"))
        assertTrue(html.contains("Rs. 85000"))
    }
}

