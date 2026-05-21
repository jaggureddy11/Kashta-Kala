package com.example.kashtakala.ui.portfolio

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.kashtakala.data.db.AppDatabase
import com.example.kashtakala.data.model.PortfolioItem
import com.example.kashtakala.data.repository.PortfolioRepository
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodDark
import com.example.kashtakala.ui.catalog.WoodLight
import com.example.kashtakala.ui.catalog.WoodMedium
import com.example.kashtakala.ui.SharedViewModel
import com.example.kashtakala.ui.common.LanguageSelector
import com.example.kashtakala.util.Language
import com.example.kashtakala.util.TranslationHelper
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PortfolioScreen(sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val db      = remember { AppDatabase.getDatabase(context) }
    val repo    = remember { PortfolioRepository(db.portfolioDao()) }

    val items       by repo.allItems.collectAsState(initial = emptyList())
    val currentLang by sharedViewModel.selectedLanguage.collectAsState()
    var captionInput by remember { mutableStateOf("") }
    var pendingUri  by remember { mutableStateOf<Uri?>(null) }
    var showDialog  by remember { mutableStateOf(false) }
    val snackState  = remember { SnackbarHostState() }

    // Image picker launcher
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pendingUri = uri
            showDialog = true
        }
    }

    val addedToast = when(currentLang) {
        Language.KANNADA -> "ಪೋರ್ಟ್‌ಫೋಲಿಯೋಗೆ ಫೋಟೋ ಸೇರಿಸಲಾಗಿದೆ!"
        Language.TELUGU -> "పోర్ట్‌ఫోలియోకి ఫోటో జోడించబడింది!"
        Language.HINDI -> "पोर्टफोलियो में फोटो जोड़ा गया!"
        else -> "Photo added to portfolio!"
    }

    // Caption dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; pendingUri = null },
            title = {
                Text(
                    TranslationHelper.getString("portfolio_dialog_title", currentLang),
                    color = WoodDark, fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = captionInput,
                    onValueChange = { captionInput = it },
                    label = { Text(TranslationHelper.getString("portfolio_dialog_label", currentLang)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WoodMedium,
                        unfocusedBorderColor = WoodLight,
                        focusedTextColor = WoodDark,
                        unfocusedTextColor = WoodDark
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        pendingUri?.let { uri ->
                            scope.launch {
                                // Copy image to internal storage
                                val fileName = "portfolio_${System.currentTimeMillis()}.jpg"
                                val file = File(context.filesDir, fileName)
                                context.contentResolver.openInputStream(uri)?.use { input ->
                                    FileOutputStream(file).use { output ->
                                        input.copyTo(output)
                                    }
                                }
                                repo.insert(PortfolioItem(
                                    imagePath = file.absolutePath,
                                    caption   = captionInput
                                ))
                                captionInput = ""
                                pendingUri   = null
                                showDialog   = false
                                snackState.showSnackbar(addedToast)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WoodDark)
                ) {
                    Text(
                        TranslationHelper.getString("portfolio_dialog_save", currentLang),
                        color = Amber
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; pendingUri = null }) {
                    Text(
                        TranslationHelper.getString("portfolio_dialog_cancel", currentLang),
                        color = WoodMedium
                    )
                }
            }
        )
    }

    val addPhotoDesc = when(currentLang) {
        Language.KANNADA -> "ಫೋಟೋ ಸೇರಿಸಿ"
        Language.TELUGU -> "ఫోటో జోడించండి"
        Language.HINDI -> "फोटो जोड़ें"
        else -> "Add Photo"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { picker.launch("image/*") },
                containerColor = WoodDark
            ) {
                Icon(Icons.Filled.Add, contentDescription = addPhotoDesc, tint = Amber)
            }
        },
        containerColor = Cream
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WoodDark)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            TranslationHelper.getString("portfolio_title", currentLang),
                            color = Amber, fontSize = 22.sp, fontWeight = FontWeight.Bold
                        )
                        val photoUnit = when(currentLang) {
                            Language.KANNADA -> "ಫೋಟೋಗಳು"
                            Language.TELUGU -> "ఫోటోలు"
                            Language.HINDI -> "तस्वीरें"
                            else -> "photos"
                        }
                        val tapToAdd = when(currentLang) {
                            Language.KANNADA -> "ಸೇರಿಸಲು + ಒತ್ತಿರಿ"
                            Language.TELUGU -> "జోడించడానికి + నొక్కండి"
                            Language.HINDI -> "जोड़ने के लिए + दबाएं"
                            else -> "Tap + to add"
                        }
                        Text(
                            "${items.size} $photoUnit · $tapToAdd",
                            color = WoodLight, fontSize = 13.sp
                        )
                    }
                    LanguageSelector(sharedViewModel = sharedViewModel)
                }
            }

            if (items.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📷", fontSize = 48.sp)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            TranslationHelper.getString("portfolio_empty_title", currentLang),
                            color = WoodMedium, fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            TranslationHelper.getString("portfolio_empty_desc", currentLang),
                            color = WoodLight, fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items) { item ->
                        PortfolioCard(
                            item = item,
                            currentLang = currentLang,
                            onDelete = {
                                scope.launch {
                                    repo.delete(item)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PortfolioCard(item: PortfolioItem, currentLang: Language, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        val deleteTitle = when(currentLang) {
            Language.KANNADA -> "ಫೋಟೋ ಅಳಿಸಬೇಕೇ?"
            Language.TELUGU -> "ఫోటోను తొలగించాలా?"
            Language.HINDI -> "फोटो हटाएं?"
            else -> "Delete Photo?"
        }
        val deleteLabel = when(currentLang) {
            Language.KANNADA -> "ಅಳಿಸಿ"
            Language.TELUGU -> "తొలగించు"
            Language.HINDI -> "हटाएं"
            else -> "Delete"
        }
        val cancelLabel = when(currentLang) {
            Language.KANNADA -> "ರದ್ದುಮಾಡಿ"
            Language.TELUGU -> "రద్దు చేయి"
            Language.HINDI -> "रद्द करें"
            else -> "Cancel"
        }

        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text(deleteTitle, color = WoodDark) },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text(deleteLabel, color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text(cancelLabel, color = WoodMedium)
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Box {
            GlideImage(
                model = File(item.imagePath),
                contentDescription = item.caption,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            )
            IconButton(
                onClick = { showConfirm = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                val deleteDesc = when(currentLang) {
                    Language.KANNADA -> "ಅಳಿಸಿ"
                    Language.TELUGU -> "తొలగించు"
                    Language.HINDI -> "हटाएं"
                    else -> "Delete"
                }
                Icon(Icons.Filled.Delete, contentDescription = deleteDesc,
                    tint = Color.White)
            }
        }
        if (item.caption.isNotBlank()) {
            Text(
                item.caption,
                modifier = Modifier.padding(8.dp),
                fontSize = 12.sp, color = WoodDark,
                fontWeight = FontWeight.Medium
            )
        }
    }
}