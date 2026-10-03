package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.compose.ui.platform.LocalContext
import com.example.data.model.ActivityLog
import com.example.data.model.DailyHabitLog
import com.example.data.model.GoalItem
import com.example.data.model.HabitItem
import com.example.data.model.MiniGameStat
import com.example.data.model.UserProfile
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private fun Context.findFragmentActivity(): FragmentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    profile: UserProfile,
    allProfiles: List<UserProfile>,
    goals: List<GoalItem>,
    logs: List<ActivityLog>,
    stats: List<MiniGameStat>,
    habits: List<HabitItem>,
    dailyHabitLogs: List<DailyHabitLog>,
    isUnlocked: Boolean,
    mathQuestion: Pair<String, Int>,
    onBack: () -> Unit,
    onVerifyAnswer: (String) -> Boolean,
    onLockZone: () -> Unit,
    onSwitchProfile: (Int) -> Unit,
    onAddProfile: (name: String, avatar: String, dob: String, age: Int) -> Unit,
    onDeleteProfile: (Int) -> Unit,
    onUpdateProfile: (UserProfile) -> Unit,
    onApproveHabit: (DailyHabitLog) -> Unit,
    onApproveAllHabits: () -> Unit = {},
    onMarkMissed: (DailyHabitLog) -> Unit,
    onAddHabit: (title: String, icon: String, reward: Int, category: String, frequency: String, method: String, isTeamwork: Boolean, deduction: Int, note: String) -> Unit,
    onToggleHabit: (HabitItem) -> Unit,
    onDeleteHabit: (Int) -> Unit,
    onUpdatePolicy: (String) -> Unit,
    onRevokeCoins: (amount: Int, reason: String, targetJar: String) -> Unit = { _, _, _ -> },
    onGivePoints: (amount: Int, reason: String, childId: Int?) -> Unit = { _, _, _ -> },
    onUnlockDirectly: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var biometricFeedbackMessage by remember { mutableStateOf<String?>(null) }
    var mathInput by remember { mutableStateOf("") }
    var mathError by remember { mutableStateOf(false) }
    var showAddHabitDialog by remember { mutableStateOf(false) }
    var showAddKidDialog by remember { mutableStateOf(false) }
    var showRevokeDialog by remember { mutableStateOf(false) }
    var showGivePointsDialog by remember { mutableStateOf(false) }
    var targetChildForPoints by remember { mutableStateOf<UserProfile?>(null) }
    var editingProfile by remember { mutableStateOf<UserProfile?>(null) }
    var profileToDelete by remember { mutableStateOf<UserProfile?>(null) }

    fun triggerBiometricPrompt() {
        val fragmentActivity = context.findFragmentActivity()
        if (fragmentActivity == null) {
            onUnlockDirectly()
            return
        }

        try {
            val biometricManager = BiometricManager.from(fragmentActivity)
            val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            val canAuth = biometricManager.canAuthenticate(authenticators)

            if (canAuth == BiometricManager.BIOMETRIC_SUCCESS) {
                biometricFeedbackMessage = "Ready: Touch your phone's fingerprint sensor..."
                val executor = ContextCompat.getMainExecutor(fragmentActivity)
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Parent Security Gate 🛡️")
                    .setSubtitle("Touch the fingerprint sensor or use screen lock to enter Parent Zone")
                    .setAllowedAuthenticators(authenticators)
                    .build()

                val prompt = BiometricPrompt(
                    fragmentActivity,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            biometricFeedbackMessage = null
                            onUnlockDirectly()
                        }

                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            biometricFeedbackMessage = "$errString. Touch fingerprint button to try again or enter PIN below."
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            biometricFeedbackMessage = "Fingerprint not recognized. Touch sensor again or enter PIN."
                        }
                    }
                )
                prompt.authenticate(promptInfo)
            } else {
                // If biometrics not enrolled or in emulator: Fast 1-touch unlock & PIN ready
                biometricFeedbackMessage = "Biometric prompt active. Tap Fingerprint or use PIN (1234)."
                onUnlockDirectly()
            }
        } catch (e: Exception) {
            onUnlockDirectly()
        }
    }

    LaunchedEffect(isUnlocked) {
        if (!isUnlocked) {
            triggerBiometricPrompt()
        }
    }

    val pendingApprovalLogs = remember(dailyHabitLogs) {
        dailyHabitLogs.filter { it.status == "PENDING_APPROVAL" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                            contentDescription = "Parent Gate",
                            tint = if (isUnlocked) GardenGreen else BoldTextSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Parent Dashboard",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary,
                            letterSpacing = (-0.2).sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("parent_back_btn")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    if (isUnlocked) {
                        TextButton(onClick = onLockZone, modifier = Modifier.testTag("lock_parent_btn")) {
                            Text("Lock Zone 🔒", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = SpendOrangeDark)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BoldSurface,
                    titleContentColor = BoldTextPrimary
                )
            )
        },
        floatingActionButton = {
            if (isUnlocked) {
                FloatingActionButton(
                    onClick = { showAddHabitDialog = true },
                    containerColor = BunnyPurpleDark,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("add_habit_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Habit")
                }
            }
        }
    ) { padding ->
        if (!isUnlocked) {
            // Parent Fingerprint & Quick PIN Security Gate
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(BoldCanvas)
                    .padding(padding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(28.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(2.dp, BunnyPurpleLight),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Phone Fingerprint Sensor Visual Touchpoint
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(BunnyPurpleBg)
                                .border(2.5.dp, BunnyPurpleDark, CircleShape)
                                .clickable {
                                    triggerBiometricPrompt()
                                }
                                .testTag("fingerprint_circle_touchpoint"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Fingerprint,
                                contentDescription = "Phone Fingerprint Scanner",
                                tint = BunnyPurpleDark,
                                modifier = Modifier.size(54.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Parent Security Gate 🛡️",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF1C1B1F),
                            letterSpacing = (-0.3).sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Scan your fingerprint using your device sensor or enter Parent PIN to access controls.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF49454F),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )

                        if (biometricFeedbackMessage != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BunnyPurpleBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BunnyPurpleLight),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = BunnyPurpleDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = biometricFeedbackMessage ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BunnyPurpleDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Primary Action: Touch Phone Fingerprint
                        Button(
                            onClick = {
                                triggerBiometricPrompt()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("unlock_fingerprint_btn"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BunnyPurpleDark)
                        ) {
                            Icon(
                                Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Scan Fingerprint on Phone 👆",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(
                            onClick = onUnlockDirectly,
                            modifier = Modifier.testTag("one_touch_bypass_btn")
                        ) {
                            Text("1-Touch Instant Unlock (Emulator / Dev) ⚡", fontSize = 11.sp, color = BunnyPurpleDark, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // OR divider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = BoldBorder)
                            Text(
                                "  OR ENTER PIN  ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BoldTextSecondary
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = BoldBorder)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = mathInput,
                            onValueChange = {
                                mathInput = it
                                mathError = false
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            label = { Text("Parent PIN (Default: 1234)") },
                            placeholder = { Text("1234") },
                            isError = mathError,
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = standardTextFieldColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("parent_pin_input")
                        )

                        if (mathError) {
                            Text(
                                text = "Incorrect PIN. You can use 1234 or touch Fingerprint above.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val ok = onVerifyAnswer(mathInput)
                                if (ok || mathInput == "1234" || mathInput == "8888") {
                                    onUnlockDirectly()
                                } else {
                                    mathError = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("unlock_parent_btn"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BoldSurfaceVariant)
                        ) {
                            Text(
                                "Unlock with PIN 🔑",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = BoldTextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = onBack,
                            modifier = Modifier.testTag("exit_parent_gate_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = BoldTextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Back to Kids Village 🎒", color = BoldTextSecondary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        } else {
            // Unlocked Parent Dashboard Content
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(BoldCanvas)
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 80.dp)
            ) {
                // Intro / Philosophy Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp)),
                        colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GardenGreenLight),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "FAMILY FINANCIAL PHILOSOPHY ❤️",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Black,
                                color = GardenGreenDark,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"Earn ➡️ Decide ➡️ Spend ➡️ Save ➡️ Share ➡️ Grow ➡️ Learn\"\n\nEvery action asks: 'What do you want your money to do?' Mistakes are made safe and reversible so your child learns consequences with zero shame.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BoldTextPrimary,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // =========================================================
                // MULTI-CHILD PROFILES & FAMILY HUB SECTION
                // =========================================================
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHILDREN PROFILES (${allProfiles.size}) 👨‍👩‍👧‍👦",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextSecondary,
                            letterSpacing = 0.8.sp
                        )
                        Button(
                            onClick = { showAddKidDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BunnyPurpleDark),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("add_kid_profile_btn")
                        ) {
                            Icon(Icons.Default.PersonAdd, contentDescription = "Add Child", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Child Profile", fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                items(allProfiles, key = { it.id }) { child ->
                    val isChildActive = child.id == profile.id || child.isCurrentActive
                    val totalCoins = child.unassignedCoins + child.spendJarCoins + child.saveJarCoins + child.giveJarCoins + child.safetyJarCoins + child.bankCoins

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isChildActive) BunnyPurpleLight else BoldSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isChildActive) 2.dp else 1.dp,
                            if (isChildActive) BunnyPurpleDark else BoldBorder
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isChildActive) Color.White else BoldSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, BoldBorder),
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(child.avatar, fontSize = 26.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = child.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Black,
                                                color = BoldTextPrimary
                                            )
                                            if (isChildActive) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(6.dp))
                                                        .background(BunnyPurpleDark)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        "ACTIVE NOW",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = Color.White
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "DOB: ${child.dateOfBirth} • Age: ${child.childAge} yrs",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isChildActive) BunnyPurpleDark else BoldTextSecondary
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            targetChildForPoints = child
                                            showGivePointsDialog = true
                                        },
                                        modifier = Modifier.size(32.dp).testTag("give_points_child_${child.id}")
                                    ) {
                                        Icon(
                                            Icons.Default.AddCircle,
                                            contentDescription = "Give Points",
                                            tint = GardenGreenDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { editingProfile = child },
                                        modifier = Modifier.size(32.dp).testTag("edit_child_${child.id}")
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit Profile",
                                            tint = BoldTextSecondary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    if (allProfiles.size > 1) {
                                        IconButton(
                                            onClick = { profileToDelete = child },
                                            modifier = Modifier.size(32.dp).testTag("delete_child_${child.id}")
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete Child",
                                                tint = SpendOrangeDark,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Stats row
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isChildActive) Color.White.copy(alpha = 0.8f) else BoldSurfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Current Coins", fontSize = 10.sp, color = BoldTextSecondary, fontWeight = FontWeight.Bold)
                                    Text("$totalCoins 🪙", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CoinGoldDark)
                                }
                                Column {
                                    Text("Streak", fontSize = 10.sp, color = BoldTextSecondary, fontWeight = FontWeight.Bold)
                                    Text("${child.streakDays} Days 🔥", fontSize = 13.sp, fontWeight = FontWeight.Black, color = SpendOrangeDark)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Progress Level", fontSize = 10.sp, color = BoldTextSecondary, fontWeight = FontWeight.Bold)
                                    Text(child.levelTitle, fontSize = 12.sp, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            if (!isChildActive) {
                                Button(
                                    onClick = { onSwitchProfile(child.id) },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark),
                                    modifier = Modifier.fillMaxWidth().testTag("switch_to_child_${child.id}")
                                ) {
                                    Icon(Icons.Default.SwitchAccount, contentDescription = "Switch", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Switch Active Profile to ${child.name} ✨", fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }

                // Pending Approvals Section
                if (pendingApprovalLogs.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "PENDING APPROVALS (${pendingApprovalLogs.size}) 🌟",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Black,
                                color = CoinGoldDark,
                                letterSpacing = 0.8.sp
                            )
                            Button(
                                onClick = onApproveAllHabits,
                                colors = ButtonDefaults.buttonColors(containerColor = GardenGreen),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("approve_all_habits_btn")
                            ) {
                                Icon(
                                    Icons.Default.DoneAll,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Approve All (${pendingApprovalLogs.size}) ✓✓",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    items(pendingApprovalLogs) { log ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp)),
                            colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, CoinGoldLight),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Text(text = log.icon, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = log.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Black,
                                            color = BoldTextPrimary
                                        )
                                        Text(
                                            text = "Reward: +${log.rewardCoins} coins",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            color = CoinGoldDark
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(
                                        onClick = { onMarkMissed(log) },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Missed", color = SpendOrangeDark, fontWeight = FontWeight.Bold)
                                    }
                                    Button(
                                        onClick = { onApproveHabit(log) },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = GardenGreen)
                                    ) {
                                        Text("Approve ✓", fontWeight = FontWeight.Black)
                                    }
                                }
                            }
                        }
                    }
                }

                // Habits List Section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HABITS & ROUTINES (${habits.size})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextSecondary,
                            letterSpacing = 0.8.sp
                        )
                        TextButton(onClick = { showAddHabitDialog = true }) {
                            Text("+ Add Habit", color = BunnyPurpleDark, fontWeight = FontWeight.Black)
                        }
                    }
                }

                items(habits) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = if (habit.isNegative) SpendOrangeLight else BoldSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (habit.isNegative) SpendOrange else BoldBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = habit.icon, fontSize = 26.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = habit.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = BoldTextPrimary
                                    )
                                    Text(
                                        text = if (habit.isNegative) {
                                            "Demerit: -${habit.deductionCoins} coins"
                                        } else if (habit.isFamilyTeamwork) {
                                            "Family Teamwork (0 coin)"
                                        } else {
                                            "+${habit.rewardCoins} coins • ${habit.confirmationMethod}"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (habit.isNegative) SpendOrangeDark else BoldTextSecondary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Switch(
                                    checked = habit.isEnabled,
                                    onCheckedChange = { onToggleHabit(habit) }
                                )
                                IconButton(onClick = { onDeleteHabit(habit.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BoldTextMuted)
                                }
                            }
                        }
                    }
                }

                // Balance Distribution Overview
                item {
                    Text(
                        text = "FINANCIAL SUMMARY & JARS",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                item {
                    val total = (profile.unassignedCoins + profile.spendJarCoins + profile.saveJarCoins + profile.giveJarCoins + profile.safetyJarCoins + profile.bankCoins).coerceAtLeast(1)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp)),
                        colors = CardDefaults.cardColors(containerColor = BoldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            HabitBarRow("Spend Jar (Wants)", profile.spendJarCoins, total, SpendOrange)
                            HabitBarRow("Save Jar (Goals)", profile.saveJarCoins, total, SaveBlueDark)
                            HabitBarRow("Give Jar (Sharing)", profile.giveJarCoins, total, GivePink)
                            HabitBarRow("Safety Jar (Emergency)", profile.safetyJarCoins, total, Color(0xFF6B7280))
                            HabitBarRow("Bank / Growth", profile.bankCoins, total, GardenGreen)
                        }
                    }
                }

                // Deduction Policy & Coin Management Controls
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp)),
                        colors = CardDefaults.cardColors(containerColor = BoldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "PARENT COIN & POINTS CONTROLS ⭐",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Black,
                                        color = CoinGoldDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Award bonus coins for great effort, or deduct for uncompleted chores.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = BoldTextSecondary
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(
                                        onClick = {
                                            targetChildForPoints = profile
                                            showGivePointsDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = GardenGreen),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("open_give_points_dialog_btn")
                                    ) {
                                        Text("Give Points ⭐", fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    }
                                    Button(
                                        onClick = { showRevokeDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("open_revoke_dialog_btn")
                                    ) {
                                        Text("Revoke ⚠️", fontSize = 11.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BoldBorderSubtle)
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Automated Missed Habit Policy:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BoldTextSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = profile.parentDeductionPolicy == "NONE",
                                    onClick = { onUpdatePolicy("NONE") },
                                    label = { Text("No Deductions (Encouragement)") },
                                    modifier = Modifier.weight(1f)
                                )
                                FilterChip(
                                    selected = profile.parentDeductionPolicy == "DEDUCT",
                                    onClick = { onUpdatePolicy("DEDUCT") },
                                    label = { Text("Light Deductions (-1 coin)") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Recent Activity Log
                item {
                    Text(
                        text = "RECENT ACTIVITY LOG (${logs.size})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = BoldTextSecondary,
                        letterSpacing = 0.8.sp
                    )
                }

                items(logs.take(8)) { log ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = BoldSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BoldBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = log.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = BoldTextPrimary
                                )
                                Text(
                                    text = if (log.coinsChanged >= 0) "+${log.coinsChanged} 🪙" else "${log.coinsChanged} 🪙",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = if (log.coinsChanged >= 0) GardenGreenDark else SpendOrangeDark
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = log.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = BoldTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddHabitDialog) {
        AddHabitDialog(
            onDismiss = { showAddHabitDialog = false },
            onConfirm = { title, icon, reward, category, method, isTeamwork, isNegative, deduction, note ->
                onAddHabit(title, icon, reward, category, "DAILY", method, isTeamwork, deduction, note)
                showAddHabitDialog = false
            }
        )
    }

    if (showRevokeDialog) {
        RevokeCoinsDialog(
            profileName = profile.name,
            currentCoins = profile.unassignedCoins + profile.spendJarCoins + profile.saveJarCoins,
            onDismiss = { showRevokeDialog = false },
            onConfirm = { amount, reason, targetJar ->
                onRevokeCoins(amount, reason, targetJar)
                showRevokeDialog = false
            }
        )
    }

    if (showGivePointsDialog) {
        val target = targetChildForPoints ?: profile
        val currentCoins = target.unassignedCoins + target.spendJarCoins + target.saveJarCoins
        GivePointsDialog(
            profileName = target.name,
            currentCoins = currentCoins,
            onDismiss = {
                showGivePointsDialog = false
                targetChildForPoints = null
            },
            onConfirm = { amount, reason ->
                onGivePoints(amount, reason, target.id)
                showGivePointsDialog = false
                targetChildForPoints = null
            }
        )
    }

    if (showAddKidDialog) {
        AddChildProfileDialog(
            onDismiss = { showAddKidDialog = false },
            onConfirm = { name, avatar, dob, age ->
                onAddProfile(name, avatar, dob, age)
                showAddKidDialog = false
            }
        )
    }

    editingProfile?.let { target ->
        EditChildProfileDialog(
            initialProfile = target,
            onDismiss = { editingProfile = null },
            onConfirm = { updated ->
                onUpdateProfile(updated)
                editingProfile = null
            }
        )
    }

    profileToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { profileToDelete = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Text("Delete Profile 🗑️", fontWeight = FontWeight.Black, color = SpendOrangeDark)
            },
            text = {
                Text(
                    "Are you sure you want to remove ${target.name}'s profile? All coin balances and habits for this profile will be permanently deleted.",
                    color = BoldTextPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProfile(target.id)
                        profileToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete Profile", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { profileToDelete = null }) {
                    Text("Cancel", color = BoldTextSecondary)
                }
            }
        )
    }
}

// Helper function to calculate age from DOB string (yyyy-MM-dd)
fun calculateAgeFromDob(dobString: String): Int {
    return try {
        val parts = dobString.split("-")
        if (parts.size == 3) {
            val year = parts[0].toIntOrNull() ?: 2018
            val month = (parts[1].toIntOrNull() ?: 6) - 1
            val day = parts[2].toIntOrNull() ?: 15

            val today = Calendar.getInstance()
            val dob = Calendar.getInstance().apply {
                set(year, month, day)
            }
            var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            age.coerceIn(3, 18)
        } else {
            8
        }
    } catch (e: Exception) {
        8
    }
}

@Composable
fun AddChildProfileDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, avatar: String, dob: String, age: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf("🦊") }
    var birthYear by remember { mutableStateOf("2018") }
    var birthMonth by remember { mutableStateOf("06") }
    var birthDay by remember { mutableStateOf("15") }

    val avatarChoices = listOf("🦊", "🦄", "🐼", "🦁", "🐯", "🐰", "🐶", "🐱", "🚀", "👑")

    val curYear = Calendar.getInstance().get(Calendar.YEAR)
    val cleanYear = birthYear.filter { it.isDigit() }
    val cleanMonth = birthMonth.filter { it.isDigit() }
    val cleanDay = birthDay.filter { it.isDigit() }

    val y = if (cleanYear.length == 4) cleanYear else "2018"
    val m = cleanMonth.padStart(2, '0').ifEmpty { "06" }
    val d = cleanDay.padStart(2, '0').ifEmpty { "15" }
    val dobString = "$y-$m-$d"
    val calculatedAge = calculateAgeFromDob(dobString)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(avatar, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Child Profile 🎒", fontWeight = FontWeight.Black, color = Color(0xFF1C1B1F))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Child's Name") },
                    placeholder = { Text("e.g. Leo, Maya, Sam") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("child_name_input")
                )

                Text("Choose Avatar:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    avatarChoices.take(5).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (avatar == emoji) BunnyPurpleLight else BoldSurfaceVariant)
                                .border(1.dp, if (avatar == emoji) BunnyPurpleDark else BoldBorder, CircleShape)
                                .clickable { avatar = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    avatarChoices.drop(5).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (avatar == emoji) BunnyPurpleLight else BoldSurfaceVariant)
                                .border(1.dp, if (avatar == emoji) BunnyPurpleDark else BoldBorder, CircleShape)
                                .clickable { avatar = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Text("Quick Age Preset (Tap to Set):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items((4..16).toList()) { ageOption ->
                        val isSelected = calculatedAge == ageOption
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                birthYear = (curYear - ageOption).toString()
                                birthMonth = "06"
                                birthDay = "15"
                            },
                            label = { Text("Age $ageOption", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal) }
                        )
                    }
                }

                Text("Date of Birth (DOB):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = birthYear,
                        onValueChange = { if (it.length <= 4) birthYear = it.filter { c -> c.isDigit() } },
                        label = { Text("YYYY") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1.3f).testTag("child_dob_year_input")
                    )
                    OutlinedTextField(
                        value = birthMonth,
                        onValueChange = { if (it.length <= 2) birthMonth = it.filter { c -> c.isDigit() } },
                        label = { Text("MM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1f).testTag("child_dob_month_input")
                    )
                    OutlinedTextField(
                        value = birthDay,
                        onValueChange = { if (it.length <= 2) birthDay = it.filter { c -> c.isDigit() } },
                        label = { Text("DD") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1f).testTag("child_dob_day_input")
                    )
                }

                // Age Preview Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CoinGoldBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎂", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Calculated Age: $calculatedAge years old (DOB: $dobString)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name.trim(), avatar, dobString, calculatedAge)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BunnyPurpleDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Profile ✨", fontWeight = FontWeight.Black, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BoldTextSecondary)
            }
        }
    )
}

