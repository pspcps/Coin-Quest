package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.data.model.SampleGameData
import com.example.data.model.SkillProjectEntity
import com.example.data.model.UserProfile
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeSkillsScreen(
    profile: UserProfile,
    activeProjects: List<SkillProjectEntity>,
    completedProjects: List<SkillProjectEntity>,
    onBack: () -> Unit,
    onCompleteProject: (project: SkillProjectEntity, notes: String?) -> Unit,
    onDismissSkill: (skillId: String) -> Unit,
    onReloadSkills: () -> Unit = {},
    onAddSkill: (title: String, icon: String, skillName: String, category: String, craftCost: Int, sellPrice: Int, description: String) -> Unit = { _, _, _, _, _, _, _ -> }
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Today's 5 Quests, 1 = Portfolio
    var shuffleSeed by remember { mutableIntStateOf(0) }
    var completingProject by remember { mutableStateOf<SkillProjectEntity?>(null) }
    var showAddSkillDialog by remember { mutableStateOf(false) }
    var userReflectionNotes by remember { mutableStateOf("") }

    // Fallback catalog if DB hasn't populated
    val allCatalog = remember(activeProjects) {
        if (activeProjects.isNotEmpty()) {
            activeProjects
        } else {
            SampleGameData.ageWiseSkillsCatalog.map { skill ->
                SkillProjectEntity(
                    skillId = skill.id,
                    title = skill.title,
                    icon = skill.icon,
                    skillName = skill.skillName,
                    category = skill.category,
                    craftCost = skill.craftCost,
                    sellPrice = skill.sellPrice,
                    minAge = skill.minAge,
                    maxAge = skill.maxAge,
                    description = skill.description,
                    isDismissed = false,
                    isCompleted = false,
                    completedTimes = 0
                )
            }
        }
    }

    // Curate 5 missions for today, prioritizing custom user-created skills first!
    val todayFiveProjects = remember(allCatalog, profile.childAge, shuffleSeed) {
        val userCreated = allCatalog.filter { it.skillId.startsWith("custom_skill_") }
        val standardSkills = allCatalog.filterNot { it.skillId.startsWith("custom_skill_") }
        val matchingAge = standardSkills.filter {
            profile.childAge in it.minAge..it.maxAge || (profile.childAge >= 16 && it.minAge >= 16)
        }
        val pool = if (matchingAge.size >= 5) matchingAge else standardSkills
        val shuffledStandard = pool.shuffled(kotlin.random.Random(profile.id * 31 + profile.childAge + shuffleSeed))
        (userCreated + shuffledStandard).distinctBy { it.skillId }.take(5)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Daily Skill Missions 🎯",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("skills_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BoldTextPrimary)
                    }
                },
                actions = {
                    Button(
                        onClick = { showAddSkillDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("top_add_skill_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Skill", fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }

                    IconButton(
                        onClick = { shuffleSeed++ },
                        modifier = Modifier.testTag("shuffle_skills_btn")
                    ) {
                        Icon(Icons.Default.Casino, contentDescription = "Shuffle New 5", tint = SpendOrangeDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BoldSurface,
                    titleContentColor = BoldTextPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddSkillDialog = true },
                containerColor = SpendOrangeDark,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("add_skill_fab")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Skill")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add Skill 🎨", fontWeight = FontWeight.Black, fontSize = 14.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BoldCanvas)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Primary Tab row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = BoldSurface,
                contentColor = BoldTextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, BoldBorder, RoundedCornerShape(14.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Today's 5 Quests 🎲",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (selectedTab == 0) SpendOrangeDark else BoldTextSecondary
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Portfolio (${completedProjects.size}) 🏆",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = if (selectedTab == 1) GardenGreenDark else BoldTextSecondary
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                // Today's Missions Header with Shuffle Action
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CoinGoldBg),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CoinGoldLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Text("💡", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "TODAY'S 5 CURATED MISSIONS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CoinGoldDark,
                                    letterSpacing = 0.6.sp
                                )
                                Text(
                                    text = "Parent can assign from these 5 quests for ${profile.name} (Age ${profile.childAge})!",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BoldTextPrimary
                                )
                            }
                        }

                        Button(
                            onClick = { shuffleSeed++ },
                            colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("shuffle_5_skills_btn")
                        ) {
                            Text("🎲 Shuffle", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                // 5 Micro-Venture Cards
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(todayFiveProjects, key = { it.skillId }) { project ->
                        DailySkillQuestCard(
                            project = project,
                            onComplete = {
                                completingProject = project
                                userReflectionNotes = ""
                            }
                        )
                    }
                }
            } else {
                // Portfolio of Completed Skills
                if (completedProjects.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎨", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No Completed Skills Yet!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = BoldTextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Choose one of today's 5 quests, make something awesome, and earn your first entrepreneurial profit!",
                                style = MaterialTheme.typography.bodySmall,
                                color = BoldTextSecondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        item {
                            val totalProfit = completedProjects.sumOf { (it.sellPrice - it.craftCost).coerceAtLeast(0) }
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = GardenGreenMint),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GardenGreenLight)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("TOTAL VENTURE PROFIT", fontSize = 10.sp, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                        Text("+$totalProfit 🪙 Earned", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(GardenGreenDark)
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text("${completedProjects.size} Mastered", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                                    }
                                }
                            }
                        }

                        items(completedProjects, key = { "${it.skillId}_${it.completedTimes}" }) { item ->
                            val profit = item.sellPrice - item.craftCost
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp)),
                                colors = CardDefaults.cardColors(containerColor = BoldSurface),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BoldBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Text(item.icon, fontSize = 28.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = BoldTextPrimary)
                                            Text("Skill: ${item.skillName} • ${item.category}", fontSize = 11.sp, color = BoldTextSecondary)
                                            if (!item.userNotes.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text("📝 \"${item.userNotes}\"", fontSize = 11.sp, color = BoldTextPrimary, fontWeight = FontWeight.Medium)
                                            }
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(GardenGreenMint)
                                            .border(1.dp, GardenGreenLight, RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("+$profit 🪙", fontSize = 12.sp, fontWeight = FontWeight.Black, color = GardenGreenDark)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Completion / Reflection Modal
    completingProject?.let { project ->
        val profit = project.sellPrice - project.craftCost
        AlertDialog(
            onDismissRequest = { completingProject = null },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(project.icon, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Complete ${project.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF1C1B1F)
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Awesome work! You crafted real value, practiced '${project.skillName}', and earned +$profit coins net profit! 🪙",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1C1B1F)
                    )

                    OutlinedTextField(
                        value = userReflectionNotes,
                        onValueChange = { userReflectionNotes = it },
                        label = { Text("What did you make/learn? (Optional)") },
                        placeholder = { Text("e.g. Sold handmade bookmarks to Mom!") },
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("skill_reflection_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCompleteProject(project, userReflectionNotes.trim())
                        completingProject = null
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GardenGreenDark),
                    modifier = Modifier.testTag("submit_complete_skill_btn")
                ) {
                    Text("Collect +$profit 🪙 Profit", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { completingProject = null }) {
                    Text("Cancel", color = BoldTextSecondary)
                }
            }
        )
    }

    // Add Custom Skill Quest Modal
    if (showAddSkillDialog) {
        AddCustomSkillDialog(
            onDismiss = { showAddSkillDialog = false },
            onConfirm = { title, icon, skillName, category, craftCost, sellPrice, description ->
                onAddSkill(title, icon, skillName, category, craftCost, sellPrice, description)
                showAddSkillDialog = false
            }
        )
    }
}

