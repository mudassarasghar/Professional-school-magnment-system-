package com.example.data.local.database

import com.example.data.local.dao.SchoolDao
import com.example.data.local.entities.*

object InitialDataSeeder {

    suspend fun seedDatabase(dao: SchoolDao) {
        // 1. Seed Fee Structures
        val feeStructures = listOf(
            FeeStructureEntity(
                gradeLevel = "Pre-Primary",
                className = "Nursery",
                tuitionFee = 3200.0,
                admissionFee = 4000.0,
                examFee = 1000.0,
                transportFee = 2000.0,
                sportsFee = 500.0,
                labFee = 0.0
            ),
            FeeStructureEntity(
                gradeLevel = "Pre-Primary",
                className = "Prep",
                tuitionFee = 3500.0,
                admissionFee = 4000.0,
                examFee = 1000.0,
                transportFee = 2000.0,
                sportsFee = 500.0,
                labFee = 0.0
            ),
            FeeStructureEntity(
                gradeLevel = "Primary",
                className = "Grade 5",
                tuitionFee = 4200.0,
                admissionFee = 4500.0,
                examFee = 1200.0,
                transportFee = 2200.0,
                sportsFee = 600.0,
                labFee = 500.0
            ),
            FeeStructureEntity(
                gradeLevel = "Middle",
                className = "Grade 8",
                tuitionFee = 4800.0,
                admissionFee = 5000.0,
                examFee = 1400.0,
                transportFee = 2500.0,
                sportsFee = 800.0,
                labFee = 1000.0
            ),
            FeeStructureEntity(
                gradeLevel = "Secondary",
                className = "Grade 9",
                tuitionFee = 5500.0,
                admissionFee = 6000.0,
                examFee = 1800.0,
                transportFee = 2500.0,
                sportsFee = 1000.0,
                labFee = 1500.0
            ),
            FeeStructureEntity(
                gradeLevel = "Secondary",
                className = "Grade 10",
                tuitionFee = 6000.0,
                admissionFee = 6000.0,
                examFee = 2000.0,
                transportFee = 2500.0,
                sportsFee = 1000.0,
                labFee = 1500.0
            )
        )
        dao.insertFeeStructures(feeStructures)

        // 2. Seed Staff
        val staffList = listOf(
            StaffEntity(
                staffId = "PLA-EMP-01",
                name = "Prof. Rauf Ahmad",
                role = "Principal",
                designation = "Principal / Super Admin",
                email = "principal@paradiselittleangels.edu",
                phone = "+92 300 4589123",
                assignedClass = "Administration",
                assignedSubjects = "Institutional Management",
                monthlySalary = 120000.0,
                joinDate = "2018-03-01"
            ),
            StaffEntity(
                staffId = "PLA-EMP-02",
                name = "Mrs. Shaheen Akhtar",
                role = "Principal",
                designation = "Vice Principal & Academic Head",
                email = "shaheen.akhtar@paradiselittleangels.edu",
                phone = "+92 301 7894561",
                assignedClass = "Academic Oversight",
                assignedSubjects = "Urdu & Islamic Studies",
                monthlySalary = 85000.0,
                joinDate = "2019-08-15"
            ),
            StaffEntity(
                staffId = "PLA-EMP-03",
                name = "Mr. Tariq Mahmood",
                role = "Teacher",
                designation = "Senior Physics & Science Teacher",
                email = "tariq.mahmood@paradiselittleangels.edu",
                phone = "+92 302 6549870",
                assignedClass = "Grade 10",
                assignedSubjects = "Physics & Chemistry",
                monthlySalary = 65000.0,
                joinDate = "2020-02-10"
            ),
            StaffEntity(
                staffId = "PLA-EMP-04",
                name = "Mrs. Farzana Bibi",
                role = "Teacher",
                designation = "Senior English Department Head",
                email = "farzana.bibi@paradiselittleangels.edu",
                phone = "+92 303 1238904",
                assignedClass = "Grade 9",
                assignedSubjects = "English Grammar & Literature",
                monthlySalary = 58000.0,
                joinDate = "2021-01-05"
            ),
            StaffEntity(
                staffId = "PLA-EMP-05",
                name = "Mr. Imran Ali",
                role = "Teacher",
                designation = "Mathematics Specialist",
                email = "imran.ali@paradiselittleangels.edu",
                phone = "+92 304 9876543",
                assignedClass = "Grade 8",
                assignedSubjects = "Algebra & Geometry",
                monthlySalary = 55000.0,
                joinDate = "2021-09-01"
            ),
            StaffEntity(
                staffId = "PLA-EMP-06",
                name = "Mr. Bilal Khan",
                role = "Teacher",
                designation = "Computer Science & IT Instructor",
                email = "bilal.khan@paradiselittleangels.edu",
                phone = "+92 305 4567890",
                assignedClass = "Grade 10",
                assignedSubjects = "Computer Science & Coding",
                monthlySalary = 60000.0,
                joinDate = "2022-04-12"
            ),
            StaffEntity(
                staffId = "PLA-EMP-07",
                name = "Mr. Zahid Ali",
                role = "Accountant",
                designation = "Chief Accountant & Bursar",
                email = "accounts@paradiselittleangels.edu",
                phone = "+92 306 3334455",
                assignedClass = "Accounts Office",
                assignedSubjects = "Fee Collection & Payroll",
                monthlySalary = 70000.0,
                joinDate = "2019-05-20"
            )
        )
        dao.insertStaff(staffList)

        // 3. Seed Students
        val students = listOf(
            StudentEntity(
                id = 1,
                regNo = "PLA-2026-0042",
                fullName = "Ayesha Khan",
                gender = "Female",
                dob = "2010-04-15",
                gradeLevel = "Secondary",
                className = "Grade 10",
                section = "A",
                rollNo = 1,
                parentName = "Muhammad Khan",
                parentPhone = "+92 321 4455667",
                parentEmail = "mkhan.wan@gmail.com",
                emergencyContact = "+92 321 9988776",
                medicalNotes = "Mild Asthma (inhaler kept in medical room)",
                address = "Near Central Mosque, Main Bazar, Wan Khara",
                admissionDate = "2020-04-01",
                status = "Active"
            ),
            StudentEntity(
                id = 2,
                regNo = "PLA-2026-0043",
                fullName = "Hamza Ali",
                gender = "Male",
                dob = "2010-07-22",
                gradeLevel = "Secondary",
                className = "Grade 10",
                section = "A",
                rollNo = 2,
                parentName = "Tariq Ali",
                parentPhone = "+92 322 5566778",
                parentEmail = "tariqali.wankhara@gmail.com",
                emergencyContact = "+92 322 8877665",
                medicalNotes = "No known allergies / Fully Fit",
                address = "Railway Road, Wan Khara",
                admissionDate = "2020-04-01",
                status = "Active"
            ),
            StudentEntity(
                id = 3,
                regNo = "PLA-2026-0044",
                fullName = "Zainab Fatima",
                gender = "Female",
                dob = "2010-09-11",
                gradeLevel = "Secondary",
                className = "Grade 10",
                section = "A",
                rollNo = 3,
                parentName = "Rashid Mahmood",
                parentPhone = "+92 323 6677889",
                parentEmail = "rmahmood@gmail.com",
                emergencyContact = "+92 323 7766554",
                medicalNotes = "Spectacles for distant vision",
                address = "Model Town, Wan Khara",
                admissionDate = "2021-03-15",
                status = "Active"
            ),
            StudentEntity(
                id = 4,
                regNo = "PLA-2026-0045",
                fullName = "Bilal Ahmed",
                gender = "Male",
                dob = "2010-12-05",
                gradeLevel = "Secondary",
                className = "Grade 10",
                section = "A",
                rollNo = 4,
                parentName = "Naveed Ahmed",
                parentPhone = "+92 324 7788990",
                parentEmail = "nahmed.wan@gmail.com",
                emergencyContact = "+92 324 6655443",
                medicalNotes = "None",
                address = "Canal View Colony, Wan Khara",
                admissionDate = "2020-04-01",
                status = "Active"
            ),
            StudentEntity(
                id = 5,
                regNo = "PLA-2026-0046",
                fullName = "Maryam Tariq",
                gender = "Female",
                dob = "2011-03-18",
                gradeLevel = "Secondary",
                className = "Grade 9",
                section = "A",
                rollNo = 1,
                parentName = "Tariq Mahmood",
                parentPhone = "+92 302 6549870",
                parentEmail = "tariq.m@yahoo.com",
                emergencyContact = "+92 300 1122334",
                medicalNotes = "None / Athletic",
                address = "Teacher Colony, Wan Khara",
                admissionDate = "2021-04-01",
                status = "Active"
            ),
            StudentEntity(
                id = 6,
                regNo = "PLA-2026-0047",
                fullName = "Usman Ghani",
                gender = "Male",
                dob = "2012-06-30",
                gradeLevel = "Middle",
                className = "Grade 8",
                section = "A",
                rollNo = 1,
                parentName = "Abdul Ghani",
                parentPhone = "+92 333 4455667",
                parentEmail = "aghani.trade@gmail.com",
                emergencyContact = "+92 333 9988112",
                medicalNotes = "Peanut allergy",
                address = "Grain Market Road, Wan Khara",
                admissionDate = "2022-04-01",
                status = "Active"
            ),
            StudentEntity(
                id = 7,
                regNo = "PLA-2026-0048",
                fullName = "Fatima Noor",
                gender = "Female",
                dob = "2015-08-14",
                gradeLevel = "Primary",
                className = "Grade 5",
                section = "A",
                rollNo = 1,
                parentName = "Noor Muhammad",
                parentPhone = "+92 334 5566778",
                parentEmail = "noor.m@gmail.com",
                emergencyContact = "+92 334 8877665",
                medicalNotes = "None",
                address = "Hospital Road, Wan Khara",
                admissionDate = "2023-04-01",
                status = "Active"
            ),
            StudentEntity(
                id = 8,
                regNo = "PLA-2026-0049",
                fullName = "Rayyan Asif",
                gender = "Male",
                dob = "2021-01-20",
                gradeLevel = "Pre-Primary",
                className = "Nursery",
                section = "A",
                rollNo = 1,
                parentName = "Asif Javed",
                parentPhone = "+92 335 6677889",
                parentEmail = "asif.javed@gmail.com",
                emergencyContact = "+92 335 7766554",
                medicalNotes = "None",
                address = "Green Town, Wan Khara",
                admissionDate = "2025-04-01",
                status = "Active"
            )
        )
        dao.insertStudents(students)

        // 4. Seed Attendance
        val today = "2026-08-27"
        val attendanceList = listOf(
            AttendanceEntity(
                studentId = 1,
                studentName = "Ayesha Khan",
                className = "Grade 10",
                section = "A",
                rollNo = 1,
                date = today,
                status = "Present",
                remarks = "On time",
                alertSent = false
            ),
            AttendanceEntity(
                studentId = 2,
                studentName = "Hamza Ali",
                className = "Grade 10",
                section = "A",
                rollNo = 2,
                date = today,
                status = "Absent",
                remarks = "Uninformed Absence",
                alertSent = true
            ),
            AttendanceEntity(
                studentId = 3,
                studentName = "Zainab Fatima",
                className = "Grade 10",
                section = "A",
                rollNo = 3,
                date = today,
                status = "Present",
                remarks = "On time",
                alertSent = false
            ),
            AttendanceEntity(
                studentId = 4,
                studentName = "Bilal Ahmed",
                className = "Grade 10",
                section = "A",
                rollNo = 4,
                date = today,
                status = "Late",
                remarks = "Arrived 8:20 AM (Transport issue)",
                alertSent = false
            ),
            AttendanceEntity(
                studentId = 5,
                studentName = "Maryam Tariq",
                className = "Grade 9",
                section = "A",
                rollNo = 1,
                date = today,
                status = "Present",
                remarks = "On time",
                alertSent = false
            ),
            AttendanceEntity(
                studentId = 6,
                studentName = "Usman Ghani",
                className = "Grade 8",
                section = "A",
                rollNo = 1,
                date = today,
                status = "Excused",
                remarks = "Sick Leave note submitted",
                alertSent = false
            ),
            AttendanceEntity(
                studentId = 7,
                studentName = "Fatima Noor",
                className = "Grade 5",
                section = "A",
                rollNo = 1,
                date = today,
                status = "Present",
                remarks = "On time",
                alertSent = false
            ),
            AttendanceEntity(
                studentId = 8,
                studentName = "Rayyan Asif",
                className = "Nursery",
                section = "A",
                rollNo = 1,
                date = today,
                status = "Present",
                remarks = "On time",
                alertSent = false
            )
        )
        dao.insertAttendanceList(attendanceList)

        // 5. Seed Exam Schedules
        val examSchedules = listOf(
            ExamScheduleEntity(
                examTitle = "Mid-Term Examination 2026",
                gradeLevel = "Secondary",
                className = "Grade 10",
                subject = "Physics",
                examDate = "2026-09-10",
                startTime = "08:30 AM",
                endTime = "11:30 AM",
                roomNo = "Hall A",
                totalMarks = 100.0,
                syllabusCoverage = "Unit 10 (SHM) to Unit 14 (Current Electricity)"
            ),
            ExamScheduleEntity(
                examTitle = "Mid-Term Examination 2026",
                gradeLevel = "Secondary",
                className = "Grade 10",
                subject = "Mathematics",
                examDate = "2026-09-12",
                startTime = "08:30 AM",
                endTime = "11:30 AM",
                roomNo = "Hall A",
                totalMarks = 100.0,
                syllabusCoverage = "Quadratic Equations, Variations & Matrices"
            ),
            ExamScheduleEntity(
                examTitle = "Mid-Term Examination 2026",
                gradeLevel = "Secondary",
                className = "Grade 10",
                subject = "English",
                examDate = "2026-09-15",
                startTime = "08:30 AM",
                endTime = "11:30 AM",
                roomNo = "Hall B",
                totalMarks = 100.0,
                syllabusCoverage = "Grammar, Comprehension, Essay & Unit 1-6"
            ),
            ExamScheduleEntity(
                examTitle = "Mid-Term Examination 2026",
                gradeLevel = "Secondary",
                className = "Grade 10",
                subject = "Computer Science",
                examDate = "2026-09-17",
                startTime = "08:30 AM",
                endTime = "11:30 AM",
                roomNo = "Computer Lab",
                totalMarks = 100.0,
                syllabusCoverage = "Programming in C++, Functions & Arrays"
            )
        )
        dao.insertExamSchedules(examSchedules)

        // 6. Seed Marks for Ayesha Khan and Hamza Ali
        val marks = listOf(
            MarkEntity(
                studentId = 1,
                studentName = "Ayesha Khan",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "Physics",
                marksObtained = 94.0,
                totalMarks = 100.0,
                grade = "A+",
                percentage = 94.0,
                gradePoint = 4.0,
                teacherRemarks = "Outstanding conceptual understanding in numericals."
            ),
            MarkEntity(
                studentId = 1,
                studentName = "Ayesha Khan",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "Mathematics",
                marksObtained = 98.0,
                totalMarks = 100.0,
                grade = "A+",
                percentage = 98.0,
                gradePoint = 4.0,
                teacherRemarks = "Exceptional algebraic proof techniques."
            ),
            MarkEntity(
                studentId = 1,
                studentName = "Ayesha Khan",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "English",
                marksObtained = 88.0,
                totalMarks = 100.0,
                grade = "A",
                percentage = 88.0,
                gradePoint = 3.7,
                teacherRemarks = "Impressive vocabulary and essay articulation."
            ),
            MarkEntity(
                studentId = 1,
                studentName = "Ayesha Khan",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "Chemistry",
                marksObtained = 91.0,
                totalMarks = 100.0,
                grade = "A+",
                percentage = 91.0,
                gradePoint = 4.0,
                teacherRemarks = "Excellent laboratory and theoretical grasp."
            ),
            MarkEntity(
                studentId = 1,
                studentName = "Ayesha Khan",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "Computer Science",
                marksObtained = 96.0,
                totalMarks = 100.0,
                grade = "A+",
                percentage = 96.0,
                gradePoint = 4.0,
                teacherRemarks = "Top score in coding logic and syntax."
            ),
            MarkEntity(
                studentId = 2,
                studentName = "Hamza Ali",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "Physics",
                marksObtained = 76.0,
                totalMarks = 100.0,
                grade = "B",
                percentage = 76.0,
                gradePoint = 3.0,
                teacherRemarks = "Good effort, needs more practice on circuit diagrams."
            ),
            MarkEntity(
                studentId = 2,
                studentName = "Hamza Ali",
                className = "Grade 10",
                examTitle = "First Term Assessment 2026",
                subject = "Mathematics",
                marksObtained = 82.0,
                totalMarks = 100.0,
                grade = "A",
                percentage = 82.0,
                gradePoint = 3.3,
                teacherRemarks = "Solid analytical skills."
            )
        )
        dao.insertMarks(marks)

        // 7. Seed Fee Vouchers
        val feeVouchers = listOf(
            FeeVoucherEntity(
                voucherNo = "PLA-VCH-202608-0042",
                studentId = 1,
                studentName = "Ayesha Khan",
                regNo = "PLA-2026-0042",
                className = "Grade 10",
                section = "A",
                monthYear = "August 2026",
                issueDate = "2026-08-01",
                dueDate = "2026-08-15",
                tuitionFee = 6000.0,
                admissionFee = 0.0,
                transportFee = 2500.0,
                examFee = 0.0,
                sportsFee = 1000.0,
                otherCharges = 0.0,
                discount = 500.0, // Academic merit scholarship
                totalAmount = 9000.0,
                amountPaid = 9000.0,
                paymentStatus = "Paid",
                paymentDate = "2026-08-08",
                paymentMethod = "Online Portal (HBL Habib Bank)",
                transactionRef = "HBL-TXN-8849201"
            ),
            FeeVoucherEntity(
                voucherNo = "PLA-VCH-202608-0043",
                studentId = 2,
                studentName = "Hamza Ali",
                regNo = "PLA-2026-0043",
                className = "Grade 10",
                section = "A",
                monthYear = "August 2026",
                issueDate = "2026-08-01",
                dueDate = "2026-08-15",
                tuitionFee = 6000.0,
                admissionFee = 0.0,
                transportFee = 2500.0,
                examFee = 0.0,
                sportsFee = 1000.0,
                otherCharges = 200.0,
                discount = 0.0,
                totalAmount = 9700.0,
                amountPaid = 5000.0,
                paymentStatus = "Partial",
                paymentDate = "2026-08-14",
                paymentMethod = "Cash at Accounts Office",
                transactionRef = "RCP-PLA-1092"
            ),
            FeeVoucherEntity(
                voucherNo = "PLA-VCH-202608-0044",
                studentId = 3,
                studentName = "Zainab Fatima",
                regNo = "PLA-2026-0044",
                className = "Grade 10",
                section = "A",
                monthYear = "August 2026",
                issueDate = "2026-08-01",
                dueDate = "2026-08-15",
                tuitionFee = 6000.0,
                admissionFee = 0.0,
                transportFee = 0.0,
                examFee = 0.0,
                sportsFee = 1000.0,
                otherCharges = 0.0,
                discount = 0.0,
                totalAmount = 7000.0,
                amountPaid = 0.0,
                paymentStatus = "Overdue",
                paymentDate = null,
                paymentMethod = null,
                transactionRef = null
            ),
            FeeVoucherEntity(
                voucherNo = "PLA-VCH-202608-0045",
                studentId = 4,
                studentName = "Bilal Ahmed",
                regNo = "PLA-2026-0045",
                className = "Grade 10",
                section = "A",
                monthYear = "August 2026",
                issueDate = "2026-08-01",
                dueDate = "2026-08-30",
                tuitionFee = 6000.0,
                admissionFee = 0.0,
                transportFee = 2500.0,
                examFee = 0.0,
                sportsFee = 1000.0,
                otherCharges = 0.0,
                discount = 0.0,
                totalAmount = 9500.0,
                amountPaid = 0.0,
                paymentStatus = "Pending",
                paymentDate = null,
                paymentMethod = null,
                transactionRef = null
            )
        )
        dao.insertFeeVouchers(feeVouchers)

        // 8. Seed Notices & Announcements
        val notices = listOf(
            NoticeEntity(
                title = "Mid-Term Examinations Timetable 2026",
                category = "Academic",
                content = "Mid-Term Examination dates for Pre-Primary to Secondary sections have been scheduled starting September 10, 2026. Admit cards will be issued from the Principal Office on clearance of tuition fee vouchers.",
                publishDate = "2026-08-25",
                targetAudience = "All",
                priority = "High",
                author = "Office of the Principal, Prof. Rauf Ahmad"
            ),
            NoticeEntity(
                title = "Annual Inter-School Sports Gala - Wan Khara",
                category = "Event",
                content = "Paradise Little Angels Secondary School is proud to host the Wan Khara District Inter-School Sports Gala on October 15-17, 2026. Events include Cricket, Badminton, 100m Sprint, and Tug of War. Students must register with Mr. Tariq Mahmood by next Friday.",
                publishDate = "2026-08-22",
                targetAudience = "Students",
                priority = "Normal",
                author = "Sports Department"
            ),
            NoticeEntity(
                title = "Parent-Teacher Council Meeting (PTM)",
                category = "Academic",
                content = "Mandatory Parent-Teacher Meeting will be held on Saturday, September 5th from 9:00 AM to 1:00 PM. Parents are invited to discuss academic progress, first term mark sheets, and discipline records.",
                publishDate = "2026-08-20",
                targetAudience = "Parents",
                priority = "High",
                author = "Academic Head, Mrs. Shaheen Akhtar"
            ),
            NoticeEntity(
                title = "Independence Day & Cultural Exhibition Recap",
                category = "Event",
                content = "Congratulations to all student participants who presented outstanding science models and national heritage stalls at the campus auditorium.",
                publishDate = "2026-08-16",
                targetAudience = "All",
                priority = "Normal",
                author = "Cultural Committee"
            )
        )
        dao.insertNotices(notices)

        // 9. Seed Messages
        val messages = listOf(
            MessageEntity(
                senderRole = "Parent",
                senderName = "Muhammad Khan (Parent of Ayesha Khan)",
                receiverRole = "Teacher",
                receiverName = "Mr. Tariq Mahmood (Physics Teacher)",
                subject = "Inquiry regarding Board Exam Preparation",
                message = "Respected Sir, I wanted to thank you for guiding Ayesha in Physics. Please let me know if she needs any supplementary reference books for the upcoming Board assessments.",
                timestamp = "2026-08-26 14:30",
                isRead = true
            ),
            MessageEntity(
                senderRole = "Teacher",
                senderName = "Mr. Tariq Mahmood",
                receiverRole = "Parent",
                receiverName = "Muhammad Khan",
                subject = "Re: Inquiry regarding Board Exam Preparation",
                message = "Respected Parent, Ayesha is performing exceptionally well in class tests (94% in recent assessment). I recommend the Federal & Punjab Board past 5-year question bank for practice. She is well on track for A+.",
                timestamp = "2026-08-26 16:15",
                isRead = true
            ),
            MessageEntity(
                senderRole = "Teacher",
                senderName = "Mrs. Farzana Bibi (English)",
                receiverRole = "Parent",
                receiverName = "Tariq Ali (Parent of Hamza Ali)",
                subject = "Notice regarding Uninformed Absence",
                message = "Respected Parent, Hamza was marked absent today without prior leave submission. Please ensure regular attendance as final revision lessons for Grade 10 are underway.",
                timestamp = "2026-08-27 09:15",
                isRead = false
            )
        )
        for (msg in messages) {
            dao.insertMessage(msg)
        }

        // 10. Seed Homework
        val homeworkList = listOf(
            HomeworkEntity(
                className = "Grade 10",
                subject = "Physics",
                title = "Simple Harmonic Motion Numerical Problems",
                description = "Solve exercises 10.1 to 10.8 from Chapter 10 in neat homework notebook. Focus on calculating time period of simple pendulum and spring constant.",
                assignedDate = "2026-08-26",
                dueDate = "2026-08-29",
                teacherName = "Mr. Tariq Mahmood",
                submissionCount = 28
            ),
            HomeworkEntity(
                className = "Grade 10",
                subject = "Mathematics",
                title = "Quadratic Equation Applications",
                description = "Complete Exercise 1.2 questions 1-10 using quadratic formula and completing square method.",
                assignedDate = "2026-08-27",
                dueDate = "2026-08-30",
                teacherName = "Mr. Imran Ali",
                submissionCount = 14
            ),
            HomeworkEntity(
                className = "Grade 10",
                subject = "Computer Science",
                title = "Nested Loops in C++ Exercise",
                description = "Write a C++ program that prints diamond star patterns and calculates prime numbers up to 100. Submit source code via portal or lab USB.",
                assignedDate = "2026-08-25",
                dueDate = "2026-08-28",
                teacherName = "Mr. Bilal Khan",
                submissionCount = 32
            )
        )
        dao.insertHomework(homeworkList)

        // 11. Seed Expenses & Payroll
        val expenses = listOf(
            ExpenseEntity(
                title = "Monthly Staff Payroll - July 2026",
                category = "Payroll",
                amount = 493000.0,
                expenseDate = "2026-08-01",
                recordedBy = "Mr. Zahid Ali (Accountant)",
                receiptNo = "PAY-2026-07",
                notes = "Salaries disbursed for 18 teaching and administrative staff members."
            ),
            ExpenseEntity(
                title = "Science & Computer Lab Equipment Upgrade",
                category = "Lab",
                amount = 45000.0,
                expenseDate = "2026-08-10",
                recordedBy = "Mr. Zahid Ali (Accountant)",
                receiptNo = "EXP-LAB-891",
                notes = "Purchase of new Vernier calipers, glassware, and 5 optical mice."
            ),
            ExpenseEntity(
                title = "School Backup Generator Diesel Refill",
                category = "Utilities",
                amount = 28500.0,
                expenseDate = "2026-08-18",
                recordedBy = "Ms. Saima Noor (Admin)",
                receiptNo = "EXP-GEN-442",
                notes = "100 Liters diesel for Wan Khara campus load-shedding backup."
            ),
            ExpenseEntity(
                title = "Examination Answer Sheets & Stationery Printing",
                category = "Stationery",
                amount = 22000.0,
                expenseDate = "2026-08-23",
                recordedBy = "Mr. Zahid Ali (Accountant)",
                receiptNo = "EXP-PRT-302",
                notes = "Official Paradise Little Angels header answer sheets and report cards."
            )
        )
        dao.insertExpenses(expenses)
    }
}
