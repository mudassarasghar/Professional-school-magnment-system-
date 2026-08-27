package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entities.HomeworkEntity
import com.example.data.local.entities.StudentEntity
import com.example.domain.model.GradeCalculator
import com.example.ui.components.ReportCardDialog
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun TeacherDashboard(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val selectedClass by viewModel.selectedClass.collectAsState()
    val allStudents by viewModel.students.collectAsState()
    val attendanceToday by viewModel.attendanceToday.collectAsState()
    val homeworkList by viewModel.homework.collectAsState()
    val allMarks by viewModel.allMarks.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Attendance, 1: Marksheet & Report Cards, 2: Homework & Syllabus
    var showMarksheetDialog by remember { mutableStateOf<StudentEntity?>(null) }
    var showReportCardDialog by remember { mutableStateOf<StudentEntity?>(null) }
    var showHomeworkDialog by remember { mutableStateOf(false) }

    val classList = listOf("Grade 10", "Grade 9", "Grade 8", "Grade 5", "Prep", "Nursery")
    val filteredStudents = allStudents.filter { it.className == selectedClass }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Teacher identity strip
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Teacher Faculty Dashboard",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                        )
                        Text(
                            text = "Instructor: Mr. Tariq Mahmood (Science & Physics)",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SchoolTealSecondary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Wan Khara Campus",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SchoolTealSecondary,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Class Selector Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    classList.forEach { cls ->
                        val isSelected = cls == selectedClass
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSelectedClass(cls) },
                            label = { Text(cls, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SchoolNavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Sub Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SchoolNavyPrimary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Daily Attendance") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Marksheet & Grades") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Homework Portal") })
        }

        when (selectedTab) {
            0 -> {
                // Daily Attendance Manager with Instant Alert
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SchoolNavyLight.copy(alpha = 0.08f)),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$selectedClass Attendance Roster",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Date: 2026-08-27 • Tap status to toggle & trigger parent SMS",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight, fontSize = 11.sp)
                                )
                            }
                            Icon(Icons.Default.TouchApp, contentDescription = null, tint = SchoolNavyPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (filteredStudents.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No students currently enrolled in $selectedClass.", color = TextMutedLight)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredStudents) { student ->
                                val attendanceRecord = attendanceToday.find { it.studentId == student.id }
                                val currentStatus = attendanceRecord?.status ?: "Present"

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = CardDefaults.outlinedCardBorder()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(SchoolNavyPrimary.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${student.rollNo}",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = SchoolNavyPrimary
                                                )
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(student.fullName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                            Text("Parent: ${student.parentPhone}", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                            if (attendanceRecord?.alertSent == true) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(12.dp), tint = StatusAbsentRed)
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("SMS Dispatched", style = MaterialTheme.typography.labelSmall.copy(color = StatusAbsentRed, fontSize = 9.sp))
                                                }
                                            }
                                        }

                                        // Status Selector Buttons
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            listOf("Present", "Absent", "Late", "Excused").forEach { statusOption ->
                                                val isSelected = currentStatus.equals(statusOption, ignoreCase = true)
                                                val (color, bgColor) = when (statusOption) {
                                                    "Present" -> StatusPresentGreen to StatusPresentBg
                                                    "Absent" -> StatusAbsentRed to StatusAbsentBg
                                                    "Late" -> StatusLateYellow to StatusLateBg
                                                    else -> StatusExcusedBlue to StatusExcusedBg
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = if (isSelected) color else bgColor.copy(alpha = 0.4f),
                                                    modifier = Modifier.testTag("btn_att_${student.id}_${statusOption.lowercase()}")
                                                ) {
                                                    TextButton(
                                                        onClick = {
                                                            viewModel.updateAttendanceStatus(student, statusOption)
                                                        },
                                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                                        modifier = Modifier.height(30.dp)
                                                    ) {
                                                        Text(
                                                            text = statusOption.take(1),
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                fontWeight = FontWeight.Bold,
                                                                color = if (isSelected) Color.White else color
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Marksheet Entry & Report Card Generation
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SchoolNavyPrimary.copy(alpha = 0.06f)),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Academic Evaluation & Report Cards",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Auto calculates Grade, Percentage & GPA with instant PDF preview",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight, fontSize = 11.sp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredStudents) { student ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(student.fullName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                            Text("Roll #${student.rollNo} • Reg: ${student.regNo}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            OutlinedButton(
                                                onClick = {
                                                    viewModel.setSelectedStudent(student.id)
                                                    showReportCardDialog = student
                                                },
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                modifier = Modifier.testTag("btn_report_card_${student.id}")
                                            ) {
                                                Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Report Card", fontSize = 11.sp)
                                            }

                                            Button(
                                                onClick = { showMarksheetDialog = student },
                                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                modifier = Modifier.testTag("btn_enter_marks_${student.id}")
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("+ Marks", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Homework & Syllabus Portal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Homework & Syllabus Assignments",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { showHomeworkDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolTealSecondary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Assign Homework", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(homeworkList) { hw ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(hw.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = SchoolNavyLight.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = "${hw.className} • ${hw.subject}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = SchoolNavyPrimary, fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(hw.description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Due Date: ${hw.dueDate}", style = MaterialTheme.typography.labelSmall.copy(color = StatusAbsentRed, fontWeight = FontWeight.Medium))
                                        Text("Teacher: ${hw.teacherName}", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Mark Entry Dialog
    showMarksheetDialog?.let { student ->
        MarksEntryDialog(
            student = student,
            onDismiss = { showMarksheetDialog = null },
            onSubmit = { exam, subject, obtained, total, remarks ->
                viewModel.addMarkRecord(student, exam, subject, obtained, total, remarks)
                showMarksheetDialog = null
            }
        )
    }

    // Report Card Dialog
    showReportCardDialog?.let { student ->
        ReportCardDialog(
            student = student,
            marks = allMarks,
            onDismiss = { showReportCardDialog = null }
        )
    }

    // Homework Creation Dialog
    if (showHomeworkDialog) {
        HomeworkFormDialog(
            defaultClass = selectedClass,
            onDismiss = { showHomeworkDialog = false },
            onSubmit = { cls, subj, title, desc, due ->
                viewModel.postHomework(cls, subj, title, desc, due, "Mr. Tariq Mahmood")
                showHomeworkDialog = false
            }
        )
    }
}

@Composable
fun MarksEntryDialog(
    student: StudentEntity,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Double, Double, String) -> Unit
) {
    var examTitle by remember { mutableStateOf("First Term Assessment 2026") }
    var subject by remember { mutableStateOf("Physics") }
    var marksObtainedStr by remember { mutableStateOf("90") }
    var totalMarksStr by remember { mutableStateOf("100") }
    var remarks by remember { mutableStateOf("Good performance in theory and numericals") }

    val obtained = marksObtainedStr.toDoubleOrNull() ?: 0.0
    val total = totalMarksStr.toDoubleOrNull() ?: 100.0
    val preview = GradeCalculator.calculateGrade(obtained, total)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "Enter Examination Marks",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                )
                Text(
                    text = "Student: ${student.fullName} (${student.className}-${student.section})",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = examTitle,
                    onValueChange = { examTitle = it },
                    label = { Text("Exam Assessment Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject (e.g. Physics, Mathematics, English)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = marksObtainedStr,
                        onValueChange = { marksObtainedStr = it },
                        label = { Text("Marks Obtained *") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_marks_obtained")
                    )
                    OutlinedTextField(
                        value = totalMarksStr,
                        onValueChange = { totalMarksStr = it },
                        label = { Text("Total Marks") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Real-time Grade Calculation Box
                Card(
                    colors = CardDefaults.cardColors(containerColor = SchoolNavyPrimary.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Calculated Grade: ${preview.grade}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
                            Text("Score: ${"%.1f".format(preview.percentage)}% | GPA: ${preview.gpa}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                        }
                        StatusBadge(status = preview.status)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Teacher Remarks") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (obtained >= 0.0 && total > 0.0) {
                                onSubmit(examTitle, subject, obtained, total, remarks)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                        modifier = Modifier.testTag("btn_save_mark")
                    ) {
                        Text("Save & Calculate")
                    }
                }
            }
        }
    }
}

@Composable
fun HomeworkFormDialog(
    defaultClass: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var className by remember { mutableStateOf(defaultClass) }
    var subject by remember { mutableStateOf("Physics") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("2026-08-30") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "Assign New Homework / Task",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = className, onValueChange = { className = it }, label = { Text("Class") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Homework Title *") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Instructions & Exercises *") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = dueDate, onValueChange = { dueDate = it }, label = { Text("Submission Due Date") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank() && description.isNotBlank()) {
                                onSubmit(className, subject, title, description, dueDate)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolTealSecondary)
                    ) {
                        Text("Post Assignment")
                    }
                }
            }
        }
    }
}
