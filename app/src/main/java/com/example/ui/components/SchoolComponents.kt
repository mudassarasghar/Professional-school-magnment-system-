package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.local.entities.FeeVoucherEntity
import com.example.data.local.entities.MarkEntity
import com.example.data.local.entities.StaffEntity
import com.example.data.local.entities.StudentEntity
import com.example.domain.model.UserRole
import com.example.ui.theme.*

@Composable
fun ExecutiveTopBar(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onOpenSearch: () -> Unit,
    onCollectFeeClick: () -> Unit,
    onNewAdmissionClick: () -> Unit,
    onToggleSidebar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = SchoolNavyDark,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Main Branding & Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // School Brand & Crest
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onToggleSidebar() }
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SchoolNavyCard)
                            .border(1.dp, SchoolNavyCardBorder, RoundedCornerShape(10.dp))
                            .padding(3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_crest),
                            contentDescription = "School Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "PARADISE LITTLE ANGELS",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SchoolGoldAccent.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, SchoolGoldAccent)
                            ) {
                                Text(
                                    text = "WAN KHARA",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SchoolGoldLight,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "Paradise Little Angels Secondary School Wan Khara",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondaryDark,
                                fontSize = 9.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Top Quick Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Search trigger button
                    IconButton(
                        onClick = onOpenSearch,
                        modifier = Modifier
                            .size(34.dp)
                            .background(SchoolNavyCard, RoundedCornerShape(8.dp))
                            .border(1.dp, SchoolNavyCardBorder, RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Collect fee button (Green)
                    Button(
                        onClick = onCollectFeeClick,
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolEmeraldGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Collect Fee", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // New Admission button (Cyan)
                    Button(
                        onClick = onNewAdmissionClick,
                        colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("New Admission", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Subbar with Date, Data Safe badge and Portal Role selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Date & Data Safe Chip
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SchoolNavyCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Thu, 27 Aug 2026",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondaryDark,
                                fontSize = 10.sp
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
                            text = "Data Safe",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SchoolEmeraldGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Role Filter Chips
                UserRole.values().forEach { role ->
                    val isSelected = currentRole == role
                    FilterChip(
                        selected = isSelected,
                        onClick = { onRoleSelected(role) },
                        label = {
                            Text(
                                text = role.displayName,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 10.sp
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = when (role) {
                                    UserRole.SUPER_ADMIN -> Icons.Default.Shield
                                    UserRole.TEACHER -> Icons.Default.MenuBook
                                    UserRole.STUDENT -> Icons.Default.Face
                                    UserRole.PARENT -> Icons.Default.Home
                                    UserRole.ACCOUNTANT -> Icons.Default.ReceiptLong
                                    UserRole.BLUEPRINT -> Icons.Default.DeveloperMode
                                },
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = SchoolCyanAccent,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White,
                            containerColor = SchoolNavyCard,
                            labelColor = TextSecondaryDark,
                            iconColor = TextSecondaryDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) SchoolCyanLight else SchoolNavyCardBorder
                        ),
                        modifier = Modifier
                            .height(28.dp)
                            .testTag("role_chip_${role.name.lowercase()}")
                    )
                }
            }
        }
    }
}

// Backward compatible alias
@Composable
fun SchoolHeader(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    ExecutiveTopBar(
        currentRole = currentRole,
        onRoleSelected = onRoleSelected,
        onOpenSearch = {},
        onCollectFeeClick = {},
        onNewAdmissionClick = {},
        onToggleSidebar = {},
        modifier = modifier
    )
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
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
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp
                    )
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.lowercase()) {
        "present", "paid", "active", "passed" -> StatusPresentBg to StatusPresentGreen
        "absent", "overdue", "failed" -> StatusAbsentBg to StatusAbsentRed
        "late", "partial", "pending" -> StatusLateBg to StatusLateYellow
        "excused" -> StatusExcusedBg to StatusExcusedBlue
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Text(
            text = status.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontSize = 9.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

// -------------------------------------------------------------
// 1. SMART STUDENT ID CARD DIALOG (HD Preview & HTML/CSS)
// -------------------------------------------------------------
@Composable
fun StudentIdCardDialog(
    student: StudentEntity,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    val context = LocalContext.current
    val htmlCode = remember(student) { DocumentHtmlGenerator.generateStudentIdCardHtml(student) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DocumentDialogHeader(
                    title = "Student Smart ID Card",
                    subtitle = "${student.fullName} • Reg #${student.regNo}",
                    onClose = onDismiss
                )

                // Tabs: HD Preview vs HTML/CSS Template
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SchoolNavyDarkSurface,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = SchoolCyanLight,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HD Vector Card", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HTML / CSS Template", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    DocumentZoomControl(currentZoom = zoomLevel, onZoomChange = { zoomLevel = it })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // FRONT CARD
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SchoolNavyDarker),
                            border = androidx.compose.foundation.BorderStroke(2.dp, SchoolCyanAccent)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding((16 * zoomLevel).dp)
                            ) {
                                // Header
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.img_school_crest),
                                            contentDescription = "Crest",
                                            modifier = Modifier
                                                .size((42 * zoomLevel).dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "PARADISE LITTLE ANGELS",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Black,
                                                    color = Color.White,
                                                    fontSize = (13 * zoomLevel).sp,
                                                    letterSpacing = 0.5.sp
                                                )
                                            )
                                            Text(
                                                text = "SECONDARY SCHOOL • WAN KHARA",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = SchoolGoldAccent,
                                                    fontSize = (9 * zoomLevel).sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SchoolEmeraldGreen.copy(alpha = 0.25f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, SchoolEmeraldGreen)
                                    ) {
                                        Text(
                                            text = "SMART ID",
                                            color = SchoolEmeraldGreen,
                                            fontSize = (9 * zoomLevel).sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                HorizontalDivider(
                                    color = SchoolNavyCardBorder,
                                    thickness = 1.5.dp,
                                    modifier = Modifier.padding(vertical = 12.dp)
                                )

                                // Student Details & Photo
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size((85 * zoomLevel).dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SchoolNavyCard)
                                            .border(2.dp, SchoolCyanAccent, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                Icons.Default.AccountCircle,
                                                contentDescription = null,
                                                tint = SchoolCyanLight,
                                                modifier = Modifier.size((52 * zoomLevel).dp)
                                            )
                                            Text("PHOTO", fontSize = (9 * zoomLevel).sp, color = Color.White, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = student.fullName,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                fontSize = (16 * zoomLevel).sp
                                            )
                                        )
                                        Text(
                                            text = "Reg No: ${student.regNo}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = SchoolCyanLight,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = (13 * zoomLevel).sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Class: ${student.className} (Sec ${student.section}) • Roll #${student.rollNo}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFFF1F5F9),
                                                fontWeight = FontWeight.Medium,
                                                fontSize = (12 * zoomLevel).sp
                                            )
                                        )
                                        Text(
                                            text = "Guardian: ${student.parentName}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondaryDark,
                                                fontSize = (11 * zoomLevel).sp
                                            )
                                        )
                                        Text(
                                            text = "Emergency Phone: ${student.parentPhone}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = SchoolGoldLight,
                                                fontSize = (11 * zoomLevel).sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Crisp Barcode Box
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CrispHighResBarcode(code = student.regNo)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "* ${student.regNo} * • ACADEMIC YEAR 2026-2027",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color.Black,
                                                fontSize = (10 * zoomLevel).sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Actions row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(onClick = onDismiss) {
                                Text("Close", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Printing Student Smart ID Card for ${student.fullName}...", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolCyanAccent)
                            ) {
                                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Print Card / PDF", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    HtmlCssTemplateViewer(
                        htmlContent = htmlCode,
                        documentName = "Student_ID_${student.regNo}"
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. OFFICIAL FEE VOUCHER / CHALLAN DIALOG (HD Preview & HTML/CSS)
// -------------------------------------------------------------
@Composable
fun FeeVoucherDialog(
    voucher: FeeVoucherEntity,
    onDismiss: () -> Unit,
    onPay: ((Double, String, String) -> Unit)? = null
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    val context = LocalContext.current
    val htmlCode = remember(voucher) { DocumentHtmlGenerator.generateFeeVoucherHtml(voucher) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DocumentDialogHeader(
                    title = "Official Fee Challan Voucher",
                    subtitle = "Voucher #${voucher.voucherNo} • ${voucher.monthYear}",
                    onClose = onDismiss
                )

                // Tab selection
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SchoolNavyDarkSurface,
                    contentColor = Color.White
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HD Challan Preview", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HTML / CSS Template", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    DocumentZoomControl(currentZoom = zoomLevel, onZoomChange = { zoomLevel = it })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // School Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_school_crest),
                                contentDescription = "School Crest",
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Paradise Little Angels Secondary School Wan Khara",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SchoolNavyPrimary,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "Wan Khara Campus • Official Fee Voucher (Student / Bank Copy)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondaryLight,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                            StatusBadge(status = voucher.paymentStatus)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 1.5.dp)

                        // Meta details
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .border(1.dp, SurfaceBorderLight, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Voucher No: ${voucher.voucherNo}", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
                                Text("Month: ${voucher.monthYear}", style = MaterialTheme.typography.bodySmall.copy(color = SchoolNavyPrimary, fontWeight = FontWeight.Bold))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Candidate: ${voucher.studentName} (${voucher.regNo})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Class: ${voucher.className} - Section ${voucher.section}", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = "Issue Date: ${voucher.issueDate} | Due Date: ${voucher.dueDate}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (voucher.paymentStatus == "Overdue") StatusAbsentRed else TextPrimaryLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Account Ledger Breakdown",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        FeeRow(title = "Tuition Fee", amount = voucher.tuitionFee)
                        if (voucher.transportFee > 0) FeeRow(title = "Transport Charges", amount = voucher.transportFee)
                        if (voucher.sportsFee > 0) FeeRow(title = "Sports & Physical Activities", amount = voucher.sportsFee)
                        if (voucher.admissionFee > 0) FeeRow(title = "Admission / Annual Charges", amount = voucher.admissionFee)
                        if (voucher.examFee > 0) FeeRow(title = "Examination / Paper Fund", amount = voucher.examFee)
                        if (voucher.otherCharges > 0) FeeRow(title = "Lab & Digital Portal Utilities", amount = voucher.otherCharges)
                        if (voucher.discount > 0) FeeRow(title = "Scholarship / Merit Concession", amount = -voucher.discount, isDiscount = true)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Payable Amount:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Rs. ${voucher.totalAmount.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = SchoolNavyPrimary))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Amount Received:", style = MaterialTheme.typography.bodyMedium)
                            Text("Rs. ${voucher.amountPaid.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(color = StatusPresentGreen, fontWeight = FontWeight.Bold))
                        }
                        val balance = voucher.totalAmount - voucher.amountPaid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Remaining Balance Due:", style = MaterialTheme.typography.bodyMedium)
                            Text("Rs. ${balance.toInt()}", style = MaterialTheme.typography.bodyMedium.copy(color = if (balance > 0) StatusAbsentRed else StatusPresentGreen, fontWeight = FontWeight.Bold))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Barcode box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CrispHighResBarcode(code = voucher.voucherNo)
                                Text(
                                    text = voucher.voucherNo,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(onClick = onDismiss) {
                                Text("Close", fontWeight = FontWeight.Bold)
                            }
                            Row {
                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Printing Official Fee Voucher #${voucher.voucherNo}...", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                                ) {
                                    Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Print", fontWeight = FontWeight.Bold)
                                }
                                if (onPay != null && voucher.paymentStatus != "Paid") {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            onPay(balance, "Cash / Counter", "RCPT-TXN-${System.currentTimeMillis().toString().takeLast(5)}")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SchoolEmeraldGreen)
                                    ) {
                                        Text("Collect (Rs. ${balance.toInt()})", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    HtmlCssTemplateViewer(
                        htmlContent = htmlCode,
                        documentName = "Fee_Voucher_${voucher.voucherNo}"
                    )
                }
            }
        }
    }
}

@Composable
private fun FeeRow(title: String, amount: Double, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryLight, fontSize = 13.sp))
        Text(
            text = if (isDiscount) "- Rs. ${(-amount).toInt()}" else "Rs. ${amount.toInt()}",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = if (isDiscount) StatusPresentGreen else TextPrimaryLight,
                fontSize = 13.sp
            )
        )
    }
}