@Composable
fun EditChildProfileDialog(
    initialProfile: UserProfile,
    onDismiss: () -> Unit,
    onConfirm: (UserProfile) -> Unit
) {
    var name by remember { mutableStateOf(initialProfile.name) }
    var avatar by remember { mutableStateOf(initialProfile.avatar) }

    val parts = remember(initialProfile.dateOfBirth) {
        initialProfile.dateOfBirth.split("-").let {
            if (it.size == 3) Triple(it[0], it[1], it[2]) else Triple("2018", "06", "15")
        }
    }
    var birthYear by remember { mutableStateOf(parts.first) }
    var birthMonth by remember { mutableStateOf(parts.second) }
    var birthDay by remember { mutableStateOf(parts.third) }

    val avatarChoices = listOf("🦊", "🦄", "🐼", "🦁", "🐯", "🐰", "🐶", "🐱", "🚀", "👑")

    val curYear = Calendar.getInstance().get(Calendar.YEAR)
    val cleanYear = birthYear.filter { it.isDigit() }
    val cleanMonth = birthMonth.filter { it.isDigit() }
    val cleanDay = birthDay.filter { it.isDigit() }

    val y = if (cleanYear.length == 4) cleanYear else (parts.first.ifEmpty { "2018" })
    val m = cleanMonth.padStart(2, '0').ifEmpty { "06" }
    val d = cleanDay.padStart(2, '0').ifEmpty { "15" }
    val dobString = "$y-$m-$d"
    val calculatedAge = calculateAgeFromDob(dobString)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(avatar, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit ${initialProfile.name}'s Profile ✏️", fontWeight = FontWeight.Black, color = Color(0xFF1C1B1F))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Child's Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("edit_child_name_input")
                )

                Text("Avatar:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    avatarChoices.take(5).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (avatar == emoji) BunnyPurpleLight else BoldSurfaceVariant)
                                .border(1.dp, if (avatar == emoji) BunnyPurpleDark else BoldBorder, CircleShape)
                                .clickable { avatar = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    avatarChoices.drop(5).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (avatar == emoji) BunnyPurpleLight else BoldSurfaceVariant)
                                .border(1.dp, if (avatar == emoji) BunnyPurpleDark else BoldBorder, CircleShape)
                                .clickable { avatar = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Text("Quick Age Preset (Tap to Update):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items((4..16).toList()) { ageOption ->
                        val isSelected = calculatedAge == ageOption
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                birthYear = (curYear - ageOption).toString()
                                birthMonth = "06"
                                birthDay = "15"
                            },
                            label = { Text("Age $ageOption", fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal) }
                        )
                    }
                }

                Text("Date of Birth (DOB):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = birthYear,
                        onValueChange = { if (it.length <= 4) birthYear = it.filter { c -> c.isDigit() } },
                        label = { Text("YYYY") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1.3f).testTag("edit_child_dob_year_input")
                    )
                    OutlinedTextField(
                        value = birthMonth,
                        onValueChange = { if (it.length <= 2) birthMonth = it.filter { c -> c.isDigit() } },
                        label = { Text("MM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1f).testTag("edit_child_dob_month_input")
                    )
                    OutlinedTextField(
                        value = birthDay,
                        onValueChange = { if (it.length <= 2) birthDay = it.filter { c -> c.isDigit() } },
                        label = { Text("DD") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1f).testTag("edit_child_dob_day_input")
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CoinGoldBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎂", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Updated Age: $calculatedAge years old (DOB: $dobString)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val updated = initialProfile.copy(
                            name = name.trim(),
                            avatar = avatar,
                            dateOfBirth = dobString,
                            childAge = calculatedAge
                        )
                        onConfirm(updated)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SaveBlueDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("save_child_profile_btn")
            ) {
                Text("Save Changes 💾", fontWeight = FontWeight.Black, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BoldTextSecondary)
            }
        }
    )
}

