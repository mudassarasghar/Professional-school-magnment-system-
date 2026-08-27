package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ArchitectureAndSchemaScreen(
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(
            color = SchoolNavyPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "System Architecture & Database Blueprint",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                )
                Text(
                    text = "Paradise Little Angels Secondary School, Wan Khara — Technical Deliverables",
                    style = MaterialTheme.typography.bodySmall.copy(color = SchoolGoldLight, fontSize = 11.sp)
                )
            }
        }

        TabRow(
            selectedTabIndex = selectedSection,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SchoolNavyPrimary
        ) {
            Tab(selected = selectedSection == 0, onClick = { selectedSection = 0 }, text = { Text("Architecture") })
            Tab(selected = selectedSection == 1, onClick = { selectedSection = 1 }, text = { Text("SQL Schema") })
            Tab(selected = selectedSection == 2, onClick = { selectedSection = 2 }, text = { Text("Core Logic") })
            Tab(selected = selectedSection == 3, onClick = { selectedSection = 3 }, text = { Text("Testing Spec") })
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedSection) {
                0 -> {
                    item {
                        BlueprintCard(
                            title = "1. Technical Stack Outline",
                            icon = Icons.Default.Layers
                        ) {
                            Text("• User Interface: Jetpack Compose with Material Design 3 (M3)", style = MaterialTheme.typography.bodySmall)
                            Text("• Architecture: Clean MVVM (Model-View-ViewModel) with StateFlow reactive streams", style = MaterialTheme.typography.bodySmall)
                            Text("• Persistence: Room 2.7.0 SQLite Database with KSP (Kotlin Symbol Processing)", style = MaterialTheme.typography.bodySmall)
                            Text("• Language & Runtime: Kotlin 2.2 with Coroutines & Flow concurrency", style = MaterialTheme.typography.bodySmall)
                            Text("• Notifications: Automated Absence SMS dispatch simulation to parent mobile phones", style = MaterialTheme.typography.bodySmall)
                            Text("• Document Engine: Formatted Printable Fee Vouchers & Terminal Academic Progress Cards", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    item {
                        BlueprintCard(
                            title = "2. User Roles & Permission Matrix",
                            icon = Icons.Default.Security
                        ) {
                            RoleMatrixRow("Super Admin / Principal", "Full SIS admission, staff payroll oversight, system logs, financial audit, notice publishing")
                            RoleMatrixRow("Teachers", "Class roster management, 1-tap attendance marking, marksheet entry, automated GPA calculation, homework assignment")
                            RoleMatrixRow("Students", "Profile access, attendance statistics, exam timetable, report cards, homework portal, fee receipts")
                            RoleMatrixRow("Parents", "Child academic monitoring, absence alert history, fee voucher settlement, 2-way messaging with teachers")
                            RoleMatrixRow("Accountant", "Fee structure configuration, batch monthly voucher generator, cash/online collection, school expenses & payroll")
                        }
                    }
                }

                1 -> {
                    item {
                        BlueprintCard(
                            title = "SQL Database Schema & Entity Relationships",
                            icon = Icons.Default.Storage
                        ) {
                            SqlBlock(
                                """
CREATE TABLE students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    regNo TEXT UNIQUE NOT NULL,      -- e.g. PLA-2026-0042
    fullName TEXT NOT NULL,
    gender TEXT NOT NULL,
    dob TEXT NOT NULL,
    gradeLevel TEXT NOT NULL,       -- Pre-Primary, Primary, Middle, Secondary
    className TEXT NOT NULL,        -- e.g. Grade 10
    section TEXT NOT NULL,          -- A, B
    rollNo INTEGER NOT NULL,
    parentName TEXT NOT NULL,
    parentPhone TEXT NOT NULL,
    parentEmail TEXT,
    emergencyContact TEXT NOT NULL,
    medicalNotes TEXT,
    address TEXT,
    admissionDate TEXT NOT NULL,
    status TEXT DEFAULT 'Active'
);

CREATE TABLE attendance (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    studentId INTEGER NOT NULL,
    studentName TEXT NOT NULL,
    className TEXT NOT NULL,
    section TEXT NOT NULL,
    rollNo INTEGER NOT NULL,
    date TEXT NOT NULL,             -- YYYY-MM-DD
    status TEXT NOT NULL,           -- Present, Absent, Late, Excused
    remarks TEXT,
    alertSent INTEGER DEFAULT 0,
    FOREIGN KEY(studentId) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE marks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    studentId INTEGER NOT NULL,
    studentName TEXT NOT NULL,
    className TEXT NOT NULL,
    examTitle TEXT NOT NULL,
    subject TEXT NOT NULL,
    marksObtained REAL NOT NULL,
    totalMarks REAL NOT NULL DEFAULT 100.0,
    grade TEXT NOT NULL,            -- A+, A, B, C, D, F
    percentage REAL NOT NULL,
    gradePoint REAL NOT NULL,       -- 4.0 Scale
    teacherRemarks TEXT,
    FOREIGN KEY(studentId) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE fee_vouchers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    voucherNo TEXT UNIQUE NOT NULL, -- e.g. PLA-VCH-202608-0042
    studentId INTEGER NOT NULL,
    studentName TEXT NOT NULL,
    regNo TEXT NOT NULL,
    className TEXT NOT NULL,
    section TEXT NOT NULL,
    monthYear TEXT NOT NULL,
    issueDate TEXT NOT NULL,
    dueDate TEXT NOT NULL,
    tuitionFee REAL NOT NULL,
    admissionFee REAL DEFAULT 0.0,
    transportFee REAL DEFAULT 0.0,
    examFee REAL DEFAULT 0.0,
    sportsFee REAL DEFAULT 0.0,
    otherCharges REAL DEFAULT 0.0,
    discount REAL DEFAULT 0.0,
    totalAmount REAL NOT NULL,
    amountPaid REAL DEFAULT 0.0,
    paymentStatus TEXT NOT NULL,     -- Paid, Partial, Overdue, Pending
    paymentDate TEXT,
    paymentMethod TEXT,
    transactionRef TEXT,
    FOREIGN KEY(studentId) REFERENCES students(id)
);
                                """.trimIndent()
                            )
                        }
                    }
                }

                2 -> {
                    item {
                        BlueprintCard(
                            title = "Core Business Logic & Algorithms",
                            icon = Icons.Default.Calculate
                        ) {
                            Text("1. Automated Grade & GPA Calculation Engine:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            SqlBlock(
                                """
fun calculateGrade(obtained: Double, total: Double): GradeResult {
    val percentage = (obtained / total) * 100.0
    return when {
        percentage >= 90.0 -> GradeResult("A+", 4.0, "Outstanding Academic Excellence")
        percentage >= 80.0 -> GradeResult("A",  3.7, "Excellent Mastery of Concepts")
        percentage >= 70.0 -> GradeResult("B",  3.0, "Good Progress & Performance")
        percentage >= 60.0 -> GradeResult("C",  2.3, "Satisfactory / Revision Recommended")
        percentage >= 50.0 -> GradeResult("D",  1.7, "Pass / Consistent Effort Needed")
        else               -> GradeResult("F",  0.0, "Fail / Remedial Coaching Required")
    }
}
                                """.trimIndent()
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("2. Automated Absence SMS Alert Trigger:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            SqlBlock(
                                """
if (attendanceStatus == "Absent") {
    val alertMessage = "Dear Parent of ${'$'}studentName, your child was marked absent today (${'$'}date) at Paradise Little Angels Secondary School Wan Khara. Contact Principal Office for inquiries."
    smsGateway.dispatchSms(student.parentPhone, alertMessage)
    record.copy(alertSent = true)
}
                                """.trimIndent()
                            )
                        }
                    }
                }

                3 -> {
                    item {
                        BlueprintCard(
                            title = "Testing Protocol & Quality Assurance",
                            icon = Icons.Default.FactCheck
                        ) {
                            Text("• Unit Test 1: Grade boundary calculations across 90%, 80%, 70%, 60%, 50%, and <50% edge cases.", style = MaterialTheme.typography.bodySmall)
                            Text("• Unit Test 2: Fee Voucher balance reconciliation when partial payments are posted.", style = MaterialTheme.typography.bodySmall)
                            Text("• Integration Test: Room SQLite DAO insert, reactive Flow queries, and cascade deletion.", style = MaterialTheme.typography.bodySmall)
                            Text("• UI Test / Roborazzi: Screenshot regression testing of the official Fee Voucher & Terminal Report Card layouts.", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BlueprintCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = SchoolNavyPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun RoleMatrixRow(role: String, desc: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(role, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SchoolTealSecondary))
        Text(desc, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), color = SurfaceBorderLight.copy(alpha = 0.5f))
    }
}

@Composable
private fun SqlBlock(code: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1E293B)
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFE2E8F0),
                fontSize = 11.sp,
                lineHeight = 16.sp
            ),
            modifier = Modifier.padding(10.dp)
        )
    }
}
