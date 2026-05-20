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

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDesignForDetail by remember { mutableStateOf<FurnitureDesign?>(null) }

    val filtered = allDesigns.filter { design ->
        val matchesCategory = selectedCategory == "All" || design.category == selectedCategory
        val matchesSearch = design.name.contains(searchQuery, ignoreCase = true) ||
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
                            "🪑 Kashta-Kala",
                            color = Amber,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Digital Design Catalog",
                            color = WoodLight,
                            fontSize = 13.sp
                        )
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search furniture or wood...", color = WoodLight, fontSize = 13.sp) },
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
                        unfocusedBorderColor = WoodMedium,
                        focusedTextColor = WoodDark,
                        unfocusedTextColor = WoodDark,
                        cursorColor = WoodMedium
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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) WoodDark else Color.White)
                        .border(
                            width = 1.dp,
                            color = if (isSelected) WoodDark else WoodLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Amber else WoodMedium,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Count Label
        Text(
            "${filtered.size} designs found",
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = design.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = WoodDark
                        )
                        Text(
                            text = "${design.category} · Suggested wood: ${design.suggestedWood}",
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
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
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
                            Text(design.category, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Description",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = WoodDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = design.description,
                    fontSize = 13.sp,
                    color = WoodDark,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dimensions Spec Table Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, WoodLight)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Standard Dimensions",
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
                                Text("Length: ${design.estimatedWidth} ft", fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                                Text("Width: ${design.estimatedDepth} ft", fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                            }
                            Column {
                                Text("Height: ${design.estimatedHeight} ft", fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                                val area = design.estimatedWidth * design.estimatedDepth
                                Text("Required Area: %.1f sq.ft".format(area), fontSize = 13.sp, color = WoodMedium, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Calculate Materials", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Create Quote", color = Amber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
    onFavouriteClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
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
                        Text(design.category, color = Color.White,
                            fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
                // Favourite Button
                IconButton(
                    onClick = onFavouriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavourite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = if (isFavourite) Color.Red else Color.White
                    )
                }
            }

            // Details Info
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    design.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = WoodDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    design.suggestedWood,
                    fontSize = 12.sp,
                    color = WoodMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .background(color = Cream, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, WoodLight, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        "${design.estimatedWidth}×${design.estimatedDepth} ft",
                        fontSize = 10.sp,
                        color = WoodMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

