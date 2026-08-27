package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.MarkEntity
import com.example.data.local.entities.StaffEntity
import com.example.data.local.entities.StudentEntity
import com.example.ui.theme.*

/**
 * High-Resolution Document Engine for Paradise Little Angels Secondary School Wan Khara.
 * Generates crisp, professional vector previews and production-ready HTML/CSS print templates.
 */
object DocumentHtmlGenerator {

    private const val SCHOOL_NAME = "PARADISE LITTLE ANGELS SECONDARY SCHOOL"
    private const val CAMPUS_NAME = "Wan Khara Campus, District Kasur / Lahore Border"
    private const val CONTACT_INFO = "Tel: +92 300 4892100 | Web: paradiselittleangels.edu.pk"

    private val BASE_CSS = """
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Playfair+Display:wght@700&family=JetBrains+Mono:wght@500;700&display=swap');
        
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }
        
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f1f5f9;
            color: #0f172a;
            padding: 24px;
            -webkit-print-color-adjust: exact;
            print-color-adjust: exact;
        }
        
        .page-container {
            max-width: 800px;
            margin: 0 auto;
            background: #ffffff;
            box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.1);
            border-radius: 12px;
            overflow: hidden;
            border: 1px solid #cbd5e1;
        }
        
        .header {
            background: linear-gradient(135deg, #0f2c59 0%, #070c18 100%);
            color: #ffffff;
            padding: 24px 32px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 3px solid #f59e0b;
        }
        
        .header-brand h1 {
            font-size: 20px;
            font-weight: 800;
            letter-spacing: 0.5px;
            color: #ffffff;
            margin-bottom: 4px;
            text-transform: uppercase;
        }
        
        .header-brand p {
            font-size: 12px;
            color: #fde68a;
            font-weight: 600;
            letter-spacing: 0.25px;
        }
        
        .doc-badge {
            background: rgba(245, 158, 11, 0.2);
            border: 1px solid #f59e0b;
            color: #fde68a;
            padding: 6px 14px;
            border-radius: 6px;
            font-size: 11px;
            font-weight: 700;
            letter-spacing: 0.75px;
            text-transform: uppercase;
        }
        
        .content-body {
            padding: 28px 32px;
        }
        
        .meta-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 12px 24px;
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            padding: 16px;
            margin-bottom: 24px;
        }
        
        .meta-item {
            font-size: 13px;
        }
        
        .meta-label {
            color: #64748b;
            font-weight: 500;
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 2px;
        }
        
        .meta-value {
            color: #0f172a;
            font-weight: 700;
            font-size: 14px;
        }
        
        table.doc-table {
            width: 100%;
            border-collapse: collapse;
            margin: 16px 0 24px;
            font-size: 13px;
        }
        
        table.doc-table th {
            background: #0f2c59;
            color: #ffffff;
            font-weight: 700;
            text-align: left;
            padding: 10px 14px;
            border: 1px solid #0f2c59;
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        
        table.doc-table td {
            padding: 10px 14px;
            border: 1px solid #e2e8f0;
            color: #1e293b;
        }
        
        table.doc-table tr:nth-child(even) td {
            background-color: #f8fafc;
        }
        
        .total-box {
            background: #0f2c59;
            color: #ffffff;
            border-radius: 8px;
            padding: 16px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
        }
        
        .total-label {
            font-size: 14px;
            font-weight: 600;
            color: #e2e8f0;
        }
        
        .total-amount {
            font-size: 22px;
            font-weight: 800;
            color: #fde68a;
            font-family: 'JetBrains Mono', monospace;
        }
        
        .barcode-section {
            background: #ffffff;
            border: 1px dashed #94a3b8;
            border-radius: 8px;
            padding: 12px;
            text-align: center;
            margin: 20px 0;
        }
        
        .barcode-bars {
            height: 36px;
            background: repeating-linear-gradient(
                90deg,
                #000000 0px,
                #000000 2px,
                #ffffff 2px,
                #ffffff 4px,
                #000000 4px,
                #000000 7px,
                #ffffff 7px,
                #ffffff 9px
            );
            margin: 0 auto 6px;
            max-width: 280px;
        }
        
        .barcode-text {
            font-family: 'JetBrains Mono', monospace;
            font-size: 11px;
            font-weight: 700;
            color: #0f172a;
            letter-spacing: 2px;
        }
        
        .signatures-row {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-top: 40px;
            padding-top: 20px;
            border-top: 1px solid #e2e8f0;
        }
        
        .sign-box {
            text-align: center;
            min-width: 160px;
        }
        
        .sign-line {
            height: 1px;
            background: #0f172a;
            margin-bottom: 6px;
        }
        
        .sign-title {
            font-size: 12px;
            font-weight: 700;
            color: #0f172a;
        }
        
        .sign-sub {
            font-size: 10px;
            color: #64748b;
        }
        
        .seal-stamp {
            border: 2px dashed #f59e0b;
            color: #b45309;
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 10px;
            font-weight: 800;
            text-align: center;
            text-transform: uppercase;
            transform: rotate(-8deg);
        }
        
        .footer-note {
            background: #f8fafc;
            border-top: 1px solid #e2e8f0;
            padding: 12px 32px;
            font-size: 10px;
            color: #64748b;
            text-align: center;
        }
        
        @media print {
            body {
                background: #ffffff;
                padding: 0;
            }
            .page-container {
                box-shadow: none;
                border: none;
                border-radius: 0;
                max-width: 100%;
            }
            @page {
                size: A4;
                margin: 12mm;
            }
        }
    """.trimIndent()

