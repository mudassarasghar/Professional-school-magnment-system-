package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.StaffEntity
import com.example.data.local.entities.StudentEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun SuperAdminDashboard(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.students.collectAsState()
    val staff by viewModel.staff.collectAsState()
    val attendance by viewModel.attendanceToday.collectAsState()
    val feeVouchers by viewModel.feeVouchers.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val examSchedules by viewModel.examSchedules.collectAsState()
    val allMarks by viewModel.allMarks.collectAsState()

    // Dialog state variables
    var showAdmissionDialog by remember { mutableStateOf(false) }
    var showFeeCollectDialog by remember { mutableStateOf(false) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    var showTimetableDialog by remember { mutableStateOf(false) }
    var showAttendanceMarkerDialog by remember { mutableStateOf(false) }
    var showNoticeDialog by remember { mutableStateOf(false) }

    // Stationery Document Dialogs
    var selectedStudentForDocument by remember { mutableStateOf<StudentEntity?>(null) }
    var showIdCardDialog by remember { mutableStateOf(false) }
    var showFeeVoucherDialog by remember { mutableStateOf(false) }
    var showReportCardDialog by remember { mutableStateOf(false) }
    var showCharacterCertDialog by remember { mutableStateOf(false) }
    var showAdmitCardDialog by remember { mutableStateOf(false) }
    var showSalarySlipDialog by remember { mutableStateOf(false) }
    var selectedStaffForSalary by remember { mutableStateOf<StaffEntity?>(null) }

    // Set default selected student once data loads
    LaunchedEffect(students) {
        if (selectedStudentForDocument == null && students.isNotEmpty()) {
            selectedStudentForDocument = students.firstOrNull { it.className.contains("9") } ?: students.first()
        }
    }

    // Calculations
    val totalStudents = students.size
    val schoolStudents = students.count { it.className.contains("Nursery") || it.className.contains("Prep") || it.className.contains("Grade 5") || it.className.contains("Grade 8") }
    val academyStudents = totalStudents - schoolStudents

    val presentCount = attendance.count { it.status == "Present" }
    val absentCount = attendance.count { it.status == "Absent" }
    val attendancePct = if (attendance.isNotEmpty()) ((presentCount.toDouble() / attendance.size.toDouble()) * 100).toInt() else 80

    val todayFeeCollected = feeVouchers.filter { it.paymentStatus == "Paid" }.sumOf { it.amountPaid }
    val totalPendingDues = feeVouchers.filter { it.paymentStatus != "Paid" }.sumOf { it.totalAmount - it.amountPaid }
    val paidAccountsCount = feeVouchers.count { it.paymentStatus == "Paid" }

    val totalExpenses = expenses.sumOf { it.amount }
    val cashReserve = 195500.0 - totalExpenses

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SchoolNavyDarkSurface)
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // =========================================================================
        // 1. HERO CAMPUS PORTAL BANNER (Matching uploaded design)
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SchoolNavyCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF13203C),
                                Color(0xFF0D172A)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Operational status pill
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFF0F2645),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolCyanAccent.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "OFFICIAL CAMPUS PORTAL",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SchoolCyanLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SchoolEmeraldGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "System Operational & Safe",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SchoolEmeraldGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    // Main Headline & Description
                    Column {
                        Text(
                            text = "Paradise Little Angels\nSecondary School, Wan Khara",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontSize = 19.sp,
                                lineHeight = 23.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Complete digitalized administration: Real-time attendance, daily fee collection ledger, teacher reporting times, test records, expense control, and parent communications.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }

                    // 4 Action Pill Buttons on Banner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // + Add New Student (Cyan)
                        Button(
                            onClick = { showAdmissionDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("banner_add_student")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Add New Student", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Collect Fee (Green)
                        Button(
                            onClick = { showFeeCollectDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolEmeraldGreen),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("banner_collect_fee")
                        ) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Collect Fee", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Timetables (Purple)
                        Button(
                            onClick = { showTimetableDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolPurpleAccent),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("banner_timetables")
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Timetables", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // + Daily Expense (Amber)
                        Button(
                            onClick = { showExpenseDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolGoldAccent),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("banner_add_expense")
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, tint = SchoolNavyDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Daily Expense", color = SchoolNavyDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 2. 4 KEY METRICS KPI CARDS (Matching uploaded screenshot)
        // =========================================================================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // CARD 1: STUDENT ENROLLMENT
            ExecutiveMetricCard(
                title = "STUDENT\nENROLLMENT",
                icon = Icons.Default.School,
                mainValue = "$totalStudents Enrolled",
                subValue = "$totalStudents Active",
                detailText = "School: $schoolStudents   Academy: $academyStudents",
                actionButtonText = "+ Add New Student",
                onActionClick = { showAdmissionDialog = true },
                modifier = Modifier.weight(1f)
            )

            // CARD 2: TODAY'S ATTENDANCE
            ExecutiveMetricCard(
                title = "TODAY'S\nATTENDANCE",
                icon = Icons.Default.EventAvailable,
                mainValue = "$attendancePct% (${presentCount} of ${totalStudents.coerceAtLeast(10)})",
                subValue = "$presentCount Present • $absentCount Absent",
                detailText = "Faculty on-time: ${staff.size}/${staff.size}",
                actionButtonText = "Mark Class Attendance",
                onActionClick = { showAttendanceMarkerDialog = true },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // CARD 3: FEE COLLECTIONS
            ExecutiveMetricCard(
                title = "FEE\nCOLLECTIONS",
                icon = Icons.Default.Payments,
                mainValue = "Rs. ${todayFeeCollected.toInt()}",
                subValue = "$paidAccountsCount Accounts Cleared",
                detailText = "Pending Dues: Rs. ${totalPendingDues.toInt()}",
                actionButtonText = "+ Collect Student Fee",
                onActionClick = { showFeeCollectDialog = true },
                modifier = Modifier.weight(1f)
            )

            // CARD 4: DAILY EXPENSES & CASH
            ExecutiveMetricCard(
                title = "DAILY EXPENSES\n& CASH",
                icon = Icons.Default.AccountBalanceWallet,
                mainValue = "Rs. ${cashReserve.toInt()}",
                subValue = "Today Outflow: Rs. ${totalExpenses.toInt()}",
                detailText = "Salary Due: Rs. 130,000",
                actionButtonText = "+ Add Expense",
                onActionClick = { showExpenseDialog = true },
                modifier = Modifier.weight(1f)
            )
        }

        // =========================================================================
        // 3. OFFICIAL STATIONERY & DOCUMENT ISSUANCE CENTER
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SchoolNavyCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Center Header & Candidate Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SchoolGoldAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = SchoolGoldAccent, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Official Stationery & Document Issuance Center",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "1-Click instant generation with official watermark seals, barcodes & principal signature blocks",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryDark,
                                    fontSize = 9.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Candidate Dropdown Pill Selector
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SchoolNavyDarker,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var expanded by remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Select Candidate:", color = SchoolCyanLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = selectedStudentForDocument?.let { "#${it.rollNo} - ${it.fullName} (${it.className} - ${it.section})" } ?: "Select Student",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(SchoolNavyCard)
                    ) {
                        students.forEach { student ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "#${student.rollNo} - ${student.fullName} (${student.className})",
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                },
                                onClick = {
                                    selectedStudentForDocument = student
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // 6 Document Issuance Buttons Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DocumentIssuanceTile(
                        icon = Icons.Default.Badge,
                        label = "Student ID Card",
                        accentColor = SchoolCyanLight,
                        onClick = { showIdCardDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    DocumentIssuanceTile(
                        icon = Icons.Default.ReceiptLong,
                        label = "Fee Voucher",
                        accentColor = SchoolEmeraldGreen,
                        onClick = { showFeeVoucherDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    DocumentIssuanceTile(
                        icon = Icons.Default.Assessment,
                        label = "Progress DMC",
                        accentColor = SchoolGoldAccent,
                        onClick = { showReportCardDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DocumentIssuanceTile(
                        icon = Icons.Default.WorkspacePremium,
                        label = "Character Cert",
                        accentColor = SchoolPurpleAccent,
                        onClick = { showCharacterCertDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    DocumentIssuanceTile(
                        icon = Icons.Default.ConfirmationNumber,
                        label = "Exam Admit Card",
                        accentColor = Color(0xFFF97316),
                        onClick = { showAdmitCardDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                    DocumentIssuanceTile(
                        icon = Icons.Default.Paid,
                        label = "Staff Pay Slip",
                        accentColor = SchoolEmeraldGreen,
                        onClick = {
                            selectedStaffForSalary = staff.firstOrNull()
                            showSalarySlipDialog = true
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // =========================================================================
        // 4. LIVE AUDIT LOGS & SYSTEM NOTICES
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SchoolNavyCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Live Institutional Audit & Automated Alerts",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White, fontSize = 12.sp)
                    )
                    TextButton(
                        onClick = { showNoticeDialog = true },
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("+ Broadcast Notice", color = SchoolCyanLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                DashboardAuditRow("SIS Admission: Muhammad Ahmad Raza enrolled in Grade 9 (Science)", "Just now")
                DashboardAuditRow("Automated SMS: Absence SMS triggered to Guardian of Hamza Ali", "08:45 AM")
                DashboardAuditRow("Counter Payment: Rs. 6,000 received for PLA-VCH-202608-0101 (Cleared)", "09:12 AM")
                DashboardAuditRow("Academic Desk: Mid-Term Examination 2026 timetable published", "Yesterday")
            }
        }
    }

    // =========================================================================
    // DIALOGS & MODAL INTERACTIONS
    // =========================================================================

    // 1. Admission Dialog
    if (showAdmissionDialog) {
        AdmissionFormDialog(
            onDismiss = { showAdmissionDialog = false },
            onSubmit = { name, gender, dob, gradeLevel, className, sec, roll, pName, pPhone, pEmail, emerg, med, addr ->
                viewModel.registerStudent(name, gender, dob, gradeLevel, className, sec, roll, pName, pPhone, pEmail, emerg, med, addr)
                showAdmissionDialog = false
            }
        )
    }

    // 2. Fee Collect Dialog
    if (showFeeCollectDialog) {
        QuickFeeCollectionDialog(
            students = students,
            vouchers = feeVouchers,
            onDismiss = { showFeeCollectDialog = false },
            onCollect = { voucher, amount, method, ref ->
                viewModel.payFeeVoucher(voucher, amount, method, ref)
                showFeeCollectDialog = false
            }
        )
    }

    // 3. Daily Expense Dialog
    if (showExpenseDialog) {
        DailyExpenseDialog(
            onDismiss = { showExpenseDialog = false },
            onSubmit = { title, cat, amount, ref, notes ->
                viewModel.recordSchoolExpense(title, cat, amount, notes)
                showExpenseDialog = false
            }
        )
    }

    // 4. Timetables Dialog
    if (showTimetableDialog) {
        TimetableModalDialog(
            schedules = examSchedules,
            onDismiss = { showTimetableDialog = false }
        )
    }

    // 5. Fast Attendance Marker Dialog
    if (showAttendanceMarkerDialog) {
        FastAttendanceMarkerDialog(
            students = students,
            onDismiss = { showAttendanceMarkerDialog = false },
            onMark = { student, status ->
                viewModel.updateAttendanceStatus(student, status)
            }
        )
    }

    // 6. Notice Broadcast Dialog
    if (showNoticeDialog) {
        NoticeFormDialog(
            onDismiss = { showNoticeDialog = false },
            onSubmit = { title, cat, content, target, prio ->
                viewModel.postNotice(title, cat, content, target, prio)
                showNoticeDialog = false
            }
        )
    }

    // Stationery Document Issuance Dialogs
    if (showIdCardDialog && selectedStudentForDocument != null) {
        StudentIdCardDialog(
            student = selectedStudentForDocument!!,
            onDismiss = { showIdCardDialog = false }
        )
    }

    if (showFeeVoucherDialog && selectedStudentForDocument != null) {
        val voucher = feeVouchers.firstOrNull { it.studentId == selectedStudentForDocument!!.id }
            ?: feeVouchers.firstOrNull()
        if (voucher != null) {
            FeeVoucherDialog(
                voucher = voucher,
                onDismiss = { showFeeVoucherDialog = false },
                onPay = { amt, method, ref ->
                    viewModel.payFeeVoucher(voucher, amt, method, ref)
                    showFeeVoucherDialog = false
                }
            )
        }
    }

    if (showReportCardDialog && selectedStudentForDocument != null) {
        val studentMarks = allMarks.filter { it.studentId == selectedStudentForDocument!!.id }
        ReportCardDialog(
            student = selectedStudentForDocument!!,
            marks = studentMarks,
            onDismiss = { showReportCardDialog = false }
        )
    }

    if (showCharacterCertDialog && selectedStudentForDocument != null) {
        CharacterCertificateDialog(
            student = selectedStudentForDocument!!,
            onDismiss = { showCharacterCertDialog = false }
        )
    }

    if (showAdmitCardDialog && selectedStudentForDocument != null) {
        ExamAdmitCardDialog(
            student = selectedStudentForDocument!!,
            onDismiss = { showAdmitCardDialog = false }
        )
    }

    if (showSalarySlipDialog && selectedStaffForSalary != null) {
        StaffSalarySlipDialog(
            staff = selectedStaffForSalary!!,
            onDismiss = { showSalarySlipDialog = false }
        )
    }
}

// -------------------------------------------------------------
// EXECUTIVE METRIC KPI CARD (Matching the photo layout)
// -------------------------------------------------------------
@Composable
private fun ExecutiveMetricCard(
    title: String,
    icon: ImageVector,
    mainValue: String,
    subValue: String,
    detailText: String,
    actionButtonText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Row: Title & Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Black,
                        fontSize = 9.sp,
                        lineHeight = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF334155),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Main Stat
            Text(
                text = mainValue,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    fontSize = 17.sp
                )
            )

            // Sub Value
            Text(
                text = subValue,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF475569),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            // Detail Text
            Text(
                text = detailText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF64748B),
                    fontSize = 9.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
            ) {
                Text(
                    text = actionButtonText,
                    color = Color(0xFF0F172A),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// -------------------------------------------------------------
// DOCUMENT ISSUANCE TILE
// -------------------------------------------------------------
@Composable
private fun DocumentIssuanceTile(
    icon: ImageVector,
    label: String,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = SchoolNavyDarker,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DashboardAuditRow(text: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(SchoolCyanAccent)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFFCBD5E1),
                    fontSize = 10.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = time,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondaryDark,
                fontSize = 9.sp
            )
        )
    }
}

// -------------------------------------------------------------
// FAST ATTENDANCE MARKER DIALOG
// -------------------------------------------------------------
@Composable
fun FastAttendanceMarkerDialog(
    students: List<StudentEntity>,
    onDismiss: () -> Unit,
    onMark: (StudentEntity, String) -> Unit
) {
    var selectedClass by remember { mutableStateOf("All Classes") }
    val classList = listOf("All Classes", "Nursery", "Prep", "Grade 5", "Grade 8", "Grade 9", "Grade 10")

    val filtered = if (selectedClass == "All Classes") students else students.filter { it.className == selectedClass }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = SchoolNavyDarker,
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Daily Attendance Fast Marker", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("1-Tap bulk marking with auto parent SMS alerts", color = TextSecondaryDark, fontSize = 10.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Class filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    classList.forEach { cls ->
                        val isSelected = selectedClass == cls
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedClass = cls },
                            label = { Text(cls, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SchoolCyanAccent,
                                selectedLabelColor = Color.White,
                                containerColor = SchoolNavyCard,
                                labelColor = TextSecondaryDark
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filtered) { student ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SchoolNavyCard),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(student.fullName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                    Text("${student.className} • Roll #${student.rollNo}", color = TextSecondaryDark, fontSize = 9.sp)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(
                                        onClick = { onMark(student, "Present") },
                                        colors = ButtonDefaults.buttonColors(containerColor = SchoolEmeraldGreen),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("P", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Button(
                                        onClick = { onMark(student, "Absent") },
                                        colors = ButtonDefaults.buttonColors(containerColor = StatusAbsentRed),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("A", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Button(
                                        onClick = { onMark(student, "Late") },
                                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGoldAccent),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(26.dp)
                                    ) {
                                        Text("L", color = SchoolNavyDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent)
                ) {
                    Text("Done")
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TIMETABLE MODAL DIALOG
// -------------------------------------------------------------
@Composable
fun TimetableModalDialog(
    schedules: List<com.example.data.local.entities.ExamScheduleEntity>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            shape = RoundedCornerShape(16.dp),
            color = SchoolNavyDarker,
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Academic & Examination Timetables", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Mid-Term 2026 Schedule & Room Allocations", color = SchoolCyanLight, fontSize = 10.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(schedules) { schedule ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SchoolNavyCard),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(schedule.subject, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("${schedule.className} • ${schedule.examDate}", color = SchoolGoldLight, fontSize = 10.sp)
                                    Text("Time: ${schedule.startTime} - ${schedule.endTime}", color = TextSecondaryDark, fontSize = 9.sp)
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SchoolPurpleAccent.copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, SchoolPurpleAccent)
                                ) {
                                    Text(schedule.roomNo, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent)
                ) {
                    Text("Close Timetable")
                }
            }
        }
    }
}

// -------------------------------------------------------------
// QUICK FEE COLLECTION DIALOG
// -------------------------------------------------------------
@Composable
fun QuickFeeCollectionDialog(
    students: List<StudentEntity>,
    vouchers: List<FeeVoucherEntity>,
    onDismiss: () -> Unit,
    onCollect: (FeeVoucherEntity, Double, String, String) -> Unit
) {
    var selectedStudentId by remember { mutableStateOf(students.firstOrNull()?.id ?: 0L) }
    var paymentMethod by remember { mutableStateOf("Cash / Counter") }
    var enteredAmount by remember { mutableStateOf("") }

    val studentVoucher = vouchers.firstOrNull { it.studentId == selectedStudentId && it.paymentStatus != "Paid" }
        ?: vouchers.firstOrNull { it.studentId == selectedStudentId }

    LaunchedEffect(studentVoucher) {
        if (studentVoucher != null) {
            enteredAmount = (studentVoucher.totalAmount - studentVoucher.amountPaid).toInt().toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = SchoolNavyDarker,
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolEmeraldGreen)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = SchoolEmeraldGreen)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Instant Fee Collection Desk", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text("Select Student Candidate:", color = TextSecondaryDark, fontSize = 10.sp)
                var expanded by remember { mutableStateOf(false) }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SchoolNavyCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val currentStd = students.firstOrNull { it.id == selectedStudentId }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(currentStd?.let { "${it.fullName} (${it.className})" } ?: "Select", color = Color.White, fontSize = 12.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(SchoolNavyCard)
                    ) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = { Text("${s.fullName} (${s.className})", color = Color.White, fontSize = 11.sp) },
                                onClick = {
                                    selectedStudentId = s.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (studentVoucher != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SchoolNavyCard),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Voucher: ${studentVoucher.voucherNo}", color = SchoolCyanLight, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                StatusBadge(status = studentVoucher.paymentStatus)
                            }
                            Text("Total: Rs. ${studentVoucher.totalAmount.toInt()} | Paid: Rs. ${studentVoucher.amountPaid.toInt()}", color = Color.White, fontSize = 11.sp)
                            val balance = studentVoucher.totalAmount - studentVoucher.amountPaid
                            Text("Pending Balance: Rs. ${balance.toInt()}", color = if (balance > 0) StatusAbsentRed else StatusPresentGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = enteredAmount,
                    onValueChange = { enteredAmount = it },
                    label = { Text("Amount to Collect (Rs.)", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = SchoolNavyCard,
                        unfocusedContainerColor = SchoolNavyCard
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Cash / Counter", "EasyPaisa", "Bank / Online").forEach { mode ->
                        val isSelected = paymentMethod == mode
                        FilterChip(
                            selected = isSelected,
                            onClick = { paymentMethod = mode },
                            label = { Text(mode, fontSize = 9.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SchoolEmeraldGreen,
                                selectedLabelColor = Color.White,
                                containerColor = SchoolNavyCard,
                                labelColor = TextSecondaryDark
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val amt = enteredAmount.toDoubleOrNull() ?: 0.0
                        if (studentVoucher != null && amt > 0) {
                            onCollect(
                                studentVoucher,
                                amt,
                                paymentMethod,
                                "RCPT-COL-${System.currentTimeMillis().toString().takeLast(5)}"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolEmeraldGreen)
                ) {
                    Text("Confirm Collection & Issue Receipt", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// DAILY EXPENSE DIALOG
// -------------------------------------------------------------
@Composable
fun DailyExpenseDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, Double, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Utilities & Operations") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf("Utilities & Operations", "Staff Salary", "Stationery & Exam Print", "Campus Maintenance", "Transport Fuel")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = SchoolNavyDarker,
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolGoldAccent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Record Campus Daily Expense", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Expense Title / Item", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (Rs.)", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Category:", color = TextSecondaryDark, fontSize = 10.sp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 9.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SchoolGoldAccent,
                                selectedLabelColor = SchoolNavyDark,
                                containerColor = SchoolNavyCard,
                                labelColor = TextSecondaryDark
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Approval Notes / Vendor", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val amt = amount.toDoubleOrNull() ?: 0.0
                        if (title.isNotBlank() && amt > 0) {
                            onSubmit(title, category, amt, "EXP-TXN-${System.currentTimeMillis().toString().takeLast(4)}", notes)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolGoldAccent)
                ) {
                    Text("Save Expense Record", color = SchoolNavyDark, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ADMISSION FORM DIALOG
// -------------------------------------------------------------
@Composable
fun AdmissionFormDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String, String, Int, String, String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("2012-05-14") }
    var gradeLevel by remember { mutableStateOf("Secondary") }
    var className by remember { mutableStateOf("Grade 9") }
    var section by remember { mutableStateOf("A") }
    var rollNo by remember { mutableStateOf("105") }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("") }
    var parentEmail by remember { mutableStateOf("") }
    var emergencyContact by remember { mutableStateOf("") }
    var medicalNotes by remember { mutableStateOf("Fully Fit") }
    var address by remember { mutableStateOf("Wan Khara") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            shape = RoundedCornerShape(16.dp),
            color = SchoolNavyDarker,
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolCyanAccent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("New Student SIS Admission", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Candidate Full Name", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Class (e.g. Grade 9)", fontSize = 10.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                    )
                    OutlinedTextField(
                        value = section,
                        onValueChange = { section = it },
                        label = { Text("Section", fontSize = 10.sp) },
                        modifier = Modifier.weight(0.5f),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                    )
                    OutlinedTextField(
                        value = rollNo,
                        onValueChange = { rollNo = it },
                        label = { Text("Roll No", fontSize = 10.sp) },
                        modifier = Modifier.weight(0.5f),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = parentName,
                    onValueChange = { parentName = it },
                    label = { Text("Father / Guardian Name", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = parentPhone,
                    onValueChange = { parentPhone = it },
                    label = { Text("Parent Mobile Phone (for SMS Alerts)", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Residential Address", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val roll = rollNo.toIntOrNull() ?: 1
                        if (name.isNotBlank() && parentName.isNotBlank()) {
                            onSubmit(name, gender, dob, gradeLevel, className, section, roll, parentName, parentPhone, parentEmail, emergencyContact, medicalNotes, address)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent)
                ) {
                    Text("Register & Generate Student Credentials", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// NOTICE BROADCAST DIALOG
// -------------------------------------------------------------
@Composable
fun NoticeFormDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Academic") }
    var content by remember { mutableStateOf("") }
    var target by remember { mutableStateOf("All") }
    var priority by remember { mutableStateOf("Normal") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = SchoolNavyDarker,
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolCyanAccent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Broadcast Institutional Notice", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Notice Headline", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Notice Content / Circular Details", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedContainerColor = SchoolNavyCard, unfocusedContainerColor = SchoolNavyCard)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            onSubmit(title, category, content, target, priority)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent)
                ) {
                    Text("Publish & Broadcast Notice", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
