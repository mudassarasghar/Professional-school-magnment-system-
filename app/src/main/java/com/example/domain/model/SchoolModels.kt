package com.example.domain.model

enum class UserRole(val displayName: String, val badgeTitle: String) {
    SUPER_ADMIN("Super Admin / Principal", "Principal Oversight"),
    TEACHER("Teacher", "Academic & Attendance"),
    STUDENT("Student", "Student Portal"),
    PARENT("Parent", "Parent Monitoring"),
    ACCOUNTANT("Accountant", "Fee & Finance"),
    BLUEPRINT("Architecture & Schema", "System Blueprint")
}

data class GradeCalculationResult(
    val percentage: Double,
    val grade: String,
    val gpa: Double,
    val remarks: String,
    val status: String
)

object GradeCalculator {
    fun calculateGrade(marksObtained: Double, totalMarks: Double): GradeCalculationResult {
        if (totalMarks <= 0.0) {
            return GradeCalculationResult(0.0, "N/A", 0.0, "Invalid marks", "Invalid")
        }
        val percentage = (marksObtained / totalMarks) * 100.0
        return when {
            percentage >= 90.0 -> GradeCalculationResult(percentage, "A+", 4.0, "Outstanding Academic Excellence", "Passed")
            percentage >= 80.0 -> GradeCalculationResult(percentage, "A", 3.7, "Excellent Mastery of Concepts", "Passed")
            percentage >= 70.0 -> GradeCalculationResult(percentage, "B", 3.0, "Good Progress & Performance", "Passed")
            percentage >= 60.0 -> GradeCalculationResult(percentage, "C", 2.3, "Satisfactory / Revision Recommended", "Passed")
            percentage >= 50.0 -> GradeCalculationResult(percentage, "D", 1.7, "Pass / Consistent Effort Needed", "Passed")
            else -> GradeCalculationResult(percentage, "F", 0.0, "Fail / Remedial Coaching Required", "Failed")
        }
    }
}