    fun generateStudentIdCardHtml(student: StudentEntity): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Student ID Card - ${student.fullName}</title>
                <style>
                    $BASE_CSS
                    .id-card-wrap {
                        display: flex;
                        gap: 24px;
                        justify-content: center;
                        flex-wrap: wrap;
                        padding: 30px;
                    }
                    .id-card {
                        width: 320px;
                        height: 480px;
                        background: #0f2c59;
                        border-radius: 16px;
                        overflow: hidden;
                        box-shadow: 0 10px 20px rgba(0,0,0,0.25);
                        border: 2px solid #38bdf8;
                        position: relative;
                        color: #ffffff;
                        display: flex;
                        flex-direction: column;
                    }
                    .id-card-back {
                        background: #0a1120;
                        border-color: #f59e0b;
                    }
                    .id-header {
                        padding: 16px;
                        text-align: center;
                        background: linear-gradient(180deg, #1e3a8a 0%, #0f2c59 100%);
                        border-bottom: 2px solid #38bdf8;
                    }
                    .id-photo-box {
                        width: 100px;
                        height: 110px;
                        margin: 16px auto 12px;
                        background: #1e293b;
                        border: 3px solid #38bdf8;
                        border-radius: 12px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        font-size: 11px;
                        font-weight: bold;
                        color: #38bdf8;
                    }
                    .id-details {
                        padding: 0 20px;
                        text-align: center;
                        flex: 1;
                    }
                    .id-name {
                        font-size: 18px;
                        font-weight: 800;
                        color: #ffffff;
                        margin-bottom: 4px;
                    }
                    .id-reg {
                        color: #38bdf8;
                        font-family: 'JetBrains Mono', monospace;
                        font-size: 13px;
                        font-weight: 700;
                        margin-bottom: 12px;
                    }
                    .id-meta-tag {
                        background: rgba(255,255,255,0.1);
                        border-radius: 6px;
                        padding: 6px;
                        font-size: 12px;
                        margin-bottom: 6px;
                    }
                </style>
            </head>
            <body>
                <div class="id-card-wrap">
                    <!-- Front Side -->
                    <div class="id-card">
                        <div class="id-header">
                            <h3 style="font-size: 13px; font-weight: 800; letter-spacing: 0.5px; text-transform: uppercase;">$SCHOOL_NAME</h3>
                            <p style="font-size: 9px; color: #fde68a; font-weight: 600;">WAN KHARA CAMPUS • SMART ID CARD</p>
                        </div>
                        <div class="id-photo-box">PHOTO</div>
                        <div class="id-details">
                            <div class="id-name">${student.fullName}</div>
                            <div class="id-reg">REG: ${student.regNo}</div>
                            <div class="id-meta-tag"><strong>Class:</strong> ${student.className} - Section ${student.section}</div>
                            <div class="id-meta-tag"><strong>Roll Number:</strong> #${student.rollNo}</div>
                            <div class="id-meta-tag"><strong>Guardian:</strong> ${student.parentName}</div>
                        </div>
                        <div style="background: #ffffff; padding: 6px; margin: 10px 16px 14px; border-radius: 6px; text-align: center;">
                            <div class="barcode-bars" style="height: 24px; max-width: 200px;"></div>
                            <div class="barcode-text" style="font-size: 9px;">*${student.regNo}*</div>
                        </div>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun generateFeeVoucherHtml(voucher: FeeVoucherEntity): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Fee Voucher - ${voucher.voucherNo}</title>
                <style>$BASE_CSS</style>
            </head>
            <body>
                <div class="page-container">
                    <div class="header">
                        <div class="header-brand">
                            <h1>$SCHOOL_NAME</h1>
                            <p>$CAMPUS_NAME</p>
                        </div>
                        <div class="doc-badge">Official Fee Challan</div>
                    </div>
                    <div class="content-body">
                        <div class="meta-grid">
                            <div class="meta-item">
                                <div class="meta-label">Voucher Serial No</div>
                                <div class="meta-value" style="font-family: 'JetBrains Mono', monospace;">${voucher.voucherNo}</div>
                            </div>
                            <div class="meta-item">
                                <div class="meta-label">Billing Month</div>
                                <div class="meta-value">${voucher.monthYear}</div>
                            </div>
                            <div class="meta-item">
                                <div class="meta-label">Student Name</div>
                                <div class="meta-value">${voucher.studentName}</div>
                            </div>
                            <div class="meta-item">
                                <div class="meta-label">Registration / Roll No</div>
                                <div class="meta-value">${voucher.regNo} (Class ${voucher.className}-${voucher.section})</div>
                            </div>
                            <div class="meta-item">
                                <div class="meta-label">Issue Date</div>
                                <div class="meta-value">${voucher.issueDate}</div>
                            </div>
                            <div class="meta-item">
                                <div class="meta-label">Due Date</div>
                                <div class="meta-value" style="color: #dc2626;">${voucher.dueDate}</div>
                            </div>
                        </div>

                        <table class="doc-table">
                            <thead>
                                <tr>
                                    <th>Fee Head Description</th>
                                    <th style="text-align: right;">Amount (PKR)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>Monthly Tuition Fee</td><td style="text-align: right; font-weight: 600;">Rs. ${voucher.tuitionFee.toInt()}</td></tr>
                                ${if (voucher.transportFee > 0) "<tr><td>Transport / Van Facility</td><td style='text-align: right;'>Rs. ${voucher.transportFee.toInt()}</td></tr>" else ""}
                                ${if (voucher.sportsFee > 0) "<tr><td>Sports & Physical Activities</td><td style='text-align: right;'>Rs. ${voucher.sportsFee.toInt()}</td></tr>" else ""}
                                ${if (voucher.admissionFee > 0) "<tr><td>Annual / Admission Fund</td><td style='text-align: right;'>Rs. ${voucher.admissionFee.toInt()}</td></tr>" else ""}
                                ${if (voucher.examFee > 0) "<tr><td>Examination / Paper Charges</td><td style='text-align: right;'>Rs. ${voucher.examFee.toInt()}</td></tr>" else ""}
                                ${if (voucher.otherCharges > 0) "<tr><td>Lab & Portal Utilities</td><td style='text-align: right;'>Rs. ${voucher.otherCharges.toInt()}</td></tr>" else ""}
                                ${if (voucher.discount > 0) "<tr style='color: #059669;'><td>Merit / Sibling Concession</td><td style='text-align: right;'>- Rs. ${voucher.discount.toInt()}</td></tr>" else ""}
                            </tbody>
                        </table>

                        <div class="total-box">
                            <div>
                                <div class="total-label">Total Payable Amount</div>
                                <div style="font-size: 11px; color: #cbd5e1;">Status: ${voucher.paymentStatus.uppercase()}</div>
                            </div>
                            <div class="total-amount">Rs. ${voucher.totalAmount.toInt()}</div>
                        </div>

                        <div class="barcode-section">
                            <div class="barcode-bars"></div>
                            <div class="barcode-text">${voucher.voucherNo}</div>
                        </div>

                        <div class="signatures-row">
                            <div class="sign-box">
                                <div class="sign-line"></div>
                                <div class="sign-title">Accountant Signature</div>
                                <div class="sign-sub">Accounts Dept, Wan Khara</div>
                            </div>
                            <div class="seal-stamp">Official Stamp</div>
                            <div class="sign-box">
                                <div class="sign-line"></div>
                                <div class="sign-title">Bank / Cashier Stamp</div>
                                <div class="sign-sub">Authorized Collection</div>
                            </div>
                        </div>
                    </div>
                    <div class="footer-note">
                        $CONTACT_INFO • Triplicate System (Bank / School / Student Copy)
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun generateReportCardHtml(student: StudentEntity, marks: List<MarkEntity>): String {
        val totalObtained = marks.sumOf { it.marksObtained }
        val totalMax = marks.sumOf { it.totalMarks }.coerceAtLeast(1.0)
        val percentage = (totalObtained / totalMax) * 100.0
        val grade = when {
            percentage >= 90.0 -> "A+"
            percentage >= 80.0 -> "A"
            percentage >= 70.0 -> "B"
            percentage >= 60.0 -> "C"
            percentage >= 50.0 -> "D"
            else -> "F"
        }

        val rows = marks.joinToString("\n") { mark ->
            "<tr><td><strong>${mark.subject}</strong></td><td style='text-align:center;'>${mark.totalMarks.toInt()}</td><td style='text-align:center; font-weight:700;'>${mark.marksObtained.toInt()}</td><td style='text-align:center;'><span style='background:#0f2c59; color:#fff; padding:2px 8px; border-radius:4px; font-weight:700;'>${mark.grade}</span></td><td>${mark.teacherRemarks}</td></tr>"
        }

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Academic DMC - ${student.fullName}</title>
                <style>$BASE_CSS</style>
            </head>
            <body>
                <div class="page-container">
                    <div class="header">
                        <div class="header-brand">
                            <h1>$SCHOOL_NAME</h1>
                            <p>$CAMPUS_NAME</p>
                        </div>
                        <div class="doc-badge">Detailed Marks Certificate</div>
                    </div>
                    <div class="content-body">
                        <div class="meta-grid">
                            <div class="meta-item"><div class="meta-label">Candidate Name</div><div class="meta-value">${student.fullName}</div></div>
                            <div class="meta-item"><div class="meta-label">Registration No</div><div class="meta-value">${student.regNo}</div></div>
                            <div class="meta-item"><div class="meta-label">Class & Section</div><div class="meta-value">${student.className} (Sec ${student.section})</div></div>
                            <div class="meta-item"><div class="meta-label">Roll Number</div><div class="meta-value">#${student.rollNo}</div></div>
                        </div>

                        <table class="doc-table">
                            <thead>
                                <tr>
                                    <th>Subject</th>
                                    <th style="text-align:center;">Max Marks</th>
                                    <th style="text-align:center;">Obtained</th>
                                    <th style="text-align:center;">Grade</th>
                                    <th>Teacher Remarks</th>
                                </tr>
                            </thead>
                            <tbody>
                                $rows
                            </tbody>
                        </table>

                        <div class="total-box">
                            <div>
                                <div class="total-label">Aggregate Academic Score</div>
                                <div style="font-size: 12px; color: #cbd5e1;">Percentage: ${"%.1f".format(percentage)}%</div>
                            </div>
                            <div style="text-align: right;">
                                <div class="total-amount">${totalObtained.toInt()} / ${totalMax.toInt()}</div>
                                <div style="color: #fde68a; font-weight: 700; font-size: 14px;">Grade: $grade</div>
                            </div>
                        </div>

                        <div class="signatures-row">
                            <div class="sign-box">
                                <div class="sign-line"></div>
                                <div class="sign-title">Controller of Examinations</div>
                                <div class="sign-sub">Academic Council</div>
                            </div>
                            <div class="seal-stamp">OFFICIAL SEAL</div>
                            <div class="sign-box">
                                <div class="sign-line"></div>
                                <div class="sign-title">Prof. Rauf Ahmad</div>
                                <div class="sign-sub">Principal / Executive Head</div>
                            </div>
                        </div>
                    </div>
                    <div class="footer-note">$CONTACT_INFO • Certified Official Document</div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun generateCharacterCertificateHtml(student: StudentEntity): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Character Certificate - ${student.fullName}</title>
                <style>
                    $BASE_CSS
                    .cert-border {
                        border: 6px double #0f2c59;
                        padding: 36px;
                        border-radius: 8px;
                        background: #ffffff;
                    }
                    .cert-title {
                        font-family: 'Playfair Display', serif;
                        font-size: 24px;
                        color: #0f2c59;
                        text-align: center;
                        margin: 16px 0 24px;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                    }
                    .cert-para {
                        font-size: 14px;
                        line-height: 24px;
                        color: #1e293b;
                        text-align: justify;
                        margin-bottom: 16px;
                    }
                </style>
            </head>
            <body>
                <div class="page-container">
                    <div class="content-body" style="padding: 24px;">
                        <div class="cert-border">
                            <div style="text-align: center;">
                                <h1 style="font-size: 20px; font-weight: 800; color: #0f2c59;">$SCHOOL_NAME</h1>
                                <p style="font-size: 12px; color: #b45309; font-weight: 700;">$CAMPUS_NAME</p>
                            </div>
                            <div class="cert-title">Character & Conduct Certificate</div>
                            <p class="cert-para">
                                This is to officially certify that <strong>${student.fullName}</strong>, child of <strong>${student.parentName}</strong>, bearing Registration No. <strong>${student.regNo}</strong>, has been a bona fide student of this institution in <strong>${student.className} (Section ${student.section})</strong>.
                            </p>
                            <p class="cert-para">
                                During their academic tenure at Paradise Little Angels Secondary School Wan Khara, their moral character, disciplinary conduct, and civic behavior were found to be <strong>EXCELLENT and exemplary</strong>. They actively participated in curricular and co-curricular programs.
                            </p>
                            <p class="cert-para" style="color: #0f2c59; font-weight: 600;">
                                We wish them outstanding success in all future academic pursuits.
                            </p>
                            <div class="signatures-row" style="margin-top: 30px;">
                                <div class="sign-box">
                                    <div style="font-size: 11px; font-weight: 600; color: #64748b; margin-bottom: 12px;">Issue Date: 27-Aug-2026</div>
                                    <div class="sign-sub">Serial: PLA-CERT-2026-088</div>
                                </div>
                                <div class="seal-stamp">INSTITUTION SEAL</div>
                                <div class="sign-box">
                                    <div class="sign-line"></div>
                                    <div class="sign-title">Prof. Rauf Ahmad</div>
                                    <div class="sign-sub">Principal / Executive Head</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun generateAdmitCardHtml(student: StudentEntity): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Admit Card - ${student.fullName}</title>
                <style>$BASE_CSS</style>
            </head>
            <body>
                <div class="page-container">
                    <div class="header">
                        <div class="header-brand">
                            <h1>$SCHOOL_NAME</h1>
                            <p>$CAMPUS_NAME</p>
                        </div>
                        <div class="doc-badge">Examination Roll No Slip</div>
                    </div>
                    <div class="content-body">
                        <div class="meta-grid">
                            <div class="meta-item"><div class="meta-label">Candidate Name</div><div class="meta-value">${student.fullName}</div></div>
                            <div class="meta-item"><div class="meta-label">Roll Number</div><div class="meta-value" style="font-size: 16px; color: #0284c7;">#${student.rollNo}</div></div>
                            <div class="meta-item"><div class="meta-label">Registration No</div><div class="meta-value">${student.regNo}</div></div>
                            <div class="meta-item"><div class="meta-label">Examination Center</div><div class="meta-value">Main Academic Hall, Wan Khara</div></div>
                        </div>

                        <table class="doc-table">
                            <thead>
                                <tr>
                                    <th>Subject</th>
                                    <th>Date</th>
                                    <th>Timings</th>
                                    <th>Room</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>Physics / Science</td><td>10-Sep-2026</td><td>08:30 AM - 11:30 AM</td><td>Hall A</td></tr>
                                <tr><td>Mathematics</td><td>12-Sep-2026</td><td>08:30 AM - 11:30 AM</td><td>Hall A</td></tr>
                                <tr><td>English Compulsory</td><td>14-Sep-2026</td><td>08:30 AM - 11:30 AM</td><td>Room 4</td></tr>
                                <tr><td>Urdu Literature</td><td>16-Sep-2026</td><td>08:30 AM - 11:30 AM</td><td>Room 4</td></tr>
                                <tr><td>Computer Science</td><td>18-Sep-2026</td><td>08:30 AM - 11:30 AM</td><td>Lab 1</td></tr>
                            </tbody>
                        </table>

                        <div style="background: #fef2f2; border: 1px solid #fecaca; border-radius: 6px; padding: 10px; font-size: 11px; color: #991b1b; margin-bottom: 20px;">
                            <strong>Examination Instructions:</strong> Candidates must bring this original Slip & School ID. No electronic devices permitted in hall.
                        </div>

                        <div class="signatures-row">
                            <div class="sign-box">
                                <div class="sign-line"></div>
                                <div class="sign-title">Controller of Examinations</div>
                            </div>
                            <div class="seal-stamp">OFFICIAL SEAL</div>
                            <div class="sign-box">
                                <div class="sign-line"></div>
                                <div class="sign-title">Hall Superintendent</div>
                            </div>
                        </div>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    fun generateSalarySlipHtml(staff: StaffEntity): String {
        val basic = staff.monthlySalary * 0.70
        val allowances = staff.monthlySalary * 0.30
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Salary Slip - ${staff.name}</title>
                <style>$BASE_CSS</style>
            </head>
            <body>
                <div class="page-container">
                    <div class="header">
                        <div class="header-brand">
                            <h1>$SCHOOL_NAME</h1>
                            <p>$CAMPUS_NAME</p>
                        </div>
                        <div class="doc-badge">Faculty Salary Slip</div>
                    </div>
                    <div class="content-body">
                        <div class="meta-grid">
                            <div class="meta-item"><div class="meta-label">Staff Member</div><div class="meta-value">${staff.name}</div></div>
                            <div class="meta-item"><div class="meta-label">Employee ID</div><div class="meta-value">${staff.staffId}</div></div>
                            <div class="meta-item"><div class="meta-label">Designation / Role</div><div class="meta-value">${staff.designation} (${staff.role})</div></div>
                            <div class="meta-item"><div class="meta-label">Salary Month</div><div class="meta-value">August 2026</div></div>
                        </div>

                        <table class="doc-table">
                            <thead>
                                <tr>
                                    <th>Pay Component</th>
                                    <th style="text-align: right;">Amount (PKR)</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr><td>Basic Academic Salary</td><td style="text-align: right;">Rs. ${basic.toInt()}</td></tr>
                                <tr><td>Medical & Transport Allowances</td><td style="text-align: right;">Rs. ${allowances.toInt()}</td></tr>
                                <tr><td>Deductions / Provident Fund</td><td style="text-align: right;">Rs. 0</td></tr>
                            </tbody>
                        </table>

                        <div class="total-box">
                            <div><div class="total-label">Net Disbursed Amount</div><div style="font-size: 11px; color: #cbd5e1;">Status: PAID</div></div>
                            <div class="total-amount">Rs. ${staff.monthlySalary.toInt()}</div>
                        </div>

                        <div class="signatures-row">
                            <div class="sign-box"><div class="sign-line"></div><div class="sign-title">Accountant Officer</div></div>
                            <div class="seal-stamp">FINANCE SEAL</div>
                            <div class="sign-box"><div class="sign-line"></div><div class="sign-title">Employee Signature</div></div>
                        </div>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }
}

/**
 * High Resolution Document Header Bar for Dialogs
 */
@Composable
fun DocumentDialogHeader(
    title: String,
    subtitle: String,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SchoolNavyPrimary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = SchoolGoldLight,
                    fontSize = 11.sp
                )
            )
        }
        IconButton(
            onClick = onClose,
            modifier = Modifier.testTag("close_document_dialog_btn")
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}