// -------------------------------------------------------------
// 3. OFFICIAL DMC / PROGRESS REPORT CARD DIALOG (HD & HTML/CSS)
// -------------------------------------------------------------
@Composable
fun ReportCardDialog(
    student: StudentEntity,
    marks: List<MarkEntity>,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    val context = LocalContext.current
    val htmlCode = remember(student, marks) { DocumentHtmlGenerator.generateReportCardHtml(student, marks) }

    val totalObtained = marks.sumOf { it.marksObtained }
    val totalMax = marks.sumOf { it.totalMarks }.coerceAtLeast(1.0)
    val overallPercentage = (totalObtained / totalMax) * 100.0
    val overallGrade = when {
        overallPercentage >= 90.0 -> "A+"
        overallPercentage >= 80.0 -> "A"
        overallPercentage >= 70.0 -> "B"
        overallPercentage >= 60.0 -> "C"
        overallPercentage >= 50.0 -> "D"
        else -> "F"
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DocumentDialogHeader(
                    title = "Detailed Marks Certificate (DMC)",
                    subtitle = "${student.fullName} • Grade $overallGrade (${"%.1f".format(overallPercentage)}%)",
                    onClose = onDismiss
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SchoolNavyDarkSurface,
                    contentColor = Color.White
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Assessment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HD DMC Sheet", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HTML / CSS Template", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    DocumentZoomControl(currentZoom = zoomLevel, onZoomChange = { zoomLevel = it })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // School Letterhead
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_school_crest),
                                contentDescription = "School Crest",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "PARADISE LITTLE ANGELS SECONDARY SCHOOL WAN KHARA",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = SchoolNavyPrimary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Text(
                                text = "Wan Khara Campus • Detailed Marks Certificate (DMC)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryLight,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Candidate meta
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .border(1.dp, SurfaceBorderLight, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Candidate: ${student.fullName}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Reg No: ${student.regNo}", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Class: ${student.className}-${student.section}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Roll No: ${student.rollNo}", style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Subject Marks & Performance Evaluation",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        if (marks.isEmpty()) {
                            Text(
                                text = "No examination mark records found for this student.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextMutedLight),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        } else {
                            marks.forEach { mark ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(mark.subject, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp))
                                        if (mark.teacherRemarks.isNotBlank()) {
                                            Text(mark.teacherRemarks, style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight, fontSize = 11.sp))
                                        }
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${mark.marksObtained.toInt()} / ${mark.totalMarks.toInt()}",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        StatusBadge(status = mark.grade)
                                    }
                                }
                                HorizontalDivider(color = SurfaceBorderLight.copy(alpha = 0.6f))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Aggregate Box
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SchoolNavyPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Total Score: ${totalObtained.toInt()} / ${totalMax.toInt()}", color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                                    Text("Percentage: ${"%.1f".format(overallPercentage)}%", color = SchoolGoldLight, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SchoolGoldAccent
                                ) {
                                    Text(
                                        text = "Grade $overallGrade",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = SchoolNavyDark
                                        ),
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Official Signatures & Seal
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Controller of Exams", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Verified Official Record", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                            }
                            OfficialEmbossedSeal()
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Prof. Rauf Ahmad", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Principal / Executive Head", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = onDismiss) {
                                Text("Close", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Printing DMC for ${student.fullName}...", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                            ) {
                                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Print DMC / PDF", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    HtmlCssTemplateViewer(
                        htmlContent = htmlCode,
                        documentName = "Report_Card_${student.regNo}"
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. OFFICIAL CHARACTER CERTIFICATE DIALOG (HD & HTML/CSS)
// -------------------------------------------------------------
@Composable
fun CharacterCertificateDialog(
    student: StudentEntity,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    val context = LocalContext.current
    val htmlCode = remember(student) { DocumentHtmlGenerator.generateCharacterCertificateHtml(student) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DocumentDialogHeader(
                    title = "Official Character Certificate",
                    subtitle = "${student.fullName} • Reg #${student.regNo}",
                    onClose = onDismiss
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SchoolNavyDarkSurface,
                    contentColor = Color.White
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HD Certificate Sheet", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HTML / CSS Template", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    DocumentZoomControl(currentZoom = zoomLevel, onZoomChange = { zoomLevel = it })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = androidx.compose.foundation.BorderStroke(3.dp, SchoolGoldAccent)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding((20 * zoomLevel).dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.img_school_crest),
                                        contentDescription = "Crest",
                                        modifier = Modifier.size((50 * zoomLevel).dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "PARADISE LITTLE ANGELS SECONDARY SCHOOL",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            color = SchoolNavyPrimary,
                                            fontSize = (14 * zoomLevel).sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                    Text(
                                        text = "Wan Khara District Campus",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondaryLight,
                                            fontSize = (11 * zoomLevel).sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SchoolNavyPrimary
                                    ) {
                                        Text(
                                            text = "CHARACTER & CONDUCT CERTIFICATE",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = (12 * zoomLevel).sp
                                            ),
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "This is to officially certify that ${student.fullName}, child of ${student.parentName}, bearing Registration No. ${student.regNo}, has been a bona fide student of this institution in ${student.className} (Section ${student.section}).",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = (13 * zoomLevel).sp,
                                        lineHeight = (20 * zoomLevel).sp,
                                        color = TextPrimaryLight
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "During their academic tenure at Paradise Little Angels Secondary School Wan Khara, their moral character, disciplinary conduct, and civic behavior were found to be EXCELLENT and exemplary. They actively participated in academic and co-curricular programs.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = (13 * zoomLevel).sp,
                                        lineHeight = (20 * zoomLevel).sp,
                                        color = TextPrimaryLight
                                    )
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "We wish them outstanding success in all future academic pursuits.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = (13 * zoomLevel).sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SchoolNavyPrimary
                                    )
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Bottom
                                ) {
                                    Column {
                                        Text("Date of Issue: 27-Aug-2026", style = MaterialTheme.typography.labelMedium.copy(fontSize = (11 * zoomLevel).sp, fontWeight = FontWeight.Bold))
                                        Text("Serial: PLA-CERT-2026-088", style = MaterialTheme.typography.labelSmall.copy(fontSize = (10 * zoomLevel).sp, color = TextMutedLight))
                                    }
                                    OfficialEmbossedSeal()
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Prof. Rauf Ahmad", style = MaterialTheme.typography.labelMedium.copy(fontSize = (11 * zoomLevel).sp, fontWeight = FontWeight.Bold))
                                        Text("Principal Signature & Stamp", style = MaterialTheme.typography.labelSmall.copy(fontSize = (10 * zoomLevel).sp, color = TextMutedLight))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = onDismiss) {
                                Text("Close", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Printing Character Certificate for ${student.fullName}...", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                            ) {
                                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Print Certificate / PDF", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    HtmlCssTemplateViewer(
                        htmlContent = htmlCode,
                        documentName = "Character_Certificate_${student.regNo}"
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 5. EXAM ADMIT CARD / ROLL NO SLIP DIALOG (HD & HTML/CSS)
// -------------------------------------------------------------
@Composable
fun ExamAdmitCardDialog(
    student: StudentEntity,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    val context = LocalContext.current
    val htmlCode = remember(student) { DocumentHtmlGenerator.generateAdmitCardHtml(student) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DocumentDialogHeader(
                    title = "Examination Admit Card",
                    subtitle = "${student.fullName} • Roll #${student.rollNo}",
                    onClose = onDismiss
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SchoolNavyDarkSurface,
                    contentColor = Color.White
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Badge, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HD Roll No Slip", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HTML / CSS Template", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    DocumentZoomControl(currentZoom = zoomLevel, onZoomChange = { zoomLevel = it })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_school_crest),
                                contentDescription = null,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Paradise Little Angels Secondary School Wan Khara",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SchoolNavyPrimary,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "Mid-Term Examination 2026 • Official Roll No Slip",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondaryLight,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 1.5.dp)

                        // Candidate card
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .border(1.dp, SurfaceBorderLight, RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Name: ${student.fullName}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text("Reg No: ${student.regNo}", style = MaterialTheme.typography.bodySmall)
                                Text("Class: ${student.className} (${student.section})", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SchoolCyanAccent
                                ) {
                                    Text(
                                        text = "ROLL # ${student.rollNo}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Center: Main Hall Wan Khara", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold))
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Exam Paper Timetable Schedule", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                        Spacer(modifier = Modifier.height(8.dp))

                        ExamScheduleRow("Physics / Science", "10-Sep-2026", "08:30 AM - 11:30 AM", "Hall A")
                        ExamScheduleRow("Mathematics", "12-Sep-2026", "08:30 AM - 11:30 AM", "Hall A")
                        ExamScheduleRow("English Compulsory", "14-Sep-2026", "08:30 AM - 11:30 AM", "Room 4")
                        ExamScheduleRow("Urdu Literature", "16-Sep-2026", "08:30 AM - 11:30 AM", "Room 4")
                        ExamScheduleRow("Computer Science", "18-Sep-2026", "08:30 AM - 11:30 AM", "Lab 1")

                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = StatusAbsentBg),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StatusAbsentRed.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = StatusAbsentRed, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Important: Candidates must bring original Roll No Slip and School ID Card. Electronic devices are strictly prohibited.",
                                    style = MaterialTheme.typography.labelSmall.copy(color = StatusAbsentRed, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = onDismiss) {
                                Text("Close", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Printing Exam Admit Card for ${student.fullName}...", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                            ) {
                                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Print Admit Card", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    HtmlCssTemplateViewer(
                        htmlContent = htmlCode,
                        documentName = "Admit_Card_${student.regNo}"
                    )
                }
            }
        }
    }
}

@Composable
private fun ExamScheduleRow(subject: String, date: String, time: String, room: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(subject, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp))
            Text("$date • $time", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondaryLight, fontSize = 11.sp))
        }
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorderLight)
        ) {
            Text(
                room,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SchoolNavyPrimary),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
        }
    }
    HorizontalDivider(color = SurfaceBorderLight.copy(alpha = 0.6f))
}

