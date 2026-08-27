package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entities.FeeStructureEntity
import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.StudentEntity
import com.example.ui.components.FeeVoucherDialog
import com.example.ui.components.StatCard
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun AccountantModule(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val feeVouchers by viewModel.feeVouchers.collectAsState()
    val feeStructures by viewModel.feeStructures.collectAsState()
    val students by viewModel.students.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val staff by viewModel.staff.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Vouchers & Collection, 1: Fee Structure, 2: Payroll & Expenses
    var showGenerateVoucherDialog by remember { mutableStateOf(false) }
    var showRecordPaymentDialog by remember { mutableStateOf<FeeVoucherEntity?>(null) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    var viewingVoucher by remember { mutableStateOf<FeeVoucherEntity?>(null) }

    val totalCollected = feeVouchers.sumOf { it.amountPaid }
    val totalBilled = feeVouchers.sumOf { it.totalAmount }
    val totalPending = totalBilled - totalCollected
    val overdueCount = feeVouchers.count { it.paymentStatus == "Overdue" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Finance Header Strip
        Surface(
            color = SchoolNavyPrimary,
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
                            text = "Bursar & Accounts Department",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White)
                        )
                        Text(
                            text = "Chief Accountant: Mr. Zahid Ali • Wan Khara Accounts Office",
                            style = MaterialTheme.typography.bodySmall.copy(color = SchoolGoldLight, fontSize = 11.sp)
                        )
                    }
                    Button(
                        onClick = { showGenerateVoucherDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolGoldAccent),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_generate_voucher_top")
                    ) {
                        Icon(Icons.Default.PostAdd, contentDescription = null, tint = SchoolNavyDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Generate Voucher", color = SchoolNavyDark, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
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
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Fee Vouchers (${feeVouchers.size})") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Grade Structures") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Payroll & Expenses") })
        }

        when (selectedTab) {
            0 -> {
                // Fee Vouchers & Payment Collection
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    // Summary cards
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatCard(
                            title = "Collected Fees",
                            value = "Rs. ${totalCollected.toInt()}",
                            icon = Icons.Default.CheckCircle,
                            iconColor = StatusPresentGreen,
                            bgColor = StatusPresentBg,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Outstanding",
                            value = "Rs. ${totalPending.toInt()}",
                            subtitle = "$overdueCount Overdue vouchers",
                            icon = Icons.Default.PendingActions,
                            iconColor = FeeOverdueRed,
                            bgColor = FeeOverdueBg,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Institutional Vouchers Ledger",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        OutlinedButton(
                            onClick = {
                                viewModel.showMessage("📲 Batch overdue payment SMS reminders sent to $overdueCount parents!")
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_send_fee_reminders")
                        ) {
                            Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Send Reminders ($overdueCount)", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(feeVouchers) { voucher ->
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
                                            Text(voucher.studentName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                            Text("${voucher.className}-${voucher.section} • Reg: ${voucher.regNo} • ${voucher.voucherNo}", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight))
                                        }
                                        StatusBadge(status = voucher.paymentStatus)
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Total: Rs. ${voucher.totalAmount.toInt()} (Paid: Rs. ${voucher.amountPaid.toInt()})", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium))
                                        Text("Due: ${voucher.dueDate}", style = MaterialTheme.typography.labelSmall.copy(color = if (voucher.paymentStatus == "Overdue") StatusAbsentRed else TextMutedLight))
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedButton(
                                            onClick = { viewingVoucher = voucher },
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Voucher Print", fontSize = 11.sp)
                                        }

                                        if (voucher.paymentStatus != "Paid") {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Button(
                                                onClick = { showRecordPaymentDialog = voucher },
                                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.testTag("btn_collect_payment_${voucher.id}")
                                            ) {
                                                Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Collect Payment", fontSize = 11.sp)
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
                // Grade Fee Structure Table
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Standard Fee Structures by Grade Level",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Paradise Little Angels Secondary School, Wan Khara",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(feeStructures) { struct ->
                            val totalMonthly = struct.tuitionFee + struct.transportFee + struct.sportsFee + struct.labFee
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
                                        Text(struct.className, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = SchoolNavyLight.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = struct.gradeLevel,
                                                style = MaterialTheme.typography.labelSmall.copy(color = SchoolNavyPrimary, fontWeight = FontWeight.Bold),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Tuition Fee: Rs. ${struct.tuitionFee.toInt()}", style = MaterialTheme.typography.bodySmall)
                                        Text("Admission Fee: Rs. ${struct.admissionFee.toInt()}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Transport: Rs. ${struct.transportFee.toInt()}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                        Text("Exam & Sports: Rs. ${(struct.examFee + struct.sportsFee).toInt()}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                        Text("Total Standard Package:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text("Rs. ${totalMonthly.toInt()} / month", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Payroll & Operating Expenses
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
                            text = "School Expenditures & Staff Payroll",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Button(
                            onClick = { showExpenseDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Record Expense", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            // Staff Payroll Aggregate Card
                            val totalPayroll = staff.sumOf { it.monthlySalary }
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SchoolNavyPrimary.copy(alpha = 0.08f)),
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
                                        Text("Monthly Staff Payroll Commitment", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text("${staff.size} active faculty & staff members", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                    }
                                    Text("Rs. ${totalPayroll.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
                                }
                            }
                        }

                        items(expenses) { exp ->
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
                                        Text(exp.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                                        Text("Category: ${exp.category} • Ref: ${exp.receiptNo}", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                        if (exp.notes.isNotBlank()) {
                                            Text(exp.notes, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Rs. ${exp.amount.toInt()}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = StatusAbsentRed))
                                        Text(exp.expenseDate, style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Voucher Print/Preview Dialog
    viewingVoucher?.let { voucher ->
        FeeVoucherDialog(
            voucher = voucher,
            onDismiss = { viewingVoucher = null }
        )
    }

    // Record Payment Dialog
    showRecordPaymentDialog?.let { voucher ->
        RecordPaymentDialog(
            voucher = voucher,
            onDismiss = { showRecordPaymentDialog = null },
            onSubmit = { amount, method, ref ->
                viewModel.payFeeVoucher(voucher, amount, method, ref)
                showRecordPaymentDialog = null
            }
        )
    }

    // Generate Voucher Dialog
    if (showGenerateVoucherDialog) {
        GenerateVoucherDialog(
            students = students,
            onDismiss = { showGenerateVoucherDialog = false },
            onSubmit = { st, month, tuition, admission, transport, exam, sports, other, discount, due ->
                viewModel.createFeeVoucher(st, month, tuition, admission, transport, exam, sports, other, discount, due)
                showGenerateVoucherDialog = false
            }
        )
    }

    // Record Expense Dialog
    if (showExpenseDialog) {
        ExpenseDialog(
            onDismiss = { showExpenseDialog = false },
            onSubmit = { title, cat, amt, notes ->
                viewModel.recordSchoolExpense(title, cat, amt, notes)
                showExpenseDialog = false
            }
        )
    }
}

@Composable
fun RecordPaymentDialog(
    voucher: FeeVoucherEntity,
    onDismiss: () -> Unit,
    onSubmit: (Double, String, String) -> Unit
) {
    val remaining = voucher.totalAmount - voucher.amountPaid
    var amountStr by remember { mutableStateOf(remaining.toInt().toString()) }
    var method by remember { mutableStateOf("Cash at Accounts Counter") }
    var refNo by remember { mutableStateOf("RCP-PLA-${System.currentTimeMillis().toString().takeLast(4)}") }

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
                    text = "Collect Fee Payment",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                )
                Text(
                    text = "Student: ${voucher.studentName} (${voucher.voucherNo})",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Amount Received (Rs.) *") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = method,
                    onValueChange = { method = it },
                    label = { Text("Payment Method (Cash / Bank / JazzCash / Online)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = refNo,
                    onValueChange = { refNo = it },
                    label = { Text("Receipt / Transaction Reference") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amt = amountStr.toDoubleOrNull() ?: 0.0
                            if (amt > 0.0) {
                                onSubmit(amt, method, refNo)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary),
                        modifier = Modifier.testTag("btn_confirm_payment")
                    ) {
                        Text("Issue Receipt & Save")
                    }
                }
            }
        }
    }
}

@Composable
fun GenerateVoucherDialog(
    students: List<StudentEntity>,
    onDismiss: () -> Unit,
    onSubmit: (StudentEntity, String, Double, Double, Double, Double, Double, Double, Double, String) -> Unit
) {
    var selectedStudentIndex by remember { mutableStateOf(0) }
    var monthYear by remember { mutableStateOf("September 2026") }
    var tuitionStr by remember { mutableStateOf("6000") }
    var transportStr by remember { mutableStateOf("2500") }
    var sportsStr by remember { mutableStateOf("1000") }
    var examStr by remember { mutableStateOf("0") }
    var discountStr by remember { mutableStateOf("0") }
    var dueDate by remember { mutableStateOf("2026-09-15") }

    val currentStudent = students.getOrNull(selectedStudentIndex) ?: students.firstOrNull()

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
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                Text(
                    text = "Generate Monthly Fee Voucher",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                )
                Text(
                    text = "Paradise Little Angels Secondary School, Wan Khara",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight)
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (currentStudent != null) {
                    Text("Select Target Student:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SchoolNavyPrimary.copy(alpha = 0.08f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${currentStudent.fullName} • ${currentStudent.className}-${currentStudent.section} (Reg: ${currentStudent.regNo})",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = monthYear, onValueChange = { monthYear = it }, label = { Text("Billing Month") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = dueDate, onValueChange = { dueDate = it }, label = { Text("Due Date") }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = tuitionStr, onValueChange = { tuitionStr = it }, label = { Text("Tuition Fee (Rs.)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = transportStr, onValueChange = { transportStr = it }, label = { Text("Transport (Rs.)") }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = sportsStr, onValueChange = { sportsStr = it }, label = { Text("Sports & Lab (Rs.)") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = discountStr, onValueChange = { discountStr = it }, label = { Text("Discount / Concession (Rs.)") }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (currentStudent != null) {
                                onSubmit(
                                    currentStudent,
                                    monthYear,
                                    tuitionStr.toDoubleOrNull() ?: 0.0,
                                    0.0,
                                    transportStr.toDoubleOrNull() ?: 0.0,
                                    examStr.toDoubleOrNull() ?: 0.0,
                                    sportsStr.toDoubleOrNull() ?: 0.0,
                                    0.0,
                                    discountStr.toDoubleOrNull() ?: 0.0,
                                    dueDate
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                    ) {
                        Text("Create Voucher")
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, Double, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Utilities") }
    var amountStr by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

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
                    text = "Record School Expenditure",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Expense Title *") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = amountStr, onValueChange = { amountStr = it }, label = { Text("Amount (Rs.) *") }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notes / Vendor Reference") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val amt = amountStr.toDoubleOrNull() ?: 0.0
                            if (title.isNotBlank() && amt > 0.0) {
                                onSubmit(title, category, amt, notes)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                    ) {
                        Text("Record Entry")
                    }
                }
            }
        }
    }
}