/**
 * Interactive Zoom & Resolution Selector
 */
@Composable
fun DocumentZoomControl(
    currentZoom: Float,
    onZoomChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SchoolNavyDarkSurface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.HighQuality,
                contentDescription = null,
                tint = SchoolCyanLight,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "HD Resolution Preview",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf(1.0f to "100%", 1.25f to "125%", 1.5f to "150%").forEach { (zoom, label) ->
                val isSelected = (currentZoom == zoom)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isSelected) SchoolCyanAccent else SchoolNavyCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) SchoolCyanLight else SchoolNavyCardBorder
                    ),
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    TextButton(
                        onClick = { onZoomChange(zoom) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) Color.White else TextSecondaryDark,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * HTML / CSS Source Code & Template Viewer with Copy Action
 */
@Composable
fun HtmlCssTemplateViewer(
    htmlContent: String,
    documentName: String
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Clean HTML5 + CSS3 Print Template",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SchoolNavyPrimary,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = "A4 Layout • Vector Print Ready • High DPI",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryLight,
                        fontSize = 11.sp
                    )
                )
            }

            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("$documentName HTML", htmlContent)
                    clipboard.setPrimaryClip(clip)
                    copied = true
                    Toast.makeText(context, "$documentName HTML copied to clipboard!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.testTag("copy_html_btn")
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (copied) "Copied!" else "Copy HTML", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                Text(
                    text = htmlContent,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFE2E8F0),
                        fontSize = 10.sp,
                        lineHeight = 15.sp
                    )
                )
            }
        }
    }
}

