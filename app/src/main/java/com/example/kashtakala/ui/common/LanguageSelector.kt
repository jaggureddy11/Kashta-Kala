package com.example.kashtakala.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.kashtakala.ui.SharedViewModel
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodDark
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.util.Language

@Composable
fun LanguageSelector(sharedViewModel: SharedViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val currentLang by sharedViewModel.selectedLanguage.collectAsState()

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Amber
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Cream)
        ) {
            Language.entries.forEach { lang ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = lang.displayName,
                            color = WoodDark,
                            fontWeight = if (lang == currentLang) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        sharedViewModel.setLanguage(lang)
                        expanded = false
                    }
                )
            }
        }
    }
}
