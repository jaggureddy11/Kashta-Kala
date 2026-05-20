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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PortfolioScreen() {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val db      = remember { AppDatabase.getDatabase(context) }
    val repo    = remember { PortfolioRepository(db.portfolioDao()) }

    var items       by remember { mutableStateOf<List<PortfolioItem>>(emptyList()) }
    var captionInput by remember { mutableStateOf("") }
    var pendingUri  by remember { mutableStateOf<Uri?>(null) }
    var showDialog  by remember { mutableStateOf(false) }
    val snackState  = remember { SnackbarHostState() }

    // Load items
    LaunchedEffect(Unit) {
        items = repo.allItems.first()
    }

    // Image picker launcher
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pendingUri = uri
            showDialog = true
        }
    }

    // Caption dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false; pendingUri = null },
            title = { Text("Add Caption", color = WoodDark, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = captionInput,
                    onValueChange = { captionInput = it },
                    label = { Text("Caption (optional)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WoodMedium,
                        unfocusedBorderColor = WoodLight
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
                                items = repo.allItems.first()
                                captionInput = ""
                                pendingUri   = null
                                showDialog   = false
                                snackState.showSnackbar("Photo added to portfolio!")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WoodDark)
                ) { Text("Save", color = Amber) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false; pendingUri = null }) {
                    Text("Cancel", color = WoodMedium)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { picker.launch("image/*") },
                containerColor = WoodDark
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Photo", tint = Amber)
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
                Column {
                    Text("📸 My Portfolio", color = Amber,
                        fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("${items.size} photos · Tap + to add",
                        color = WoodLight, fontSize = 13.sp)
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
                        Text("No photos yet",
                            color = WoodMedium, fontSize = 16.sp,
                            fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))
                        Text("Tap the + button to add\nyour finished work",
                            color = WoodLight, fontSize = 13.sp,
                            textAlign = TextAlign.Center)
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
                            onDelete = {
                                scope.launch {
                                    repo.delete(item)
                                    items = repo.allItems.first()
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
fun PortfolioCard(item: PortfolioItem, onDelete: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Delete Photo?", color = WoodDark) },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Delete", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancel", color = WoodMedium)
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
                Icon(Icons.Filled.Delete, contentDescription = "Delete",
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