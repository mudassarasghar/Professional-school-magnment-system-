package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.*
import com.example.data.repository.SchoolRepository
import com.example.domain.model.GradeCalculator
import com.example.domain.model.UserRole
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SchoolViewModel(private val repository: SchoolRepository) : ViewModel() {

    private val _currentRole = MutableStateFlow(UserRole.SUPER_ADMIN)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _selectedClass = MutableStateFlow("Grade 10")
    val selectedClass: StateFlow<String> = _selectedClass.asStateFlow()

    private val _selectedDate = MutableStateFlow("2026-08-27")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedStudentId = MutableStateFlow<Long>(1L)
    val selectedStudentId: StateFlow<Long> = _selectedStudentId.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    val students: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val staff: StateFlow<List<StaffEntity>> = repository.allStaff
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendanceToday: StateFlow<List<AttendanceEntity>> = _selectedDate
        .flatMapLatest { date -> repository.getAttendanceByDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val examSchedules: StateFlow<List<ExamScheduleEntity>> = repository.allExamSchedules
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allMarks: StateFlow<List<MarkEntity>> = _selectedStudentId
        .flatMapLatest { id -> repository.getMarksForStudent(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val feeStructures: StateFlow<List<FeeStructureEntity>> = repository.allFeeStructures
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val feeVouchers: StateFlow<List<FeeVoucherEntity>> = repository.allFeeVouchers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notices: StateFlow<List<NoticeEntity>> = repository.allNotices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val messages: StateFlow<List<MessageEntity>> = repository.allMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val homework: StateFlow<List<HomeworkEntity>> = repository.allHomework
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
        }
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setSelectedClass(className: String) {
        _selectedClass.value = className
    }

    fun setSelectedStudent(id: Long) {
        _selectedStudentId.value = id
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun showMessage(msg: String) {
        _statusMessage.value = msg
    }

    // --- Student Enrollment / SIS ---
    fun registerStudent(
        fullName: String,
        gender: String,
        dob: String,
        gradeLevel: String,
        className: String,
        section: String,
        rollNo: Int,
        parentName: String,
        parentPhone: String,
        parentEmail: String,
        emergencyContact: String,
        medicalNotes: String,
        address: String
    ) {
        viewModelScope.launch {
            val count = students.value.size + 1
            val regNo = "PLA-2026-00${40 + count}"
            val student = StudentEntity(
                regNo = regNo,
                fullName = fullName,
                gender = gender,
                dob = dob,
                gradeLevel = gradeLevel,
                className = className,
                section = section,
                rollNo = rollNo,
                parentName = parentName,
                parentPhone = parentPhone,
                parentEmail = parentEmail,
                emergencyContact = emergencyContact,
                medicalNotes = medicalNotes.ifBlank { "None / Normal" },
                address = address.ifBlank { "Wan Khara" },
                admissionDate = "2026-08-27",
                status = "Active"
            )
            repository.insertStudent(student)
            showMessage("✅ Student $fullName registered successfully with Reg ID: $regNo")
        }
    }

    // --- Mark Attendance & Absent Alerts ---
    fun updateAttendanceStatus(
        student: StudentEntity,
        status: String,
        remarks: String = ""
    ) {
        viewModelScope.launch {
            val isAbsent = status == "Absent"
            val record = AttendanceEntity(
                studentId = student.id,
                studentName = student.fullName,
                className = student.className,
                section = student.section,
                rollNo = student.rollNo,
                date = _selectedDate.value,
                status = status,
                remarks = remarks,
                alertSent = isAbsent
            )
            repository.saveAttendanceRecord(record)
            if (isAbsent) {
                showMessage("📲 Automated Absence SMS Alert dispatched to parent (${student.parentPhone}) for ${student.fullName}")
            } else {
                showMessage("✅ Attendance updated for ${student.fullName}: $status")
            }
        }
    }

    // --- Add / Calculate Marks ---
    fun addMarkRecord(
        student: StudentEntity,
        examTitle: String,
        subject: String,
        marksObtained: Double,
        totalMarks: Double,
        teacherRemarks: String
    ) {
        viewModelScope.launch {
            repository.saveMark(
                studentId = student.id,
                studentName = student.fullName,
                className = student.className,
                examTitle = examTitle,
                subject = subject,
                marksObtained = marksObtained,
                totalMarks = totalMarks,
                teacherRemarks = teacherRemarks
            )
            val result = GradeCalculator.calculateGrade(marksObtained, totalMarks)
            showMessage("📝 Marks entered for ${student.fullName} in $subject: ${result.grade} (${result.percentage}%)")
        }
    }

    // --- Generate Fee Voucher ---
    fun createFeeVoucher(
        student: StudentEntity,
        monthYear: String,
        tuition: Double,
        admission: Double,
        transport: Double,
        exam: Double,
        sports: Double,
        other: Double,
        discount: Double,
        dueDate: String
    ) {
        viewModelScope.launch {
            val total = (tuition + admission + transport + exam + sports + other) - discount
            val voucherNo = "PLA-VCH-${System.currentTimeMillis().toString().takeLast(6)}"
            val voucher = FeeVoucherEntity(
                voucherNo = voucherNo,
                studentId = student.id,
                studentName = student.fullName,
                regNo = student.regNo,
                className = student.className,
                section = student.section,
                monthYear = monthYear,
                issueDate = "2026-08-27",
                dueDate = dueDate,
                tuitionFee = tuition,
                admissionFee = admission,
                transportFee = transport,
                examFee = exam,
                sportsFee = sports,
                otherCharges = other,
                discount = discount,
                totalAmount = total,
                amountPaid = 0.0,
                paymentStatus = "Pending"
            )
            repository.generateFeeVoucher(voucher)
            showMessage("🧾 Fee Voucher $voucherNo generated for ${student.fullName} (Total: Rs. ${total.toInt()})")
        }
    }

    // --- Collect Fee Payment ---
    fun payFeeVoucher(
        voucher: FeeVoucherEntity,
        amount: Double,
        method: String,
        ref: String
    ) {
        viewModelScope.launch {
            repository.collectPayment(
                voucher = voucher,
                amountToPay = amount,
                paymentMethod = method,
                paymentDate = "2026-08-27",
                refNo = ref.ifBlank { "TXN-${System.currentTimeMillis().toString().takeLast(6)}" }
            )
            showMessage("💳 Payment of Rs. ${amount.toInt()} collected for Voucher ${voucher.voucherNo}")
        }
    }

    // --- Post Notice ---
    fun postNotice(title: String, category: String, content: String, target: String, priority: String) {
        viewModelScope.launch {
            val notice = NoticeEntity(
                title = title,
                category = category,
                content = content,
                publishDate = "2026-08-27",
                targetAudience = target,
                priority = priority,
                author = "Principal Office, Wan Khara"
            )
            repository.createNotice(notice)
            showMessage("📢 School notice published: $title")
        }
    }

    // --- Send Direct Message ---
    fun sendDirectMessage(
        senderRole: String,
        senderName: String,
        receiverRole: String,
        receiverName: String,
        subject: String,
        message: String
    ) {
        viewModelScope.launch {
            val msg = MessageEntity(
                senderRole = senderRole,
                senderName = senderName,
                receiverRole = receiverRole,
                receiverName = receiverName,
                subject = subject,
                message = message,
                timestamp = "2026-08-27 10:00",
                isRead = false
            )
            repository.sendMessage(msg)
            showMessage("✉️ Message delivered to $receiverName")
        }
    }

    // --- Add Homework ---
    fun postHomework(
        className: String,
        subject: String,
        title: String,
        description: String,
        dueDate: String,
        teacherName: String
    ) {
        viewModelScope.launch {
            val hw = HomeworkEntity(
                className = className,
                subject = subject,
                title = title,
                description = description,
                assignedDate = "2026-08-27",
                dueDate = dueDate,
                teacherName = teacherName,
                submissionCount = 0
            )
            repository.createHomework(hw)
            showMessage("📚 Homework assigned for $className ($subject): $title")
        }
    }

    // --- Add Expense ---
    fun recordSchoolExpense(
        title: String,
        category: String,
        amount: Double,
        notes: String
    ) {
        viewModelScope.launch {
            val exp = ExpenseEntity(
                title = title,
                category = category,
                amount = amount,
                expenseDate = "2026-08-27",
                recordedBy = "Mr. Zahid Ali (Accountant)",
                receiptNo = "EXP-${System.currentTimeMillis().toString().takeLast(4)}",
                notes = notes
            )
            repository.recordExpense(exp)
            showMessage("💰 Expense Rs. ${amount.toInt()} recorded under $category")
        }
    }
}
