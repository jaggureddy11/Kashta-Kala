package com.example.kashtakala.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.data.model.FurnitureDesign

val WoodDark   = Color(0xFF4A2C0A)
val WoodMedium = Color(0xFF8B5E3C)
val WoodLight  = Color(0xFFD4956A)
val Amber      = Color(0xFFE8A44A)
val Cream      = Color(0xFFFDF8F2)

@Composable
fun CatalogScreen() {
    val allDesigns = remember { DesignDataSource.getAllDesigns() }
    val favourites = remember { mutableStateSetOf<Int>() }
    var selectedCategory by remember { mutableStateOf("All") }

    val filtered = if (selectedCategory == "All") allDesigns
    else allDesigns.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WoodDark)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    "🪑 Kashta-Kala",
                    color = Amber,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Design Catalog",
                    color = WoodLight,
                    fontSize = 13.sp
                )
            }
        }

        // Category chips
        ScrollableTabRow(
            selectedTabIndex = DesignDataSource.categories.indexOf(selectedCategory),
            containerColor = WoodMedium,
            contentColor = Color.White,
            edgePadding = 8.dp
        ) {
            DesignDataSource.categories.forEach { cat ->
                Tab(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    text = { Text(cat, fontSize = 13.sp) },
                    selectedContentColor = Amber,
                    unselectedContentColor = Color.White
                )
            }
        }

        // Count
        Text(
            "${filtered.size} designs",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = WoodMedium,
            fontSize = 12.sp
        )

        // Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { design ->
                DesignCard(
                    design = design,
                    isFavourite = design.id in favourites,
                    onFavouriteClick = {
                        if (design.id in favourites) favourites.remove(design.id)
                        else favourites.add(design.id)
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DesignCard(
    design: FurnitureDesign,
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Image placeholder (wood-coloured box)
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
                // Keep the favourite button exactly as it is
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

            // Info
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    design.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = WoodDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    design.suggestedWood,
                    fontSize = 11.sp,
                    color = WoodMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(color = Cream, shape = RoundedCornerShape(4.dp))
                        .border(1.dp, WoodLight, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        "${design.estimatedWidth}×${design.estimatedDepth} ft",
                        fontSize = 10.sp,
                        color = WoodMedium
                    )
                }
            }
        }
    }
}

fun <T> mutableStateSetOf(vararg elements: T): MutableSet<T> {
    return mutableSetOf(*elements).also { }
}