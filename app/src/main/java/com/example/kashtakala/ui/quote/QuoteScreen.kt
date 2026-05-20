package com.example.kashtakala.ui.quote

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.data.db.AppDatabase
import com.example.kashtakala.data.model.SavedQuote
import com.example.kashtakala.data.repository.QuoteRepository
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodDark
import com.example.kashtakala.ui.catalog.WoodLight
import com.example.kashtakala.ui.catalog.WoodMedium
import com.example.kashtakala.ui.SharedViewModel
import com.example.kashtakala.util.WoodCalculator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(sharedViewModel: SharedViewModel) {
    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val db      = remember { AppDatabase.getDatabase(context) }
    val repo    = remember { QuoteRepository(db.quoteDao()) }

    val sharedDesign by sharedViewModel.selectedDesignForQuote.collectAsState()

    var customerName  by remember { mutableStateOf("") }
    var designName    by remember { mutableStateOf("") }
    var woodType      by remember { mutableStateOf("Teak") }
    var length        by remember { mutableStateOf("") }
    var width         by remember { mutableStateOf("") }
    var height        by remember { mutableStateOf("") }
    var labourCost    by remember { mutableStateOf("") }
    var overheadPct   by remember { mutableStateOf("10") }
    val savedQuotes   by repo.allQuotes.collectAsState(initial = emptyList())
    var snackMsg      by remember { mutableStateOf<String?>(null) }
    var showSaved     by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackMsg) {
        snackMsg?.let {
            snackbarHostState.showSnackbar(it)
            snackMsg = null
        }
    }

    // Pre-fill state when navigated from Catalog
    LaunchedEffect(sharedDesign) {
        sharedDesign?.let { design ->
            designName = design.name
            woodType = design.suggestedWood
            length = design.estimatedWidth.toString()
            width = design.estimatedDepth.toString()
            height = design.estimatedHeight.toString()
            showSaved = false
            sharedViewModel.clearQuoteSelection()
        }
    }

    // Calculate totals
    val lengthVal = length.toFloatOrNull() ?: 0f
    val widthVal  = width.toFloatOrNull() ?: 0f
    val area = if (lengthVal > 0 && widthVal > 0) WoodCalculator.calculateArea(lengthVal, widthVal) else 0f
    val matCost  = if (area > 0) WoodCalculator.calculateMaterialCost(area, woodType) else 0f
    val labour   = labourCost.toFloatOrNull() ?: 0f
    val overhead = overheadPct.toFloatOrNull() ?: 0f
    val total    = WoodCalculator.calculateTotalCost(matCost, labour, overhead)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Cream
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WoodDark)
                    .padding(16.dp)
            ) {
                Column {
                    Text("💰 Price Quote", color = Amber,
                        fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Generate & save customer quotes",
                        color = WoodLight, fontSize = 13.sp)
                }
            }

            // Segmented Tab Toggle Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showSaved = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showSaved) WoodDark else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("New Quote", color = if (!showSaved) Amber else WoodMedium, fontWeight = FontWeight.Bold) }

                Button(
                    onClick = { showSaved = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showSaved) WoodDark else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Saved (${savedQuotes.size})", color = if (showSaved) Amber else WoodMedium, fontWeight = FontWeight.Bold) }
            }

            if (!showSaved) {
                // ── NEW QUOTE FORM ──────────────────────────────────
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    QuoteCard(title = "Customer Details") {
                        QuoteField("Customer Name", customerName) { customerName = it }
                        Spacer(Modifier.height(8.dp))
                        QuoteField("Design / Item Name", designName) { designName = it }
                    }

                    Spacer(Modifier.height(12.dp))

                    QuoteCard(title = "Dimensions & Wood") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            QuoteField("L (ft)", length, Modifier.weight(1f), KeyboardType.Decimal) { length = it }
                            QuoteField("W (ft)", width,  Modifier.weight(1f), KeyboardType.Decimal) { width  = it }
                            QuoteField("H (ft)", height, Modifier.weight(1f), KeyboardType.Decimal) { height = it }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text("Selected Wood Type", fontSize = 12.sp, color = WoodMedium, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(6.dp))
                        // Wood type chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            WoodCalculator.woodTypes.forEach { w ->
                                FilterChip(
                                    selected = woodType == w,
                                    onClick = { woodType = w },
                                    label = { Text(w, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = WoodDark,
                                        selectedLabelColor = Amber,
                                        containerColor = Cream,
                                        labelColor = WoodDark
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    QuoteCard(title = "Cost Breakdown") {
                        QuoteField("Labour Cost (₹)", labourCost, keyboardType = KeyboardType.Decimal) { labourCost = it }
                        Spacer(Modifier.height(8.dp))
                        QuoteField("Overhead %", overheadPct, keyboardType = KeyboardType.Decimal) { overheadPct = it }
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = WoodLight)
                        Spacer(Modifier.height(8.dp))
                        QuoteSummaryRow("Material Cost", "₹%,.0f".format(matCost))
                        QuoteSummaryRow("Labour Cost",   "₹%,.0f".format(labour))
                        QuoteSummaryRow("Overhead / Miscellaneous", "₹%,.0f".format((matCost + labour) * overhead / 100))
                        HorizontalDivider(color = WoodLight, modifier = Modifier.padding(vertical = 6.dp))
                        QuoteSummaryRow("TOTAL ESTIMATE", "₹%,.0f".format(total), bold = true)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Buttons Row for Share & Save
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (customerName.isBlank()) {
                                    snackMsg = "Please enter customer name"
                                    return@Button
                                }
                                if (area <= 0f) {
                                    snackMsg = "Please enter valid dimensions"
                                    return@Button
                                }
                                val text = formatQuoteForSharing(
                                    customerName = customerName,
                                    designName = designName.ifBlank { "Custom" },
                                    woodType = woodType,
                                    length = length,
                                    width = width,
                                    height = height,
                                    matCost = matCost,
                                    labour = labour,
                                    overhead = overhead,
                                    total = total
                                )
                                shareQuoteText(context, text)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WoodMedium),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("🔗 Share", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = {
                                if (customerName.isBlank()) {
                                    snackMsg = "Please enter customer name"
                                    return@Button
                                }
                                if (area <= 0f) {
                                    snackMsg = "Please enter valid dimensions"
                                    return@Button
                                }
                                scope.launch {
                                    repo.insert(SavedQuote(
                                        customerName  = customerName,
                                        designName    = designName.ifBlank { "Custom" },
                                        woodType      = woodType,
                                        lengthFt      = length.toFloatOrNull() ?: 0f,
                                        widthFt       = width.toFloatOrNull()  ?: 0f,
                                        heightFt      = height.toFloatOrNull() ?: 0f,
                                        areaSqFt      = area,
                                        materialCost  = matCost,
                                        labourCost    = labour,
                                        overheadPercent = overhead,
                                        totalCost     = total
                                    ))
                                    snackMsg    = "✅ Quote saved for $customerName!"
                                    customerName = ""; designName = ""
                                    length = ""; width = ""; height = ""
                                    labourCost = ""; overheadPct = "10"
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WoodDark),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("💾 Save", fontSize = 15.sp,
                                fontWeight = FontWeight.Bold, color = Amber)
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }

            } else {
                // ── SAVED QUOTES LIST ───────────────────────────────
                if (savedQuotes.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text("No saved quotes yet.\nCreate one in New Quote tab.",
                            color = WoodMedium, fontSize = 14.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        savedQuotes.forEach { quote ->
                            SavedQuoteCard(
                                quote = quote,
                                context = context,
                                onDelete = {
                                    scope.launch {
                                        repo.delete(quote)
                                    }
                                }
                            )
                        }
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

fun shareQuoteText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share Quote via")
    context.startActivity(shareIntent)
}

fun formatQuoteForSharing(
    customerName: String,
    designName: String,
    woodType: String,
    length: String,
    width: String,
    height: String,
    matCost: Float,
    labour: Float,
    overhead: Float,
    total: Float
): String {
    return """
        🪵 *KASHTA-KALA PRICE QUOTE* 🪵
        ------------------------------------
        *Customer Name:* $customerName
        *Item Name:* $designName
        *Wood Type:* $woodType
        *Dimensions:* $length ft x $width ft x $height ft
        
        *Cost Breakdown:*
        - Material Cost: ₹${"%,.0f".format(matCost)}
        - Labour Cost: ₹${"%,.0f".format(labour)}
        - Overhead / Misc: ₹${"%,.0f".format((matCost + labour) * overhead / 100)}
        ------------------------------------
        *ESTIMATED TOTAL: ₹${"%,.0f".format(total)}*
        
        _Thank you for choosing Kashta-Kala!_
    """.trimIndent()
}

fun formatSavedQuoteForSharing(quote: SavedQuote): String {
    return """
        🪵 *KASHTA-KALA PRICE QUOTE* 🪵
        ------------------------------------
        *Customer Name:* ${quote.customerName}
        *Item/Design:* ${quote.designName}
        *Wood Type:* ${quote.woodType}
        *Dimensions:* ${quote.lengthFt} ft x ${quote.widthFt} ft x ${quote.heightFt} ft
        
        *Cost Breakdown:*
        - Material Cost: ₹${"%,.0f".format(quote.materialCost)}
        - Labour Cost: ₹${"%,.0f".format(quote.labourCost)}
        - Overhead / Misc: ₹${"%,.0f".format((quote.materialCost + quote.labourCost) * quote.overheadPercent / 100)}
        ------------------------------------
        *ESTIMATED TOTAL: ₹${"%,.0f".format(quote.totalCost)}*
        
        _Thank you for choosing Kashta-Kala!_
    """.trimIndent()
}

@Composable
fun QuoteCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold,
                color = WoodDark, fontSize = 14.sp)
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun QuoteField(
    label: String, value: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value, onValueChange = onChange,
        label = { Text(label, fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true, modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WoodMedium,
            unfocusedBorderColor = WoodLight,
            focusedLabelColor = WoodMedium,
            focusedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
fun QuoteSummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = WoodMedium, fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(value, color = WoodDark, fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun SavedQuoteCard(quote: SavedQuote, context: Context, onDelete: () -> Unit) {
    val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(quote.dateCreated))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(quote.customerName, fontWeight = FontWeight.Bold,
                    color = WoodDark, fontSize = 15.sp)
                Text("₹%,.0f".format(quote.totalCost),
                    fontWeight = FontWeight.Bold, color = Amber, fontSize = 15.sp)
            }
            Spacer(Modifier.height(4.dp))
            Text("${quote.designName} · ${quote.woodType}",
                color = WoodMedium, fontSize = 12.sp)
            Text("${quote.lengthFt}×${quote.widthFt}×${quote.heightFt} ft · $date",
                color = WoodLight, fontSize = 11.sp)
            Spacer(Modifier.height(8.dp))
            
            HorizontalDivider(color = Cream, modifier = Modifier.padding(vertical = 4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = {
                        val text = formatSavedQuoteForSharing(quote)
                        shareQuoteText(context, text)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = WoodMedium
                    )
                ) {
                    Text("🔗 Share Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("🗑 Delete", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}