@Composable
private fun DailySkillQuestCard(
    project: SkillProjectEntity,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val profit = project.sellPrice - project.craftCost
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = BoldSurface),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BoldBorder),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(CoinGoldBg)
                            .border(1.dp, CoinGoldLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(project.icon, fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = BoldTextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BunnyPurpleLight)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Ages ${project.minAge}-${project.maxAge}",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BunnyPurpleDark
                                )
                            }
                            Text(
                                "• ${project.category}",
                                fontSize = 11.sp,
                                color = BoldTextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Profit Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(GardenGreenMint)
                        .border(1.dp, GardenGreenLight, RoundedCornerShape(10.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "+$profit 🪙 Profit",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = GardenGreenDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Financial Breakdown Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BoldSurfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Material Cost", fontSize = 10.sp, color = BoldTextSecondary)
                    Text("${project.craftCost} 🪙", fontSize = 13.sp, fontWeight = FontWeight.Black, color = SpendOrangeDark)
                }
                Text("➡️", fontSize = 12.sp)
                Column {
                    Text("Customer Price", fontSize = 10.sp, color = BoldTextSecondary)
                    Text("${project.sellPrice} 🪙", fontSize = 13.sp, fontWeight = FontWeight.Black, color = CoinGoldDark)
                }
                Text("=", fontSize = 14.sp, fontWeight = FontWeight.Black)
                Column(horizontalAlignment = Alignment.End) {
                    Text("Net You Keep", fontSize = 10.sp, color = BoldTextSecondary)
                    Text("+$profit 🪙", fontSize = 14.sp, fontWeight = FontWeight.Black, color = GardenGreenDark)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = project.description,
                style = MaterialTheme.typography.bodySmall,
                color = BoldTextPrimary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Skill: ${project.skillName}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BoldTextSecondary
                )

                Button(
                    onClick = onComplete,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoinGoldDark),
                    modifier = Modifier.testTag("complete_quest_${project.skillId}")
                ) {
                    Text("I Did This! 🛠️", fontWeight = FontWeight.Black, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AddCustomSkillDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, icon: String, skillName: String, category: String, craftCost: Int, sellPrice: Int, description: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var icon by remember { mutableStateOf("🎨") }
    var skillName by remember { mutableStateOf("Crafting") }
    var category by remember { mutableStateOf("Creative Arts") }
    var craftCostStr by remember { mutableStateOf("1") }
    var sellPriceStr by remember { mutableStateOf("4") }
    var description by remember { mutableStateOf("") }

    val iconChoices = listOf("🎨", "🍪", "🧵", "🪴", "📚", "🎵", "📸", "🧁", "🧼", "🧶", "💎", "🚀")
    val categoryChoices = listOf("Creative Arts", "Food & Baking", "Tech & Digital", "Handicrafts", "Service & Help")

    val craftCost = craftCostStr.toIntOrNull() ?: 1
    val sellPrice = sellPriceStr.toIntOrNull() ?: 4
    val netProfit = (sellPrice - craftCost).coerceAtLeast(1)

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 28.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Custom Skill Quest 🎨",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1C1B1F)
                )
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
                    "Create your own micro-business or creative quest! Practice real skills and earn profits.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF49454F)
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Quest Title") },
                    placeholder = { Text("e.g. Handmade Bookmarks, Lemonade Stand") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("new_skill_title_input")
                )

                Text("Choose Icon:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
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
                                .border(1.dp, if (icon == emoji) BunnyPurpleDark else BoldBorder, CircleShape)
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
                                .border(1.dp, if (icon == emoji) BunnyPurpleDark else BoldBorder, CircleShape)
                                .clickable { icon = emoji },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 20.sp)
                        }
                    }
                }

                Text("Category:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF49454F))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categoryChoices) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = {
                                category = cat
                                skillName = cat.split(" ").first()
                            },
                            label = { Text(cat, fontSize = 11.sp, fontWeight = if (category == cat) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = craftCostStr,
                        onValueChange = { craftCostStr = it.filter { c -> c.isDigit() } },
                        label = { Text("Cost (🪙)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1f).testTag("new_skill_cost_input")
                    )

                    OutlinedTextField(
                        value = sellPriceStr,
                        onValueChange = { sellPriceStr = it.filter { c -> c.isDigit() } },
                        label = { Text("Price (🪙)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = standardTextFieldColors(),
                        modifier = Modifier.weight(1f).testTag("new_skill_price_input")
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
                        Text("💡", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Net Profit: +$netProfit coins reward when completed!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = CoinGoldDark
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description & Steps") },
                    placeholder = { Text("e.g. Draw bookmarks, color them, sell to friends or parents for 4 coins!") },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = standardTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().testTag("new_skill_desc_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val desc = if (description.isNotBlank()) description.trim() else "Craft and deliver your $title creation to earn reward!"
                        onConfirm(title.trim(), icon, skillName, category, craftCost, sellPrice, desc)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = SpendOrangeDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("submit_new_skill_btn")
            ) {
                Text("Add Quest 🚀", fontWeight = FontWeight.Black, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BoldTextSecondary)
            }
        }
    )
}
