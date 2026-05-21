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
import com.example.kashtakala.ui.common.LanguageSelector
import com.example.kashtakala.util.Language
import com.example.kashtakala.util.TranslationHelper
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
    val currentLang by sharedViewModel.selectedLanguage.collectAsState()

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        TranslationHelper.getString("estimator_title", currentLang),
                        color = Amber, fontSize = 22.sp, fontWeight = FontWeight.Bold
                    )
                    Text(
                        TranslationHelper.getString("estimator_subtitle", currentLang),
                        color = WoodLight, fontSize = 13.sp
                    )
                }
                LanguageSelector(sharedViewModel = sharedViewModel)
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
                        TranslationHelper.getString("estimator_dimensions_title", currentLang),
                        fontWeight = FontWeight.Bold, color = WoodDark, fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val lengthLabel = TranslationHelper.getString("estimator_label_length", currentLang)
                        val widthLabel = TranslationHelper.getString("estimator_label_width", currentLang)
                        val heightLabel = TranslationHelper.getString("estimator_label_height", currentLang)

                        DimensionField(lengthLabel, length, currentLang, Modifier.weight(1f)) { length = it }
                        DimensionField(widthLabel,  width,  currentLang, Modifier.weight(1f)) { width  = it }
                        DimensionField(heightLabel, height, currentLang, Modifier.weight(1f)) { height = it }
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
                        TranslationHelper.getString("estimator_wood_type", currentLang),
                        fontWeight = FontWeight.Bold, color = WoodDark, fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        val localizedSelectedWood = when(selectedWood.lowercase()) {
                            "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                            "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                            "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                            "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                            "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                            "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                            else -> selectedWood
                        }
                        OutlinedTextField(
                            value = localizedSelectedWood,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WoodMedium,
                                unfocusedBorderColor = WoodLight,
                                focusedTextColor = WoodDark,
                                unfocusedTextColor = WoodDark
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            WoodCalculator.woodTypes.forEach { wood ->
                                val localizedWoodName = when(wood.lowercase()) {
                                    "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                                    "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                                    "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                                    "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                                    "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                                    "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                                    else -> wood
                                }
                                val perSqFtText = when(currentLang) {
                                    Language.KANNADA -> "ಚದರ ಅಡಿಗೆ"
                                    Language.TELUGU -> "చదరపు అడుగుకి"
                                    Language.HINDI -> "प्रति वर्ग फिट"
                                    else -> "sq.ft"
                                }
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(localizedWoodName, fontWeight = FontWeight.Medium)
                                            Text(
                                                "₹${WoodCalculator.woodCostPerSqFt[wood]?.toInt()}/$perSqFtText",
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

            // Localized Error message
            val localizedError = when(errorMsg) {
                "Length must be a valid positive number" -> when(currentLang) {
                    Language.KANNADA -> "ಉದ್ದವು ಮಾನ್ಯವಾದ ಸಕಾರಾತ್ಮಕ ಸಂಖ್ಯೆಯಾಗಿರಬೇಕು"
                    Language.TELUGU -> "పొడవు సరైన ధన సంఖ్య అయి ఉండాలి"
                    Language.HINDI -> "लंबाई एक मान्य धनात्मक संख्या होनी चाहिए"
                    else -> errorMsg
                }
                "Width must be a valid positive number" -> when(currentLang) {
                    Language.KANNADA -> "ಅಗಲವು ಮಾನ್ಯವಾದ ಸಕಾರಾತ್ಮಕ ಸಂಖ್ಯೆಯಾಗಿರಬೇಕು"
                    Language.TELUGU -> "వెడల్పు సరైన ధన సంఖ్య అయి ఉండాలి"
                    Language.HINDI -> "चौड़ाई एक मान्य धनात्मक संख्या होनी चाहिए"
                    else -> errorMsg
                }
                "Height must be a valid positive number" -> when(currentLang) {
                    Language.KANNADA -> "ಎತ್ತರವು ಮಾನ್ಯವಾದ ಸಕಾರಾತ್ಮಕ ಸಂಖ್ಯೆಯಾಗಿರಬೇಕು"
                    Language.TELUGU -> "ఎత్తు సరైన ధన సంఖ్య అయి ఉండాలి"
                    Language.HINDI -> "ऊंचाई एक मान्य धनात्मक संख्या होनी चाहिए"
                    else -> errorMsg
                }
                else -> errorMsg
            }
            localizedError?.let {
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
                Text(
                    TranslationHelper.getString("estimator_btn_calculate", currentLang),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = Amber
                )
            }

            // Result card
            result?.let { r ->
                val localizedResultWood = when(r.woodType.lowercase()) {
                    "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                    "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                    "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                    "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                    "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                    "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                    else -> r.woodType
                }
                val sqftUnit = when(currentLang) {
                    Language.KANNADA -> "ಚದರ ಅಡಿ"
                    Language.TELUGU -> "చదరపు అడుగులు"
                    Language.HINDI -> "वर्ग फिट"
                    else -> "sq.ft"
                }
                val cuftUnit = when(currentLang) {
                    Language.KANNADA -> "ಘನ ಅಡಿ"
                    Language.TELUGU -> "ఘనపు అడుగులు"
                    Language.HINDI -> "घन फिट"
                    else -> "cu.ft"
                }

                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WoodDark),
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            TranslationHelper.getString("estimator_result_title", currentLang),
                            color = Amber, fontWeight = FontWeight.Bold, fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ResultRow(TranslationHelper.getString("estimator_result_wood", currentLang), localizedResultWood)
                        ResultRow(TranslationHelper.getString("estimator_result_area", currentLang), "%.2f $sqftUnit".format(r.areaSqFt))
                        ResultRow(TranslationHelper.getString("estimator_result_volume", currentLang), "%.2f $cuftUnit".format(r.volumeCuFt))
                        ResultRow(TranslationHelper.getString("estimator_result_cost", currentLang), "₹%,.0f".format(r.materialCost))
                        HorizontalDivider(
                            color = WoodLight,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                TranslationHelper.getString("estimator_result_tip_label", currentLang),
                                color = Amber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                TranslationHelper.getString("estimator_result_tip_text", currentLang),
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

fun formatToFtIn(value: String, lang: Language): String {
    val floatVal = value.toFloatOrNull() ?: return ""
    if (floatVal <= 0f) return ""
    val feet = floatVal.toInt()
    val inches = Math.round((floatVal - feet) * 12)
    val ftUnit = when(lang) {
        Language.KANNADA -> "ಅಡಿ"
        Language.TELUGU -> "అడుగులు"
        Language.HINDI -> "फिट"
        else -> "ft"
    }
    val inUnit = when(lang) {
        Language.KANNADA -> "ಇಂಚು"
        Language.TELUGU -> "అంగుళాలు"
        Language.HINDI -> "इंच"
        else -> "in"
    }
    return if (inches == 0) {
        "$feet $ftUnit"
    } else if (feet == 0) {
        "$inches $inUnit"
    } else {
        "$feet $ftUnit $inches $inUnit"
    }
}

@Composable
fun DimensionField(
    label: String,
    value: String,
    lang: Language,
    modifier: Modifier,
    onChange: (String) -> Unit
) {
    val ftInText = remember(value) { formatToFtIn(value, lang) }
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
                focusedTextColor = WoodDark,
                unfocusedTextColor = WoodDark
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