// -------------------------------------------------------------
// 6. STAFF SALARY SLIP / PAY VOUCHER DIALOG (HD & HTML/CSS)
// -------------------------------------------------------------
@Composable
fun StaffSalarySlipDialog(
    staff: StaffEntity,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var zoomLevel by remember { mutableFloatStateOf(1.0f) }
    val context = LocalContext.current
    val htmlCode = remember(staff) { DocumentHtmlGenerator.generateSalarySlipHtml(staff) }

    val basic = staff.monthlySalary * 0.70
    val allowances = staff.monthlySalary * 0.30
    val deductions = 0.0
    val netPay = basic + allowances - deductions

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DocumentDialogHeader(
                    title = "Faculty Monthly Salary Slip",
                    subtitle = "${staff.name} • ${staff.staffId}",
                    onClose = onDismiss
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SchoolNavyDarkSurface,
                    contentColor = Color.White
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HD Salary Slip", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HTML / CSS Template", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }

                if (selectedTab == 0) {
                    DocumentZoomControl(currentZoom = zoomLevel, onZoomChange = { zoomLevel = it })

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_school_crest),
                                contentDescription = null,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Paradise Little Angels Secondary School Wan Khara", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 12.sp, color = SchoolNavyPrimary))
                                Text("Staff Monthly Salary Slip • August 2026", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight, fontSize = 11.sp))
                            }
                            StatusBadge(status = "Paid")
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), thickness = 1.5.dp)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .border(1.dp, SurfaceBorderLight, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text("Employee: ${staff.name} (${staff.staffId})", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Designation: ${staff.designation} (${staff.role})", style = MaterialTheme.typography.bodySmall.copy(color = SchoolNavyPrimary, fontWeight = FontWeight.SemiBold))
                            Text("Assigned: ${staff.assignedClass} | Sub: ${staff.assignedSubjects}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryLight))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Earnings & Deductions Summary", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp))
                        Spacer(modifier = Modifier.height(8.dp))

                        FeeRow("Basic Academic Pay", basic)
                        FeeRow("Medical & Transport Allowance", allowances)
                        FeeRow("Tax / Provident Fund Deductions", deductions)

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Net Disbursed Pay:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Rs. ${netPay.toInt()}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = StatusPresentGreen))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Accounts Dept Sign", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                Text("Verified for Disbursement", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                            }
                            OfficialEmbossedSeal(label = "FINANCE SEAL")
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Employee Acknowledgement", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                Text("Direct Bank Transfer", style = MaterialTheme.typography.labelSmall.copy(color = TextMutedLight))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(onClick = onDismiss) {
                                Text("Close", fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Printing Salary Slip for ${staff.name}...", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyPrimary)
                            ) {
                                Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Print Salary Slip", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    HtmlCssTemplateViewer(
                        htmlContent = htmlCode,
                        documentName = "Salary_Slip_${staff.staffId}"
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// UNIVERSAL SEARCH & QUICK LOOKUP DIALOG
// -------------------------------------------------------------
@Composable
fun UniversalSearchDialog(
    students: List<StudentEntity>,
    staff: List<StaffEntity>,
    vouchers: List<FeeVoucherEntity>,
    onDismiss: () -> Unit,
    onStudentClick: (StudentEntity) -> Unit,
    onStaffClick: (StaffEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredStudents = remember(searchQuery, students) {
        if (searchQuery.isBlank()) students.take(5)
        else students.filter {
            it.fullName.contains(searchQuery, ignoreCase = true) ||
            it.regNo.contains(searchQuery, ignoreCase = true) ||
            it.rollNo.toString() == searchQuery.trim() ||
            it.parentPhone.contains(searchQuery)
        }
    }

    val filteredStaff = remember(searchQuery, staff) {
        if (searchQuery.isBlank()) staff.take(4)
        else staff.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.staffId.contains(searchQuery, ignoreCase = true) ||
            it.designation.contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SchoolNavyDarker),
            border = androidx.compose.foundation.BorderStroke(1.dp, SchoolNavyCardBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search Input Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search students, roll no, teachers, phone...", color = TextMutedLight, fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SchoolCyanLight) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = null, tint = TextMutedLight)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("universal_search_input"),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SchoolNavyCard,
                        unfocusedContainerColor = SchoolNavyCard,
                        focusedBorderColor = SchoolCyanAccent,
                        unfocusedBorderColor = SchoolNavyCardBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("Students & Candidates (${filteredStudents.size})", color = SchoolCyanLight, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    filteredStudents.forEach { student ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onStudentClick(student)
                                    onDismiss()
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(SchoolCyanAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(student.rollNo.toString(), color = SchoolCyanLight, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(student.fullName, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text("${student.className} • Reg: ${student.regNo}", color = TextSecondaryDark, fontSize = 10.sp)
                            }
                            StatusBadge(status = student.status)
                        }
                        HorizontalDivider(color = SchoolNavyCardBorder.copy(alpha = 0.5f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Faculty & Staff (${filteredStaff.size})", color = SchoolGoldAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    filteredStaff.forEach { member ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onStaffClick(member)
                                    onDismiss()
                                }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(SchoolGoldAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.School, contentDescription = null, tint = SchoolGoldAccent, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(member.name, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                                Text("${member.designation} • ${member.staffId}", color = TextSecondaryDark, fontSize = 10.sp)
                            }
                            Text("Rs. ${member.monthlySalary.toInt()}", color = StatusPresentGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(color = SchoolNavyCardBorder.copy(alpha = 0.5f))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(containerColor = SchoolNavyCard)
                ) {
                    Text("Close Search", color = Color.White, fontSize = 11.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER CANVAS GRAPHIC: Ultra-Crisp High-Res Barcode
// -------------------------------------------------------------
@Composable
fun BarcodeSimulatedGraphic(text: String, modifier: Modifier = Modifier) {
    CrispHighResBarcode(code = text, modifier = modifier)
}
