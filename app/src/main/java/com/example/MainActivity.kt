package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.database.SchoolDatabase
import com.example.data.repository.SchoolRepository
import com.example.domain.model.UserRole
import com.example.ui.components.ExecutiveTopBar
import com.example.ui.components.UniversalSearchDialog
import com.example.ui.screens.AccountantModule
import com.example.ui.screens.AdmissionFormDialog
import com.example.ui.screens.ArchitectureAndSchemaScreen
import com.example.ui.screens.ParentPortal
import com.example.ui.screens.QuickFeeCollectionDialog
import com.example.ui.screens.StudentPortal
import com.example.ui.screens.SuperAdminDashboard
import com.example.ui.screens.TeacherDashboard
import com.example.ui.theme.ParadiseSchoolTheme
import com.example.ui.viewmodel.SchoolViewModel
import com.example.ui.viewmodel.SchoolViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = SchoolDatabase.getDatabase(applicationContext)
        val repository = SchoolRepository(database.schoolDao())
        val viewModelFactory = SchoolViewModelFactory(repository)

        setContent {
            ParadiseSchoolTheme {
                val viewModel: SchoolViewModel = viewModel(factory = viewModelFactory)
                SchoolAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SchoolAppContent(viewModel: SchoolViewModel) {
    val currentRole by viewModel.currentRole.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val students by viewModel.students.collectAsState()
    val staff by viewModel.staff.collectAsState()
    val feeVouchers by viewModel.feeVouchers.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showUniversalSearch by remember { mutableStateOf(false) }
    var showTopAdmission by remember { mutableStateOf(false) }
    var showTopFeeCollect by remember { mutableStateOf(false) }

    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(
                message = msg,
                duration = SnackbarDuration.Short
            )
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            ExecutiveTopBar(
                currentRole = currentRole,
                onRoleSelected = { viewModel.setRole(it) },
                onOpenSearch = { showUniversalSearch = true },
                onCollectFeeClick = { showTopFeeCollect = true },
                onNewAdmissionClick = { showTopAdmission = true },
                onToggleSidebar = { }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .imePadding()
        ) {
            when (currentRole) {
                UserRole.SUPER_ADMIN -> SuperAdminDashboard(viewModel = viewModel)
                UserRole.TEACHER -> TeacherDashboard(viewModel = viewModel)
                UserRole.STUDENT -> StudentPortal(viewModel = viewModel)
                UserRole.PARENT -> ParentPortal(viewModel = viewModel)
                UserRole.ACCOUNTANT -> AccountantModule(viewModel = viewModel)
                UserRole.BLUEPRINT -> ArchitectureAndSchemaScreen()
            }
        }
    }

    // Universal Search Modal
    if (showUniversalSearch) {
        UniversalSearchDialog(
            students = students,
            staff = staff,
            vouchers = feeVouchers,
            onDismiss = { showUniversalSearch = false },
            onStudentClick = { student ->
                viewModel.setRole(UserRole.STUDENT)
                showUniversalSearch = false
            },
            onStaffClick = { member ->
                viewModel.setRole(UserRole.TEACHER)
                showUniversalSearch = false
            }
        )
    }

    // Top New Admission Modal
    if (showTopAdmission) {
        AdmissionFormDialog(
            onDismiss = { showTopAdmission = false },
            onSubmit = { name, gender, dob, gradeLevel, className, sec, roll, pName, pPhone, pEmail, emerg, med, addr ->
                viewModel.registerStudent(name, gender, dob, gradeLevel, className, sec, roll, pName, pPhone, pEmail, emerg, med, addr)
                showTopAdmission = false
            }
        )
    }

    // Top Fee Collect Modal
    if (showTopFeeCollect) {
        QuickFeeCollectionDialog(
            students = students,
            vouchers = feeVouchers,
            onDismiss = { showTopFeeCollect = false },
            onCollect = { voucher, amount, method, ref ->
                viewModel.payFeeVoucher(voucher, amount, method, ref)
                showTopFeeCollect = false
            }
        )
    }
}
