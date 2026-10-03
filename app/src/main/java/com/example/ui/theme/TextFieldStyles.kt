package com.example.ui.theme

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun standardTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFF1C1B1F),
    unfocusedTextColor = Color(0xFF1C1B1F),
    disabledTextColor = Color(0xFF79747E),
    errorTextColor = Color(0xFFB3261E),
    focusedContainerColor = Color(0xFFFFFFFF),
    unfocusedContainerColor = Color(0xFFFDFCFB),
    disabledContainerColor = Color(0xFFF0EBE4),
    cursorColor = SpendOrangeDark,
    focusedBorderColor = SpendOrangeDark,
    unfocusedBorderColor = Color(0xFFD0C8BF),
    focusedLabelColor = SpendOrangeDark,
    unfocusedLabelColor = Color(0xFF49454F),
    focusedPlaceholderColor = Color(0xFF79747E),
    unfocusedPlaceholderColor = Color(0xFF79747E)
)

@Composable
fun filledStandardTextFieldColors() = TextFieldDefaults.colors(
    focusedTextColor = Color(0xFF1C1B1F),
    unfocusedTextColor = Color(0xFF1C1B1F),
    disabledTextColor = Color(0xFF79747E),
    focusedContainerColor = Color(0xFFF7F5F0),
    unfocusedContainerColor = Color(0xFFF7F5F0),
    cursorColor = SpendOrangeDark,
    focusedIndicatorColor = SpendOrangeDark,
    unfocusedIndicatorColor = Color(0xFFD0C8BF),
    focusedLabelColor = SpendOrangeDark,
    unfocusedLabelColor = Color(0xFF49454F),
    focusedPlaceholderColor = Color(0xFF79747E),
    unfocusedPlaceholderColor = Color(0xFF79747E)
)
