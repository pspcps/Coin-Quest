package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.data.model.CustomToyItem
import com.example.data.model.UserProfile
import com.example.ui.theme.standardTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToyShopScreen(
    profile: UserProfile,
    customToys: List<CustomToyItem>,
    onBack: () -> Unit,
    onBuyToy: (toy: CustomToyItem, isSmartAlternative: Boolean) -> Unit,
    onAddToy: (title: String, icon: String, price: Int, category: String, desc: String, altTitle: String, altPrice: Int, xp: Int) -> Unit,
    onDeleteToy: (toyId: Int) -> Unit
) {
    var selectedToyForChoice by remember { mutableStateOf<CustomToyItem?>(null) }
    var showAddToyDialog by remember { mutableStateOf(false) }

    // Dialog state for adding a toy
    var newToyTitle by remember { mutableStateOf("") }
    var newToyIcon by remember { mutableStateOf("🧸") }
    var newToyPrice by remember { mutableStateOf("25") }
    var newToyCategory by remember { mutableStateOf("TOYS") }
    var newToyDesc by remember { mutableStateOf("") }
    var newAltTitle by remember { mutableStateOf("") }
    var newAltPrice by remember { mutableStateOf("8") }
    var newAltXp by remember { mutableStateOf("15") }

    val iconOptions = listOf("🧸", "🚀", "🤖", "🎨", "🎮", "🛹", "🚴", "🧩", "🦄", "⚽", "🚂", "🎸")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🧸 Parent Toy Shop & Choices", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("toy_shop_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showAddToyDialog = true },
                        modifier = Modifier.testTag("parent_add_toy_btn")
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add Toy", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            // Header Balance & Smart XP
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("🎮 Spend Jar Coins", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text("${profile.spendJarCoins} 🪙", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text("Unassigned: ${profile.unassignedCoins}c", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("🌟 ${profile.smartChoiceBadge}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("${profile.smartChoiceXp} Smart XP", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                            Text("Save coins ➡️ Boost XP!", fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f))
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Available Toys & Rewards", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                        Text("Pick standard toys or choose smart alternatives to save!", fontSize = 12.sp, color = Color.Gray)
                    }
                    Button(
                        onClick = { showAddToyDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Add Toy", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (customToys.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No toys added yet! Tap '+ Add Toy' to add family wishlist items.", color = Color.Gray)
                    }
                }
            } else {
                items(customToys, key = { it.id }) { toy ->
                    ToyCard(
                        toy = toy,
                        userSpendCoins = profile.spendJarCoins,
                        onSelectChoice = { selectedToyForChoice = toy },
                        onDelete = { onDeleteToy(toy.id) }
                    )
                }
            }
        }
    }

    // Smart Choice Decision Dialog
    selectedToyForChoice?.let { toy ->
        AlertDialog(
            onDismissRequest = { selectedToyForChoice = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(toy.icon, fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("What's Your Choice?", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        "You can redeem the full toy or choose the smart alternative to save coins and gain Smart XP!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Option 1: Standard toy
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onBuyToy(toy, false)
                                selectedToyForChoice = null
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(toy.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(toy.description, fontSize = 11.sp, color = Color.Gray)
                            }
                            Button(
                                onClick = {
                                    onBuyToy(toy, false)
                                    selectedToyForChoice = null
                                },
                                enabled = profile.spendJarCoins >= toy.price,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("${toy.price} 🪙", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Option 2: Smart alternative (if present)
                    if (!toy.smartAlternativeTitle.isNullOrBlank() && toy.smartAlternativePrice != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFDCFCE7),
                            border = BorderStroke(1.5.dp, Color(0xFF16A34A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onBuyToy(toy, true)
                                    selectedToyForChoice = null
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("⭐ Smart Alternative", color = Color(0xFF15803D), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("+${toy.smartXpReward} XP", color = Color(0xFFB45309), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                    Text(toy.smartAlternativeTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Saves ${toy.price - toy.smartAlternativePrice} coins!", fontSize = 11.sp, color = Color(0xFF15803D), fontWeight = FontWeight.SemiBold)
                                }
                                Button(
                                    onClick = {
                                        onBuyToy(toy, true)
                                        selectedToyForChoice = null
                                    },
                                    enabled = profile.spendJarCoins >= toy.smartAlternativePrice,
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                                ) {
                                    Text("${toy.smartAlternativePrice} 🪙", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { selectedToyForChoice = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Add Toy Dialog (Parent)
    if (showAddToyDialog) {
        AlertDialog(
            onDismissRequest = { showAddToyDialog = false },
            title = { Text("🧸 Add Toy / Wishlist Item", fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text("Choose Icon:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            iconOptions.take(6).forEach { icon ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (newToyIcon == icon) MaterialTheme.colorScheme.primaryContainer else Color.LightGray.copy(alpha = 0.3f))
                                        .clickable { newToyIcon = icon },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(icon, fontSize = 18.sp)
                                }
                            }
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = newToyTitle,
                            onValueChange = { newToyTitle = it },
                            label = { Text("Toy / Item Name") },
                            placeholder = { Text("e.g. Remote Control Car") },
                            colors = standardTextFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = newToyPrice,
                            onValueChange = { newToyPrice = it.filter { c -> c.isDigit() } },
                            label = { Text("Price in Coins") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = standardTextFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = newToyDesc,
                            onValueChange = { newToyDesc = it },
                            label = { Text("Short Description / Goal") },
                            colors = standardTextFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        Text("💡 Smart Alternative (Optional)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    item {
                        OutlinedTextField(
                            value = newAltTitle,
                            onValueChange = { newAltTitle = it },
                            label = { Text("Alternative (e.g. DIY Build or Library)") },
                            colors = standardTextFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = newAltPrice,
                                onValueChange = { newAltPrice = it.filter { c -> c.isDigit() } },
                                label = { Text("Alt Price (Coins)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = standardTextFieldColors(),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newAltXp,
                                onValueChange = { newAltXp = it.filter { c -> c.isDigit() } },
                                label = { Text("Bonus XP") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = standardTextFieldColors(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newToyTitle.isNotBlank()) {
                            val price = newToyPrice.toIntOrNull() ?: 20
                            val altPrice = newAltPrice.toIntOrNull() ?: (price / 3)
                            val altXp = newAltXp.toIntOrNull() ?: 15
                            onAddToy(
                                newToyTitle,
                                newToyIcon,
                                price,
                                newToyCategory,
                                newToyDesc,
                                newAltTitle,
                                altPrice,
                                altXp
                            )
                            newToyTitle = ""
                            newToyDesc = ""
                            newAltTitle = ""
                            showAddToyDialog = false
                        }
                    }
                ) {
                    Text("Add to Shop")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddToyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ToyCard(
    toy: CustomToyItem,
    userSpendCoins: Int,
    onSelectChoice: () -> Unit,
    onDelete: () -> Unit
) {
    val canAffordStandard = userSpendCoins >= toy.price
    val canAffordAlt = toy.smartAlternativePrice?.let { userSpendCoins >= it } ?: false

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(toy.icon, fontSize = 24.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(toy.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(toy.description, fontSize = 12.sp, color = Color.Gray, maxLines = 2)
                    }
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray)
                }
            }

            if (!toy.smartAlternativeTitle.isNullOrBlank()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("Smart Choice: ${toy.smartAlternativeTitle}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                Text("Cost: ${toy.smartAlternativePrice}c (Saves ${toy.price - (toy.smartAlternativePrice ?: 0)}c + Gain XP)", fontSize = 11.sp, color = Color(0xFF166534))
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Standard Cost: ${toy.price} 🪙",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (canAffordStandard) MaterialTheme.colorScheme.primary else Color.Gray
                )

                Button(
                    onClick = onSelectChoice,
                    shape = RoundedCornerShape(12.dp),
                    enabled = canAffordStandard || canAffordAlt
                ) {
                    Text(if (canAffordStandard || canAffordAlt) "Choose / Buy 🛍️" else "Need ${toy.price - userSpendCoins} More")
                }
            }
        }
    }
}
