package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.MessageEntity
import com.example.data.local.entities.NoticeEntity
import com.example.ui.components.FeeVoucherDialog
import com.example.ui.components.ReportCardDialog
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun ParentPortal(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val students by viewModel.students.collectAsState()
    val selectedStudentId by viewModel.selectedStudentId.collectAsState()
    val marks by viewModel.allMarks.collectAsState()
    val feeVouchers by viewModel.feeVouchers.collectAsState()
    val attendanceToday by viewModel.attendanceToday.collectAsState()
    val notices by viewModel.notices.collectAsState()
    val messages by viewModel.messages.collectAsState()

    val currentStudent = students.find { it.id == selectedStudentId } ?: students.firstOrNull()
    var selectedTab by remember { mutableStateOf(0) } // 0: Child Progress, 1: Fee Vouchers & Payment, 2: School Circulars, 3: Message Teacher
    var showVoucherDialog by remember { mutableStateOf<FeeVoucherEntity?>(null) }
    var showReportCardDialog by remember { mutableStateOf(false) }
    var showSendMessageDialog by remember { mutableStateOf(false) }

    val studentVouchers = feeVouchers.filter { it.studentId == currentStudent?.id }
    val todayAttendance = attendanceToday.find { it.studentId == currentStudent?.id }

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

        // Parent Banner
        Surface(
            color = SchoolNavyPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = SchoolNavyPrimary, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Parent Portal • ${currentStudent.parentName}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color.White)
                    )
                    Text(
                        text = "Child: ${currentStudent.fullName} (${currentStudent.className}-${currentStudent.section})",
                        style = MaterialTheme.typography.bodySmall.copy(color = SchoolGoldLight, fontSize = 12.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (todayAttendance?.status?.lowercase()) {
                        "present" -> StatusPresentBg
                        "absent" -> StatusAbsentBg
                        else -> StatusLateBg
                    }
                ) {
                    Text(
                        text = "Today: ${todayAttendance?.status ?: "Present"}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (todayAttendance?.status?.lowercase()) {
                                "present" -> StatusPresentGreen
                                "absent" -> StatusAbsentRed
                                else -> StatusLateYellow
                            },
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Sub Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = SchoolNavyPrimary
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Progress") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Fees (${studentVouchers.size})") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Notices (${notices.size})") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Messages") })
        }

        when (selectedTab) {
            0 -> {
                // Child Progress & Attendance
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Attendance Warning / Status banner if absent
                    if (todayAttendance?.status == "Absent") {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StatusAbsentBg),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = StatusAbsentRed)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text("Unexcused Absence Alert", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = StatusAbsentRed))
                                    Text("Your child was marked absent today. Automated SMS notification dispatched.", style = MaterialTheme.typography.bodySmall.copy(color = StatusAbsentRed, fontSize = 11.sp))
                                }
                            }
                        }
                    }

                    // Progress Overview
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("First Term Academic Standing", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                Button(
                                    onClick = { showReportCardDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("btn_parent_view_report")
                                ) {
                                    Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Report Card", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            marks.forEach { mark ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(mark.subject, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("${mark.marksObtained.toInt()}/${mark.totalMarks.toInt()} (${mark.percentage.toInt()}%)", style = MaterialTheme.typography.bodySmall)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        StatusBadge(status = mark.grade)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Fee Dues & Payment History
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
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
                                        Text("Voucher: ${voucher.voucherNo}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text("Month: ${voucher.monthYear}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                    }
                                    StatusBadge(status = voucher.paymentStatus)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Amount Due: Rs. ${voucher.totalAmount.toInt()}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                    Text("Paid: Rs. ${voucher.amountPaid.toInt()}", style = MaterialTheme.typography.bodySmall.copy(color = StatusPresentGreen))
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Due Date: ${voucher.dueDate}", style = MaterialTheme.typography.labelSmall.copy(color = if (voucher.paymentStatus == "Overdue") StatusAbsentRed else TextSecondaryLight))
                                    Button(
                                        onClick = { showVoucherDialog = voucher },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (voucher.paymentStatus == "Paid") SchoolTealSecondary else SchoolGoldAccent
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("btn_parent_voucher_${voucher.id}")
                                    ) {
                                        Text(
                                            text = if (voucher.paymentStatus == "Paid") "View Receipt" else "Pay / View Voucher",
                                            color = if (voucher.paymentStatus == "Paid") Color.White else SchoolNavyDark,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // School Noticeboard
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notices) { notice ->
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
                                    Text(notice.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary), modifier = Modifier.weight(1f))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SchoolNavyLight.copy(alpha = 0.1f)
                                    ) {
                                        Text(notice.category, style = MaterialTheme.typography.labelSmall.copy(color = SchoolNavyPrimary, fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notice.content, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Published: ${notice.publishDate}", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                    Text(notice.author, style = MaterialTheme.typography.labelSmall.copy(color = SchoolTealSecondary, fontWeight = FontWeight.Medium))
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Direct Teacher Messaging Portal
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
                            text = "Teacher Communications",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { showSendMessageDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                        ) {
                            Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Message Teacher", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(messages) { msg ->
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
                                        Text(msg.subject, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text(msg.timestamp, style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("From: ${msg.senderName} (${msg.senderRole})", style = MaterialTheme.typography.labelSmall.copy(color = SchoolTealSecondary, fontWeight = FontWeight.Medium))
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(msg.message, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
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

    // Send Message Dialog
    if (showSendMessageDialog && currentStudent != null) {
        SendMessageDialog(
            parentName = currentStudent.parentName,
            onDismiss = { showSendMessageDialog = false },
            onSubmit = { teacher, subject, body ->
                viewModel.sendDirectMessage(
                    senderRole = "Parent",
                    senderName = "${currentStudent.parentName} (Parent of ${currentStudent.fullName})",
                    receiverRole = "Teacher",
                    receiverName = teacher,
                    subject = subject,
                    message = body
                )
                showSendMessageDialog = false
            }
        )
    }
}

@Composable
fun SendMessageDialog(
    parentName: String,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var selectedTeacher by remember { mutableStateOf("Mr. Tariq Mahmood (Physics)") }
    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

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
                    text = "Direct Message to Teacher",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                )
                Text(
                    text = "From: $parentName",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = selectedTeacher,
                    onValueChange = { selectedTeacher = it },
                    label = { Text("Teacher Name & Subject") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject / Reason *") },
                    placeholder = { Text("e.g. Inquiry regarding board exams / leave") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Message Body *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
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
                            if (subject.isNotBlank() && body.isNotBlank()) {
                                onSubmit(selectedTeacher, subject, body)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                    ) {
                        Text("Send Message")
                    }
                }
            }
        }
    }
}
