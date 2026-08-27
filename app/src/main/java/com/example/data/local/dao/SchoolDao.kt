package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {

    // --- Students ---
    @Query("SELECT * FROM students ORDER BY className ASC, rollNo ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE className = :className ORDER BY rollNo ASC")
    fun getStudentsByClass(className: String): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :id")
    suspend fun getStudentById(id: Long): StudentEntity?

    @Query("SELECT * FROM students WHERE regNo = :regNo LIMIT 1")
    suspend fun getStudentByRegNo(regNo: String): StudentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("SELECT COUNT(*) FROM students")
    fun getStudentCount(): Flow<Int>

    // --- Staff ---
    @Query("SELECT * FROM staff ORDER BY role ASC, name ASC")
    fun getAllStaff(): Flow<List<StaffEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: List<StaffEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleStaff(staff: StaffEntity): Long

    // --- Attendance ---
    @Query("SELECT * FROM attendance WHERE date = :date ORDER BY className ASC, rollNo ASC")
    fun getAttendanceByDate(date: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE className = :className AND date = :date ORDER BY rollNo ASC")
    fun getAttendanceForClassAndDate(className: String, date: String): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceList(records: List<AttendanceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendanceRecord(record: AttendanceEntity): Long

    // --- Exams & Marks ---
    @Query("SELECT * FROM exam_schedules ORDER BY examDate ASC, startTime ASC")
    fun getAllExamSchedules(): Flow<List<ExamScheduleEntity>>

    @Query("SELECT * FROM exam_schedules WHERE className = :className ORDER BY examDate ASC")
    fun getExamSchedulesByClass(className: String): Flow<List<ExamScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamSchedules(schedules: List<ExamScheduleEntity>)

    @Query("SELECT * FROM marks WHERE studentId = :studentId ORDER BY subject ASC")
    fun getMarksForStudent(studentId: Long): Flow<List<MarkEntity>>

    @Query("SELECT * FROM marks WHERE className = :className AND examTitle = :examTitle ORDER BY studentName ASC, subject ASC")
    fun getMarksByClassAndExam(className: String, examTitle: String): Flow<List<MarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarks(marks: List<MarkEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleMark(mark: MarkEntity): Long

    // --- Fee Structures & Vouchers ---
    @Query("SELECT * FROM fee_structures ORDER BY className ASC")
    fun getAllFeeStructures(): Flow<List<FeeStructureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeStructures(structures: List<FeeStructureEntity>)

    @Query("SELECT * FROM fee_vouchers ORDER BY id DESC")
    fun getAllFeeVouchers(): Flow<List<FeeVoucherEntity>>

    @Query("SELECT * FROM fee_vouchers WHERE studentId = :studentId ORDER BY id DESC")
    fun getFeeVouchersForStudent(studentId: Long): Flow<List<FeeVoucherEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeVouchers(vouchers: List<FeeVoucherEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeeVoucher(voucher: FeeVoucherEntity): Long

    @Update
    suspend fun updateFeeVoucher(voucher: FeeVoucherEntity)

    // --- Notices & Announcements ---
    @Query("SELECT * FROM notices ORDER BY id DESC")
    fun getAllNotices(): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotices(notices: List<NoticeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticeEntity): Long

    // --- Direct Messages ---
    @Query("SELECT * FROM messages ORDER BY id DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    // --- Homework ---
    @Query("SELECT * FROM homework ORDER BY dueDate ASC")
    fun getAllHomework(): Flow<List<HomeworkEntity>>

    @Query("SELECT * FROM homework WHERE className = :className ORDER BY dueDate ASC")
    fun getHomeworkForClass(className: String): Flow<List<HomeworkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(homeworkList: List<HomeworkEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleHomework(homework: HomeworkEntity): Long

    // --- Expenses & Payroll ---
    @Query("SELECT * FROM expenses ORDER BY id DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<ExpenseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long
}