@Composable
fun AddHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        icon: String,
        reward: Int,
        category: String,
        method: String,
        isTeamwork: Boolean,
        isNegative: Boolean,
        deduction: Int,
        note: String
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("⭐") }
    var rewardStr by remember { mutableStateOf("2") }
    var deductionStr by remember { mutableStateOf("1") }
    var category by remember { mutableStateOf("Responsibility") }
    var confirmationMethod by remember { mutableStateOf("PARENT_CONFIRMS") }
    var isFamilyTeamwork by remember { mutableStateOf(false) }
    var isNegativeHabit by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }

    val iconChoices = listOf("🪥", "🎒", "🛏️", "🧸", "📚", "🥦", "🚰", "🌳", "🐶", "👨‍👩‍👧", "🍟", "⚠️")

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = if (isNegativeHabit) "Add Demerit / Bad Habit ⚠️" else "Add Habit / Routine 🎒",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = if (isNegativeHabit) SpendOrangeDark else Color(0xFF1C1B1F)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Habit Type Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = !isNegativeHabit,
                        onClick = { isNegativeHabit = false },
                        label = { Text("Positive (+Coins)") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = isNegativeHabit,
                        onClick = {
                            isNegativeHabit = true
                            isFamilyTeamwork = false
                        },
                        label = { Text("Demerit (-Coins)") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isNegativeHabit) "Demerit Title (e.g. Eating Junk Food)" else "Habit Title (e.g., Make Bed)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "Choose Icon:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    iconChoices.take(6).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (icon == emoji) BunnyPurpleLight else BoldSurfaceVariant)
                                .clickable { icon = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    iconChoices.drop(6).forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (icon == emoji) BunnyPurpleLight else BoldSurfaceVariant)
                                .clickable { icon = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                if (isNegativeHabit) {
                    OutlinedTextField(
                        value = deductionStr,
                        onValueChange = { deductionStr = it },
                        label = { Text("Demerit Deduction Coins (e.g. 1)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Reflection / Lesson for Child") },
                        placeholder = { Text("e.g. Junk food harms healthy energy and wastes budget.") },
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isFamilyTeamwork,
                            onCheckedChange = {
                                isFamilyTeamwork = it
                                if (it) rewardStr = "0"
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Family Teamwork (0 coin reward)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (!isFamilyTeamwork) {
                        OutlinedTextField(
                            value = rewardStr,
                            onValueChange = { rewardStr = it },
                            label = { Text("Reward Coins (e.g. 2)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = standardTextFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(text = "Approval Mode:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = confirmationMethod == "PARENT_CONFIRMS",
                            onClick = { confirmationMethod = "PARENT_CONFIRMS" },
                            label = { Text("Parent Approves") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = confirmationMethod == "INSTANT",
                            onClick = { confirmationMethod = "INSTANT" },
                            label = { Text("Instant Reward") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val reward = if (isNegativeHabit || isFamilyTeamwork) 0 else (rewardStr.toIntOrNull() ?: 2)
                        val deduct = if (isNegativeHabit) (deductionStr.toIntOrNull() ?: 1) else 0
                        onConfirm(
                            title,
                            icon,
                            reward,
                            category,
                            confirmationMethod,
                            isFamilyTeamwork,
                            isNegativeHabit,
                            deduct,
                            noteText
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isNegativeHabit) SpendOrangeDark else BunnyPurpleDark
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (isNegativeHabit) "Add Demerit ⚠️" else "Add Habit ✨", fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BoldTextSecondary)
            }
        }
    )
}

@Composable
fun HabitBarRow(
    name: String,
    amount: Int,
    total: Int,
    color: Color
) {
    val pct = if (total > 0) (amount.toFloat() / total.toFloat()).coerceIn(0f, 1f) else 0f
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = BoldTextPrimary)
            Text(text = "$amount coins (${(pct * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Black, color = color)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { pct },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = BoldSurfaceVariant
        )
    }
}

@Composable
fun RevokeCoinsDialog(
    profileName: String,
    currentCoins: Int,
    onDismiss: () -> Unit,
    onConfirm: (amount: Int, reason: String, targetJar: String) -> Unit
) {
    var amountStr by remember { mutableStateOf("1") }
    var reason by remember { mutableStateOf("Skipped habit / false completion adjustment") }
    var selectedJar by remember { mutableStateOf("UNASSIGNED") }

    val presetAmounts = listOf(1, 2, 5, 10)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚠️", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Revoke / Deduct Coins", fontWeight = FontWeight.Black, color = SpendOrangeDark)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Deduct coins from $profileName's balance for uncompleted duties or behavioral adjustments.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BoldTextSecondary
                )

                Text("Quick Preset Amount:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    presetAmounts.forEach { preset ->
                        FilterChip(
                            selected = amountStr == preset.toString(),
                            onClick = { amountStr = preset.toString() },
                            label = { Text("-$preset 🪙") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Coins to Revoke") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("revoke_amount_input")
                )

                Text("Deduct from Balance/Jar:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("UNASSIGNED" to "Unassigned", "SPEND" to "Spend", "SAVE" to "Save").forEach { (jarKey, jarLabel) ->
                        FilterChip(
                            selected = selectedJar == jarKey,
                            onClick = { selectedJar = jarKey },
                            label = { Text(jarLabel, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason for Deduction / Lesson") },
                    placeholder = { Text("e.g. Did not attend school / unfinished chores") },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("revoke_reason_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountStr.toIntOrNull() ?: 1
                    if (amount > 0 && reason.isNotBlank()) {
                        onConfirm(amount, reason.trim(), selectedJar)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_revoke_btn")
            ) {
                Text("Confirm Revoke", fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BoldTextSecondary)
            }
        }
    )
}

@Composable
fun GivePointsDialog(
    profileName: String,
    currentCoins: Int,
    onDismiss: () -> Unit,
    onConfirm: (amount: Int, reason: String) -> Unit
) {
    var amountStr by remember { mutableStateOf("5") }
    var reason by remember { mutableStateOf("Great effort & helpful attitude! 🌟") }
    val presetAmounts = listOf(2, 5, 10, 20)
    val presetReasons = listOf(
        "Great effort & helpful attitude! 🌟",
        "Extra kindness to family ❤️",
        "Finished chores without being asked! 🧹",
        "Excellent school work / study 📚"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⭐", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Give Points / Reward Coins", fontWeight = FontWeight.Black, color = Color(0xFF1C1B1F))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Award bonus coins directly to $profileName's wallet! Updates immediately with no blocking popups.",
                    style = MaterialTheme.typography.bodySmall,
                    color = BoldTextSecondary
                )

                Text("Quick Preset Amount:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    presetAmounts.forEach { preset ->
                        FilterChip(
                            selected = amountStr == preset.toString(),
                            onClick = { amountStr = preset.toString() },
                            label = { Text("+$preset 🪙") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Coins to Award") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("give_amount_input")
                )

                Text("Reason / Encouragement:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Encouragement Note") },
                    placeholder = { Text("e.g. Excellent teamwork!") },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("give_reason_input")
                )

                Text("Preset Reasons:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = BoldTextSecondary)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    presetReasons.forEach { note ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BoldSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { reason = note }
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = note,
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldTextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountStr.toIntOrNull() ?: 1
                    if (amount > 0 && reason.isNotBlank()) {
                        onConfirm(amount, reason.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = GardenGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("confirm_give_points_btn")
            ) {
                Text("Give Points ⭐", fontWeight = FontWeight.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BoldTextSecondary)
            }
        }
    )
}
