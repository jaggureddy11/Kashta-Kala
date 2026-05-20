package com.example.kashtakala.ui.estimator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodDark
import com.example.kashtakala.ui.catalog.WoodLight
import com.example.kashtakala.ui.catalog.WoodMedium
import com.example.kashtakala.ui.SharedViewModel
import com.example.kashtakala.util.WoodCalculator

data class EstimatorResult(
    val areaSqFt: Float,
    val volumeCuFt: Float,
    val materialCost: Float,
    val woodType: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstimatorScreen(sharedViewModel: SharedViewModel) {
    val sharedDesign by sharedViewModel.selectedDesignForEstimation.collectAsState()

    var length       by remember { mutableStateOf("") }
    var width        by remember { mutableStateOf("") }
    var height       by remember { mutableStateOf("") }
    var selectedWood by remember { mutableStateOf("Teak") }
    var errorMsg     by remember { mutableStateOf<String?>(null) }
    var result       by remember { mutableStateOf<EstimatorResult?>(null) }
    var expanded     by remember { mutableStateOf(false) }

    // Pre-fill fields if navigated from Catalog Detail Bottom Sheet
    LaunchedEffect(sharedDesign) {
        sharedDesign?.let { design ->
            length = design.estimatedWidth.toString()
            width = design.estimatedDepth.toString()
            height = design.estimatedHeight.toString()
            selectedWood = design.suggestedWood
            sharedViewModel.clearEstimationSelection()
            
            // Automatically calculate
            val l = design.estimatedWidth
            val w = design.estimatedDepth
            val h = design.estimatedHeight
            val area = WoodCalculator.calculateArea(l, w)
            val volume = WoodCalculator.calculateVolume(l, w, h)
            val matCost = WoodCalculator.calculateMaterialCost(area, design.suggestedWood)
            result = EstimatorResult(area, volume, matCost, design.suggestedWood)
            errorMsg = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
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
                Text(
                    "📐 Material Estimator",
                    color = Amber, fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
                Text(
                    "Calculate wood required & cost",
                    color = WoodLight, fontSize = 13.sp
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // Dimensions card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Furniture Dimensions (in feet)",
                        fontWeight = FontWeight.Bold, color = WoodDark, fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DimensionField("Length", length, Modifier.weight(1f)) { length = it }
                        DimensionField("Width",  width,  Modifier.weight(1f)) { width  = it }
                        DimensionField("Height", height, Modifier.weight(1f)) { height = it }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Wood type card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Wood Type",
                        fontWeight = FontWeight.Bold, color = WoodDark, fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedWood,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WoodMedium,
                                unfocusedBorderColor = WoodLight,
                                focusedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            WoodCalculator.woodTypes.forEach { wood ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(wood, fontWeight = FontWeight.Medium)
                                            Text(
                                                "₹${WoodCalculator.woodCostPerSqFt[wood]?.toInt()}/sq.ft",
                                                fontSize = 11.sp, color = WoodMedium
                                            )
                                        }
                                    },
                                    onClick = { selectedWood = wood; expanded = false }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Error message
            errorMsg?.let {
                Text(it, color = Color.Red, fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Calculate button
            Button(
                onClick = {
                    errorMsg = WoodCalculator.validateInputs(length, width, height)
                    if (errorMsg == null) {
                        val l = length.toFloat()
                        val w = width.toFloat()
                        val h = height.toFloat()
                        val area     = WoodCalculator.calculateArea(l, w)
                        val volume   = WoodCalculator.calculateVolume(l, w, h)
                        val matCost  = WoodCalculator.calculateMaterialCost(area, selectedWood)
                        result = EstimatorResult(area, volume, matCost, selectedWood)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WoodDark),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Calculate", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Amber)
            }

            // Result card
            result?.let { r ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WoodDark),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "📊 Estimation Summary",
                            color = Amber, fontWeight = FontWeight.Bold, fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ResultRow("Wood Type Selected",     r.woodType)
                        ResultRow("Total Area",          "%.2f sq.ft".format(r.areaSqFt))
                        ResultRow("Total Volume",        "%.2f cu.ft".format(r.volumeCuFt))
                        ResultRow("Material Cost (Est.)", "₹%,.0f".format(r.materialCost))
                        HorizontalDivider(
                            color = WoodLight,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "💡 Tip: ",
                                color = Amber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "You can generate a full quotation including labor cost in the Quotes tab.",
                                color = Cream,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatToFtIn(value: String): String {
    val floatVal = value.toFloatOrNull() ?: return ""
    if (floatVal <= 0f) return ""
    val feet = floatVal.toInt()
    val inches = Math.round((floatVal - feet) * 12)
    return if (inches == 0) {
        "$feet ft"
    } else if (feet == 0) {
        "$inches in"
    } else {
        "$feet ft $inches in"
    }
}

@Composable
fun DimensionField(
    label: String,
    value: String,
    modifier: Modifier,
    onChange: (String) -> Unit
) {
    val ftInText = remember(value) { formatToFtIn(value) }
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label, fontSize = 11.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = WoodMedium,
                unfocusedBorderColor = WoodLight,
                focusedLabelColor = WoodMedium,
                focusedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
            )
        )
        if (ftInText.isNotEmpty()) {
            Text(
                text = "≈ $ftInText",
                fontSize = 11.sp,
                color = WoodMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}

@Composable
fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = WoodLight, fontSize = 13.sp)
        Text(value, color = Color.White,
            fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}