/**
 * Ultra-Crisp Sharp Barcode Renderer (Draws clean pixel-aligned black rectangles)
 */
@Composable
fun CrispHighResBarcode(
    code: String,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
    ) {
        val totalWidth = size.width
        val height = size.height
        val barCount = 52
        val slotWidth = totalWidth / barCount

        // Seeded deterministic bar pattern for Code128 emulation
        for (i in 0 until barCount) {
            val isBlack = (i % 2 == 0) || (i % 5 == 0) || (i % 7 == 0)
            if (isBlack) {
                val isThick = (i % 3 == 0)
                val barW = if (isThick) slotWidth * 1.6f else slotWidth * 0.9f
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(i * slotWidth, 0f),
                    size = Size(barW, height)
                )
            }
        }
    }
}

/**
 * High-Contrast Official Stamp & Seal Vector
 */
@Composable
fun OfficialEmbossedSeal(
    modifier: Modifier = Modifier,
    label: String = "OFFICIAL SEAL"
) {
    Surface(
        modifier = modifier.size(68.dp),
        shape = CircleShape,
        color = SchoolGoldLight.copy(alpha = 0.35f),
        border = androidx.compose.foundation.BorderStroke(2.dp, SchoolGoldAccent)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Verified,
                    contentDescription = null,
                    tint = Color(0xFFB45309),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFFB45309),
                        fontWeight = FontWeight.Black,
                        fontSize = 7.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = "WAN KHARA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = SchoolNavyPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 6.sp
                    )
                )
            }
        }
    }
}
