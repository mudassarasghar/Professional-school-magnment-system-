package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.StudentEntity
import com.example.ui.components.FeeVoucherDialog
import com.example.ui.components.ReportCardDialog
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun StudentPortal(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.students.collectAsState()
    val selectedStudentId by viewModel.selectedStudentId.collectAsState()
    val marks by viewModel.allMarks.collectAsState()
    val feeVouchers by viewModel.feeVouchers.collectAsState()
    val examSchedules by viewModel.examSchedules.collectAsState()
    val homeworkList by viewModel.homework.collectAsState()
    val attendanceList by viewModel.attendanceToday.collectAsState()

    val currentStudent = students.find { it.id == selectedStudentId } ?: students.firstOrNull()
    var selectedTab by remember { mutableStateOf(0) } // 0: Profile & Academics, 1: Exam Timetable, 2: Fee Vouchers, 3: Homework
    var showVoucherDialog by remember { mutableStateOf<FeeVoucherEntity?>(null) }
    var showReportCardDialog by remember { mutableStateOf(false) }

    val studentVouchers = feeVouchers.filter { it.studentId == currentStudent?.id }
    val studentAttendance = attendanceList.filter { it.studentId == currentStudent?.id }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (currentStudent == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = SchoolNavyPrimary)
            }
            return
        }

        // Student Switcher Bar (to test different student accounts)
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select Student:", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                students.take(5).forEach { st ->
                    val isSelected = st.id == currentStudent.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedStudent(st.id) },
                        label = { Text("${st.fullName} (${st.className})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SchoolNavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Student Identity Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SchoolNavyPrimary),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_crest),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentStudent.fullName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 17.sp
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SchoolGoldAccent
                            ) {
                                Text(
                                    text = "Roll #${currentStudent.rollNo}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SchoolNavyDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "${currentStudent.className} - Section ${currentStudent.section} • Reg: ${currentStudent.regNo}",
                            style = MaterialTheme.typography.bodySmall.copy(color = SchoolGoldLight, fontSize = 12.sp)
                        )
                        Text(
                            text = "Paradise Little Angels Secondary School, Wan Khara",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                        )
                    }
                }

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.padding(vertical = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Father / Guardian", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f)))
                        Text(currentStudent.parentName, style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontWeight = FontWeight.Medium))
                    }
                    Column {
                        Text("Emergency Phone", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f)))
                        Text(currentStudent.emergencyContact, style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontWeight = FontWeight.Medium))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Medical Status", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(alpha = 0.7f)))
                        Text(currentStudent.medicalNotes.take(15), style = MaterialTheme.typography.bodySmall.copy(color = SchoolGoldLight))
                    }
                }
            }
        }

        // Sub Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SchoolNavyPrimary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Report Card") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Exam Dates") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Fee Vouchers") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Homework") })
        }

        when (selectedTab) {
            0 -> {
                // Report Card & Academic Record Tab
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
                            text = "Terminal Academic Results",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { showReportCardDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                            modifier = Modifier.testTag("btn_view_official_report")
                        ) {
                            Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Official Report Card", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (marks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No exam marks posted yet for this student.", color = TextMutedLight)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(marks) { mark ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    border = CardDefaults.outlinedCardBorder()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(mark.subject, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                            Text(mark.examTitle, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                            if (mark.teacherRemarks.isNotBlank()) {
                                                Text("Feedback: ${mark.teacherRemarks}", style = MaterialTheme.typography.labelSmall.copy(color = SchoolTealSecondary))
                                            }
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "${mark.marksObtained.toInt()} / ${mark.totalMarks.toInt()}",
                                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                StatusBadge(status = mark.grade)
                                            }
                                            Text("Percentage: ${mark.percentage}%", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Exam Timetables
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Upcoming Mid-Term Examination Schedule",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(examSchedules) { sch ->
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
                                        Text(sch.subject, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = SchoolNavyLight.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = sch.roomNo,
                                                style = MaterialTheme.typography.labelSmall.copy(color = SchoolNavyPrimary, fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Date: ${sch.examDate}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = SchoolTealSecondary))
                                        Text("Timing: ${sch.startTime} - ${sch.endTime}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Syllabus: ${sch.syllabusCoverage}", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Fee Vouchers
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Monthly Fee Vouchers & Receipts",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (studentVouchers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No fee vouchers generated for this student.", color = TextMutedLight)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(studentVouchers) { voucher ->
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
                                                Text(voucher.monthYear, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                                Text("Voucher: ${voucher.voucherNo}", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                            }
                                            StatusBadge(status = voucher.paymentStatus)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Total Amount: Rs. ${voucher.totalAmount.toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                            Text("Paid: Rs. ${voucher.amountPaid.toInt()}", style = MaterialTheme.typography.bodySmall.copy(color = StatusPresentGreen))
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("Due Date: ${voucher.dueDate}", style = MaterialTheme.typography.labelSmall.copy(color = StatusAbsentRed))
                                            Button(
                                                onClick = { showVoucherDialog = voucher },
                                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                                modifier = Modifier.testTag("btn_view_voucher_${voucher.id}")
                                            ) {
                                                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("View Voucher", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Homework Tasks
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Assignments & Homework Tasks",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
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
                                        Text(hw.subject, style = MaterialTheme.typography.labelSmall.copy(color = SchoolTealSecondary, fontWeight = FontWeight.Bold))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(hw.description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Due: ${hw.dueDate}", style = MaterialTheme.typography.labelSmall.copy(color = StatusAbsentRed))
                                        Button(
                                            onClick = { viewModel.showMessage("✅ Assignment '${hw.title}' marked as submitted!") },
                                            colors = ButtonDefaults.buttonColors(containerColor = SchoolTealSecondary),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Submit Task", fontSize = 11.sp)
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

    // Voucher Dialog
    showVoucherDialog?.let { voucher ->
        FeeVoucherDialog(
            voucher = voucher,
            onDismiss = { showVoucherDialog = null },
            onPay = { amount, method, ref ->
                viewModel.payFeeVoucher(voucher, amount, method, ref)
                showVoucherDialog = null
            }
        )
    }

    // Report Card Dialog
    if (showReportCardDialog && currentStudent != null) {
        ReportCardDialog(
            student = currentStudent,
            marks = marks,
            onDismiss = { showReportCardDialog = false }
        )
    }
}
