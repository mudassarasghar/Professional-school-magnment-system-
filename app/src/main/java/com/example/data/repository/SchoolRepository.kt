package com.example.data.repository

import com.example.data.local.dao.SchoolDao
import com.example.data.local.database.InitialDataSeeder
import com.example.data.local.entities.*
import com.example.domain.model.GradeCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SchoolRepository(private val dao: SchoolDao) {

    suspend fun checkAndSeedInitialData() {
        val count = dao.getStudentCount().first()
        if (count == 0) {
            InitialDataSeeder.seedDatabase(dao)
        }
    }

    // Students
    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()
    fun getStudentsByClass(className: String): Flow<List<StudentEntity>> = dao.getStudentsByClass(className)
    suspend fun insertStudent(student: StudentEntity): Long = dao.insertStudent(student)
    suspend fun updateStudent(student: StudentEntity) = dao.updateStudent(student)
    suspend fun deleteStudent(student: StudentEntity) = dao.deleteStudent(student)
    suspend fun getStudentById(id: Long): StudentEntity? = dao.getStudentById(id)

    // Staff
    val allStaff: Flow<List<StaffEntity>> = dao.getAllStaff()
    suspend fun insertStaff(staff: StaffEntity): Long = dao.insertSingleStaff(staff)

    // Attendance
    fun getAttendanceByDate(date: String): Flow<List<AttendanceEntity>> = dao.getAttendanceByDate(date)
    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>> = dao.getAttendanceForStudent(studentId)
    fun getAttendanceForClassAndDate(className: String, date: String): Flow<List<AttendanceEntity>> =
        dao.getAttendanceForClassAndDate(className, date)

    suspend fun saveAttendanceRecord(record: AttendanceEntity): Long = dao.insertAttendanceRecord(record)
    suspend fun saveAttendanceList(records: List<AttendanceEntity>) = dao.insertAttendanceList(records)

    // Exams & Marks
    val allExamSchedules: Flow<List<ExamScheduleEntity>> = dao.getAllExamSchedules()
    fun getExamSchedulesByClass(className: String): Flow<List<ExamScheduleEntity>> =
        dao.getExamSchedulesByClass(className)

    suspend fun insertExamSchedule(schedule: ExamScheduleEntity) = dao.insertExamSchedules(listOf(schedule))

    fun getMarksForStudent(studentId: Long): Flow<List<MarkEntity>> = dao.getMarksForStudent(studentId)
    fun getMarksByClassAndExam(className: String, examTitle: String): Flow<List<MarkEntity>> =
        dao.getMarksByClassAndExam(className, examTitle)

    suspend fun saveMark(
        studentId: Long,
        studentName: String,
        className: String,
        examTitle: String,
        subject: String,
        marksObtained: Double,
        totalMarks: Double,
        teacherRemarks: String
    ): Long {
        val calc = GradeCalculator.calculateGrade(marksObtained, totalMarks)
        val markEntity = MarkEntity(
            studentId = studentId,
            studentName = studentName,
            className = className,
            examTitle = examTitle,
            subject = subject,
            marksObtained = marksObtained,
            totalMarks = totalMarks,
            grade = calc.grade,
            percentage = calc.percentage,
            gradePoint = calc.gpa,
            teacherRemarks = teacherRemarks.ifBlank { calc.remarks }
        )
        return dao.insertSingleMark(markEntity)
    }

    // Fee Structures & Vouchers
    val allFeeStructures: Flow<List<FeeStructureEntity>> = dao.getAllFeeStructures()
    val allFeeVouchers: Flow<List<FeeVoucherEntity>> = dao.getAllFeeVouchers()
    fun getFeeVouchersForStudent(studentId: Long): Flow<List<FeeVoucherEntity>> =
        dao.getFeeVouchersForStudent(studentId)

    suspend fun generateFeeVoucher(voucher: FeeVoucherEntity): Long = dao.insertFeeVoucher(voucher)

    suspend fun collectPayment(
        voucher: FeeVoucherEntity,
        amountToPay: Double,
        paymentMethod: String,
        paymentDate: String,
        refNo: String
    ) {
        val updatedPaid = voucher.amountPaid + amountToPay
        val status = when {
            updatedPaid >= voucher.totalAmount -> "Paid"
            updatedPaid > 0.0 -> "Partial"
            else -> "Pending"
        }
        val updatedVoucher = voucher.copy(
            amountPaid = updatedPaid,
            paymentStatus = status,
            paymentDate = paymentDate,
            paymentMethod = paymentMethod,
            transactionRef = refNo
        )
        dao.updateFeeVoucher(updatedVoucher)
    }

    // Notices
    val allNotices: Flow<List<NoticeEntity>> = dao.getAllNotices()
    suspend fun createNotice(notice: NoticeEntity): Long = dao.insertNotice(notice)

    // Messages
    val allMessages: Flow<List<MessageEntity>> = dao.getAllMessages()
    suspend fun sendMessage(message: MessageEntity): Long = dao.insertMessage(message)

    // Homework
    val allHomework: Flow<List<HomeworkEntity>> = dao.getAllHomework()
    fun getHomeworkForClass(className: String): Flow<List<HomeworkEntity>> = dao.getHomeworkForClass(className)
    suspend fun createHomework(hw: HomeworkEntity): Long = dao.insertSingleHomework(hw)

    // Expenses
    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()
    suspend fun recordExpense(expense: ExpenseEntity): Long = dao.insertExpense(expense)
}
