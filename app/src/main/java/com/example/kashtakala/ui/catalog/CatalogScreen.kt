package com.example.kashtakala.ui.catalog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyRowItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.data.db.AppDatabase
import com.example.kashtakala.data.model.FavouriteDesign
import com.example.kashtakala.data.model.FurnitureDesign
import com.example.kashtakala.data.repository.FavouriteRepository
import com.example.kashtakala.ui.SharedViewModel
import com.example.kashtakala.ui.common.LanguageSelector
import com.example.kashtakala.util.Language
import com.example.kashtakala.util.TranslationHelper
import kotlinx.coroutines.launch

val WoodDark   = Color(0xFF4A2C0A)
val WoodMedium = Color(0xFF8B5E3C)
val WoodLight  = Color(0xFFD4956A)
val Amber      = Color(0xFFE8A44A)
val Cream      = Color(0xFFFDF8F2)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CatalogScreen(
    sharedViewModel: SharedViewModel,
    onNavigateToScreen: (String) -> Unit
) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val db      = remember { AppDatabase.getDatabase(context) }
    val repo    = remember { FavouriteRepository(db.favouriteDao()) }

    val allDesigns = remember { DesignDataSource.getAllDesigns() }
    val dbFavourites by repo.allFavourites.collectAsState(initial = emptyList())
    val favouriteIds = remember(dbFavourites) { dbFavourites.map { it.designId }.toSet() }
    val currentLang by sharedViewModel.selectedLanguage.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDesignForDetail by remember { mutableStateOf<FurnitureDesign?>(null) }

    val filtered = allDesigns.filter { design ->
        val matchesCategory = selectedCategory == "All" || design.category == selectedCategory
        val matchesSearch = TranslationHelper.getDesignName(design.id, currentLang).contains(searchQuery, ignoreCase = true) ||
                design.suggestedWood.contains(searchQuery, ignoreCase = true) ||
                design.category.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        // Header with Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WoodDark)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            TranslationHelper.getString("catalog_title", currentLang),
                            color = Amber,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            TranslationHelper.getString("catalog_subtitle", currentLang),
                            color = WoodLight,
                            fontSize = 13.sp
                        )
                    }
                    LanguageSelector(sharedViewModel = sharedViewModel)
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(TranslationHelper.getString("catalog_search", currentLang), color = WoodLight, fontSize = 13.sp) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Amber,
                        unfocusedBorderColor = WoodLight.copy(alpha = 0.5f),
                        focusedTextColor = WoodDark,
                        unfocusedTextColor = WoodDark,
                        cursorColor = Amber
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = WoodMedium
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear search",
                                    tint = WoodMedium
                                )
                            }
                        }
                    }
                )
            }
        }

        // Custom Capsule Category Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Cream)
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            lazyRowItems(DesignDataSource.categories) { cat ->
                val isSelected = selectedCategory == cat
                val localizedCat = when(cat) {
                    "All" -> TranslationHelper.getString("catalog_filter_all", currentLang)
                    "Bed" -> TranslationHelper.getString("catalog_filter_bed", currentLang)
                    "Sofa" -> TranslationHelper.getString("catalog_filter_sofa", currentLang)
                    "Cabinet" -> when(currentLang) {
                        Language.KANNADA -> "ಕ್ಯಾಬಿನೆಟ್"
                        Language.TELUGU -> "క్యాబినెట్"
                        Language.HINDI -> "कैबिनेट"
                        else -> "Cabinet"
                    }
                    "Wardrobe" -> TranslationHelper.getString("catalog_filter_wardrobe", currentLang)
                    "Table" -> when(currentLang) {
                        Language.KANNADA -> "ಮೇಜು"
                        Language.TELUGU -> "బల్ల"
                        Language.HINDI -> "मेज"
                        else -> "Table"
                    }
                    else -> cat
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) WoodDark else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) WoodDark else WoodLight.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = localizedCat,
                        color = if (isSelected) Amber else WoodMedium,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Count Label
        val countText = when(currentLang) {
            Language.KANNADA -> "${filtered.size} ವಿನ್ಯಾಸಗಳು ಪತ್ತೆಯಾಗಿವೆ"
            Language.TELUGU -> "${filtered.size} డిజైన్లు కనుగొనబడ్డాయి"
            Language.HINDI -> "${filtered.size} डिजाइन मिले"
            else -> "${filtered.size} designs found"
        }
        Text(
            countText,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            color = WoodMedium,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        // Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { design ->
                DesignCard(
                    design = design,
                    isFavourite = design.id in favouriteIds,
                    currentLang = currentLang,
                    onFavouriteClick = {
                        scope.launch {
                            if (design.id in favouriteIds) {
                                repo.remove(FavouriteDesign(design.id))
                              } else {
                                repo.add(FavouriteDesign(design.id))
                              }
                        }
                    },
                    onCardClick = {
                        selectedDesignForDetail = design
                    }
                )
            }
        }
    }

    // Detail Bottom Sheet
    if (selectedDesignForDetail != null) {
        val design = selectedDesignForDetail!!
        ModalBottomSheet(
            onDismissRequest = { selectedDesignForDetail = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Cream
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header inside Bottom Sheet
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    val localizedDesignName = TranslationHelper.getDesignName(design.id, currentLang)
                    val localizedWoodName = when(design.suggestedWood.lowercase()) {
                        "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                        "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                        "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                        "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                        "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                        "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                        else -> design.suggestedWood
                    }
                    val localizedCategory = when(design.category) {
                        "Bed" -> TranslationHelper.getString("catalog_filter_bed", currentLang)
                        "Sofa" -> TranslationHelper.getString("catalog_filter_sofa", currentLang)
                        "Cabinet" -> when(currentLang) {
                            Language.KANNADA -> "ಕ್ಯಾಬಿನೆಟ್"
                            Language.TELUGU -> "క్యాబినెట్"
                            Language.HINDI -> "कैबिनेट"
                            else -> "Cabinet"
                        }
                        "Wardrobe" -> TranslationHelper.getString("catalog_filter_wardrobe", currentLang)
                        "Table" -> when(currentLang) {
                            Language.KANNADA -> "ಮೇಜು"
                            Language.TELUGU -> "బల్ల"
                            Language.HINDI -> "मेज"
                            else -> "Table"
                        }
                        else -> design.category
                    }
                    val suggestedWoodText = when(currentLang) {
                        Language.KANNADA -> "ಶಿಫಾರಸು ಮಾಡಿದ ಮರ: $localizedWoodName"
                        Language.TELUGU -> "సిఫార్సు చేసిన కలప: $localizedWoodName"
                        Language.HINDI -> "सुझाई गई लकड़ी: $localizedWoodName"
                        else -> "Suggested wood: $localizedWoodName"
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = localizedDesignName,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = WoodDark
                        )
                        Text(
                            text = "$localizedCategory · $suggestedWoodText",
                            fontSize = 14.sp,
                            color = WoodMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (design.id in favouriteIds) {
                                    repo.remove(FavouriteDesign(design.id))
                                } else {
                                    repo.add(FavouriteDesign(design.id))
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (design.id in favouriteIds) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (design.id in favouriteIds) Color.Red else WoodMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Image Banner
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    if (design.imageRes != 0) {
                        GlideImage(
                            model = design.imageRes,
                            contentDescription = design.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(WoodMedium),
                            contentAlignment = Alignment.Center
                        ) {
                            val categoryLabel = when(design.category) {
                                "Bed" -> TranslationHelper.getString("catalog_filter_bed", currentLang)
                                "Sofa" -> TranslationHelper.getString("catalog_filter_sofa", currentLang)
                                "Cabinet" -> when(currentLang) {
                                    Language.KANNADA -> "ಕ್ಯಾಬಿನೆಟ್"
                                    Language.TELUGU -> "క్యాబినెట్"
                                    Language.HINDI -> "कैबिनेट"
                                    else -> "Cabinet"
                                }
                                "Wardrobe" -> TranslationHelper.getString("catalog_filter_wardrobe", currentLang)
                                "Table" -> when(currentLang) {
                                    Language.KANNADA -> "ಮೇಜು"
                                    Language.TELUGU -> "బల్ల"
                                    Language.HINDI -> "मेज"
                                    else -> "Table"
                                }
                                else -> design.category
                            }
                            Text(categoryLabel, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = TranslationHelper.getString("catalog_details_description", currentLang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WoodDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = TranslationHelper.getDesignDesc(design.id, currentLang),
                    fontSize = 13.sp,
                    color = WoodDark,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dimensions Spec Table Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, WoodLight.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        val dimensionsTitle = when(currentLang) {
                            Language.KANNADA -> "ಪ್ರಮಾಣಿತ ಅಳತೆಗಳು"
                            Language.TELUGU -> "ప్రామాణిక కొలతలు"
                            Language.HINDI -> "मानक आकार"
                            else -> "Standard Dimensions"
                        }
                        val lengthLabel = when(currentLang) {
                            Language.KANNADA -> "ಉದ್ದ"
                            Language.TELUGU -> "పొడవు"
                            Language.HINDI -> "लंबाई"
                            else -> "Length"
                        }
                        val widthLabel = when(currentLang) {
                            Language.KANNADA -> "ಅಗಲ"
                            Language.TELUGU -> "వెడల్పు"
                            Language.HINDI -> "चौड़ाई"
                            else -> "Width"
                        }
                        val heightLabel = when(currentLang) {
                            Language.KANNADA -> "ಎತ್ತರ"
                            Language.TELUGU -> "ఎత్తు"
                            Language.HINDI -> "ऊंचाई"
                            else -> "Height"
                        }
                        val areaLabel = when(currentLang) {
                            Language.KANNADA -> "ಅಗತ್ಯವಿರುವ ವಿಸ್ತೀರ್ಣ"
                            Language.TELUGU -> "అవసరమైన వైశాల్యం"
                            Language.HINDI -> "आवश्यक क्षेत्रफल"
                            else -> "Required Area"
                        }
                        val ftUnit = when(currentLang) {
                            Language.KANNADA -> "ಅಡಿ"
                            Language.TELUGU -> "అడుగులు"
                            Language.HINDI -> "फिट"
                            else -> "ft"
                        }
                        val sqftUnit = when(currentLang) {
                            Language.KANNADA -> "ಚದರ ಅಡಿ"
                            Language.TELUGU -> "చదరపు అడుగులు"
                            Language.HINDI -> "वर्ग फिट"
                            else -> "sq.ft"
                        }
                        Text(
                            text = dimensionsTitle,
                            fontWeight = FontWeight.Bold,
                            color = WoodDark,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("$lengthLabel: ${design.estimatedWidth} $ftUnit", fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                                Text("$widthLabel: ${design.estimatedDepth} $ftUnit", fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                            }
                            Column {
                                Text("$heightLabel: ${design.estimatedHeight} $ftUnit", fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                                val area = design.estimatedWidth * design.estimatedDepth
                                Text("$areaLabel: %.1f $sqftUnit".format(area), fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                // Customizations Note Card
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Cream.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, WoodLight.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = TranslationHelper.getString("catalog_custom_title", currentLang),
                            fontWeight = FontWeight.Bold,
                            color = WoodDark,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = TranslationHelper.getString("catalog_custom_note", currentLang),
                            fontSize = 12.sp,
                            color = WoodMedium,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            sharedViewModel.selectDesignForEstimation(design)
                            selectedDesignForDetail = null
                            onNavigateToScreen("estimator")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WoodMedium),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(TranslationHelper.getString("catalog_btn_estimate", currentLang), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            sharedViewModel.selectDesignForQuote(design)
                            selectedDesignForDetail = null
                            onNavigateToScreen("quote")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WoodDark),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(TranslationHelper.getString("catalog_btn_quote", currentLang), color = Amber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DesignCard(
    design: FurnitureDesign,
    isFavourite: Boolean,
    currentLang: Language,
    onFavouriteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(1.dp, WoodLight.copy(alpha = 0.2f))
    ) {
        Column {
            // Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (design.imageRes != 0) {
                    GlideImage(
                        model = design.imageRes,
                        contentDescription = TranslationHelper.getDesignName(design.id, currentLang),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WoodMedium)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val categoryLabel = when(design.category) {
                            "Bed" -> TranslationHelper.getString("catalog_filter_bed", currentLang)
                            "Sofa" -> TranslationHelper.getString("catalog_filter_sofa", currentLang)
                            "Cabinet" -> when(currentLang) {
                                Language.KANNADA -> "ಕ್ಯಾಬಿನೆಟ್"
                                Language.TELUGU -> "క్యాబినెట్"
                                Language.HINDI -> "कैबिनेट"
                                else -> "Cabinet"
                            }
                            "Wardrobe" -> TranslationHelper.getString("catalog_filter_wardrobe", currentLang)
                            "Table" -> when(currentLang) {
                                Language.KANNADA -> "ಮೇಜು"
                                Language.TELUGU -> "బల్ల"
                                Language.HINDI -> "मेज"
                                else -> "Table"
                            }
                            else -> design.category
                        }
                        Text(categoryLabel, color = Color.White,
                            fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
                // Favourite Button in semi-transparent circle
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .align(Alignment.TopEnd)
                        .clickable { onFavouriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = if (isFavourite) Color.Red else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Details Info
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    TranslationHelper.getDesignName(design.id, currentLang),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = WoodDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                val localizedWood = when(design.suggestedWood.lowercase()) {
                    "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                    "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                    "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                    "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                    "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                    "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                    else -> design.suggestedWood
                }
                Text(
                    localizedWood,
                    fontSize = 12.sp,
                    color = WoodMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .background(color = Cream, shape = RoundedCornerShape(6.dp))
                        .border(1.dp, WoodLight.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    val ftUnit = when(currentLang) {
                        Language.KANNADA -> "ಅಡಿ"
                        Language.TELUGU -> "అడుగులు"
                        Language.HINDI -> "फिट"
                        else -> "ft"
                    }
                    Text(
                        "${design.estimatedWidth}×${design.estimatedDepth} $ftUnit",
                        fontSize = 10.sp,
                        color = WoodMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

