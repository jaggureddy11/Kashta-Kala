package com.example.kashtakala.ui.quote

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.kashtakala.ui.common.LanguageSelector
import com.example.kashtakala.util.Language
import com.example.kashtakala.util.TranslationHelper
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
    val currentLang by sharedViewModel.selectedLanguage.collectAsState()

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
            designName = TranslationHelper.getDesignName(design.id, currentLang)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            TranslationHelper.getString("quote_title", currentLang),
                            color = Amber, fontSize = 22.sp, fontWeight = FontWeight.Bold
                        )
                        Text(
                            TranslationHelper.getString("quote_subtitle", currentLang),
                            color = WoodLight, fontSize = 13.sp
                        )
                    }
                    LanguageSelector(sharedViewModel = sharedViewModel)
                }
            }

            // Segmented Tab Toggle Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(Cream, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { showSaved = false },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!showSaved) WoodDark else Color.Transparent,
                        contentColor = if (!showSaved) Amber else WoodMedium
                    ),
                    elevation = if (!showSaved) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        TranslationHelper.getString("quote_tab_new", currentLang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                val savedTabLabel = when(currentLang) {
                    Language.KANNADA -> "ಉಳಿಸಲಾಗಿದೆ (${savedQuotes.size})"
                    Language.TELUGU -> "భద్రపరచబడినవి (${savedQuotes.size})"
                    Language.HINDI -> "सहेजे गए (${savedQuotes.size})"
                    else -> "Saved (${savedQuotes.size})"
                }
                Button(
                    onClick = { showSaved = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (showSaved) WoodDark else Color.Transparent,
                        contentColor = if (showSaved) Amber else WoodMedium
                    ),
                    elevation = if (showSaved) ButtonDefaults.buttonElevation(defaultElevation = 2.dp) else null,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        savedTabLabel,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            if (!showSaved) {
                // ── NEW QUOTE FORM ──────────────────────────────────
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    QuoteCard(title = TranslationHelper.getString("quote_card_customer", currentLang)) {
                        QuoteField(TranslationHelper.getString("quote_label_customer_name", currentLang), customerName) { customerName = it }
                        Spacer(Modifier.height(8.dp))
                        QuoteField(TranslationHelper.getString("quote_label_item_name", currentLang), designName) { designName = it }
                    }

                    Spacer(Modifier.height(12.dp))

                    val lLabel = when(currentLang) {
                        Language.KANNADA -> "ಉದ್ದ (ಅಡಿ)"
                        Language.TELUGU -> "పొడవు (అడుగులు)"
                        Language.HINDI -> "लंबाई (फिट)"
                        else -> "L (ft)"
                    }
                    val wLabel = when(currentLang) {
                        Language.KANNADA -> "ಅಗಲ (ಅಡಿ)"
                        Language.TELUGU -> "వెడల్పు (అడుగులు)"
                        Language.HINDI -> "चौड़ाई (फिट)"
                        else -> "W (ft)"
                    }
                    val hLabel = when(currentLang) {
                        Language.KANNADA -> "ಎತ್ತರ (ಅಡಿ)"
                        Language.TELUGU -> "ఎత్తు (అడుగులు)"
                        Language.HINDI -> "ऊंचाई (फिट)"
                        else -> "H (ft)"
                    }

                    QuoteCard(title = TranslationHelper.getString("quote_card_dimensions", currentLang)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            QuoteField(lLabel, length, Modifier.weight(1f), KeyboardType.Decimal) { length = it }
                            QuoteField(wLabel, width,  Modifier.weight(1f), KeyboardType.Decimal) { width  = it }
                            QuoteField(hLabel, height, Modifier.weight(1f), KeyboardType.Decimal) { height = it }
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            TranslationHelper.getString("quote_label_selected_wood", currentLang),
                            fontSize = 12.sp, color = WoodMedium, fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(6.dp))
                        // Wood type chips (Grid style)
                        val chunks = remember { WoodCalculator.woodTypes.chunked(3) }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            chunks.forEach { row ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    row.forEach { w ->
                                        val localizedWood = when(w.lowercase()) {
                                            "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                                            "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                                            "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                                            "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                                            "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                                            "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                                            else -> w
                                        }
                                        PremiumWoodChip(
                                            selected = woodType == w,
                                            label = localizedWood,
                                            modifier = Modifier.weight(1f),
                                            onClick = { woodType = w }
                                        )
                                    }
                                    if (row.size < 3) {
                                        repeat(3 - row.size) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    QuoteCard(title = TranslationHelper.getString("quote_card_cost", currentLang)) {
                        QuoteField(TranslationHelper.getString("quote_label_labour", currentLang), labourCost, keyboardType = KeyboardType.Decimal) { labourCost = it }
                        Spacer(Modifier.height(8.dp))
                        QuoteField(TranslationHelper.getString("quote_label_overhead", currentLang), overheadPct, keyboardType = KeyboardType.Decimal) { overheadPct = it }
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = WoodLight)
                        Spacer(Modifier.height(8.dp))
                        QuoteSummaryRow(TranslationHelper.getString("quote_summary_material", currentLang), "₹%,.0f".format(matCost))
                        QuoteSummaryRow(TranslationHelper.getString("quote_summary_labour", currentLang), "₹%,.0f".format(labour))
                        QuoteSummaryRow(TranslationHelper.getString("quote_summary_overhead", currentLang), "₹%,.0f".format((matCost + labour) * overhead / 100))
                        HorizontalDivider(color = WoodLight, modifier = Modifier.padding(vertical = 6.dp))
                        QuoteSummaryRow(TranslationHelper.getString("quote_summary_total", currentLang), "₹%,.0f".format(total), bold = true)
                    }

                    Spacer(Modifier.height(16.dp))

                    // Buttons Row for Share & Save
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val errName = when(currentLang) {
                            Language.KANNADA -> "ದಯವಿಟ್ಟು ಗ್ರಾಹಕರ ಹೆಸರನ್ನು ನಮೂದಿಸಿ"
                            Language.TELUGU -> "దయచేసి కస్టమర్ పేరు నమోదు చేయండి"
                            Language.HINDI -> "कृपया ग्राहक का नाम दर्ज करें"
                            else -> "Please enter customer name"
                        }
                        val errDim = when(currentLang) {
                            Language.KANNADA -> "ದಯವಿಟ್ಟು ಮಾನ್ಯವಾದ ಅಳತೆಗಳನ್ನು ನಮೂದಿಸಿ"
                            Language.TELUGU -> "దయచేసి సరైన కొలతలు నమోదు చేయండి"
                            Language.HINDI -> "कृपया मान्य आयाम दर्ज करें"
                            else -> "Please enter valid dimensions"
                        }

                        Button(
                            onClick = {
                                if (customerName.isBlank()) {
                                    snackMsg = errName
                                    return@Button
                                }
                                if (area <= 0f) {
                                    snackMsg = errDim
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
                                    total = total,
                                    lang = currentLang
                                )
                                shareQuoteText(context, text)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WoodMedium),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    TranslationHelper.getString("quote_btn_share", currentLang),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold, color = Color.White
                                )
                            }
                        }

                        Button(
                            onClick = {
                                if (customerName.isBlank()) {
                                    snackMsg = errName
                                    return@Button
                                }
                                if (area <= 0f) {
                                    snackMsg = errDim
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
                                    val successMsg = when(currentLang) {
                                        Language.KANNADA -> "✅ $customerName ಗಾಗಿ ಕೋಟ್ ಉಳಿಸಲಾಗಿದೆ!"
                                        Language.TELUGU -> "✅ $customerName కోసం కొటేషన్ భద్రపరచబడింది!"
                                        Language.HINDI -> "✅ $customerName के लिए कोट सहेज लिया गया है!"
                                        else -> "✅ Quote saved for $customerName!"
                                    }
                                    snackMsg    = successMsg
                                    customerName = ""; designName = ""
                                    length = ""; width = ""; height = ""
                                    labourCost = ""; overheadPct = "10"
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WoodDark),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Done,
                                    contentDescription = null,
                                    tint = Amber,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    TranslationHelper.getString("quote_btn_save", currentLang),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold, color = Amber
                                )
                            }
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
                        val noSavedText = when(currentLang) {
                            Language.KANNADA -> "ಇನ್ನೂ ಯಾವುದೇ ಕೋಟ್‌ಗಳು ಉಳಿಸಿಲ್ಲ.\nಹೊಸ ಕೋಟ್ ಟ್ಯಾಬ್‌ನಲ್ಲಿ ಒಂದನ್ನು ರಚಿಸಿ."
                            Language.TELUGU -> "ఇంకా కొటేషన్లు ఏవీ భద్రపరచలేదు.\nకొత్త కొటేషన్ ట్యాబ్‌లో ఒకదాన్ని సృష్టించండి."
                            Language.HINDI -> "अभी तक कोई कोट सहेजा नहीं गया है।\nनया कोट टैब में एक बनाएं।"
                            else -> "No saved quotes yet.\nCreate one in New Quote tab."
                        }
                        Text(
                            noSavedText,
                            color = WoodMedium, fontSize = 14.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
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
                                currentLang = currentLang,
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
    total: Float,
    lang: Language
): String {
    val woodName = when(woodType.lowercase()) {
        "teak" -> TranslationHelper.getString("wood_teak", lang)
        "sheesham" -> TranslationHelper.getString("wood_sheesham", lang)
        "plywood" -> TranslationHelper.getString("wood_plywood", lang)
        "mdf" -> TranslationHelper.getString("wood_mdf", lang)
        "rosewood" -> TranslationHelper.getString("wood_rosewood", lang)
        "mango" -> TranslationHelper.getString("wood_mango", lang)
        else -> woodType
    }
    val header = when(lang) {
        Language.KANNADA -> "🪵 *ಕಷ್ಟ-ಕಲಾ ಬೆಲೆ ಕೋಟ್* 🪵"
        Language.TELUGU -> "🪵 *కష్ట-కల ధర కొటేషన్* 🪵"
        Language.HINDI -> "🪵 *कष्ट-कला मूल्य कोट* 🪵"
        else -> "🪵 *KASHTA-KALA PRICE QUOTE* 🪵"
    }
    val custLabel = when(lang) {
        Language.KANNADA -> "ಗ್ರಾಹಕರ ಹೆಸರು"
        Language.TELUGU -> "కస్టమర్ పేరు"
        Language.HINDI -> "ग्राहक का नाम"
        else -> "Customer Name"
    }
    val itemLabel = when(lang) {
        Language.KANNADA -> "ವಸ್ತು/ವಿನ್ಯಾಸ"
        Language.TELUGU -> "వస్తువు/డిజైన్"
        Language.HINDI -> "वस्तु/डिज़ाइन"
        else -> "Item Name"
    }
    val woodLabel = when(lang) {
        Language.KANNADA -> "ಮರದ ವಿಧ"
        Language.TELUGU -> "కలప రకం"
        Language.HINDI -> "लकड़ी का प्रकार"
        else -> "Wood Type"
    }
    val dimLabel = when(lang) {
        Language.KANNADA -> "ಅಳತೆಗಳು"
        Language.TELUGU -> "కొలతలు"
        Language.HINDI -> "आयाम"
        else -> "Dimensions"
    }
    val costLabel = when(lang) {
        Language.KANNADA -> "ವೆಚ್ಚದ ವಿವರ"
        Language.TELUGU -> "ధర వివరాలు"
        Language.HINDI -> "लागत विवरण"
        else -> "Cost Breakdown"
    }
    val matLabel = when(lang) {
        Language.KANNADA -> "ಮರ ಮತ್ತು ಸಾಮಗ್ರಿ ವೆಚ್ಚ"
        Language.TELUGU -> "కలప & సామగ్రి ఖర్చు"
        Language.HINDI -> "लकड़ी और सामग्री लागत"
        else -> "Material Cost"
    }
    val labLabel = when(lang) {
        Language.KANNADA -> "ಕೂಲಿ ವೆಚ್ಚ"
        Language.TELUGU -> "కూలి ఖర్చు"
        Language.HINDI -> "श्रम लागत"
        else -> "Labour Cost"
    }
    val overLabel = when(lang) {
        Language.KANNADA -> "ಇತರ ವೆಚ್ಚಗಳು"
        Language.TELUGU -> "ఇతర ఖర్చులు"
        Language.HINDI -> "अन्य व्यय"
        else -> "Overhead / Misc"
    }
    val totLabel = when(lang) {
        Language.KANNADA -> "ಒಟ್ಟು ಅಂದಾಜು"
        Language.TELUGU -> "మొత్తం అంచనా"
        Language.HINDI -> "कुल अनुमान"
        else -> "ESTIMATED TOTAL"
    }
    val footer = when(lang) {
        Language.KANNADA -> "_ಕಷ್ಟ-ಕಲಾ ಆಯ್ಕೆ ಮಾಡಿದ್ದಕ್ಕಾಗಿ ಧನ್ಯವಾದಗಳು!_"
        Language.TELUGU -> "_కష్ట-కల ఎంచుకున్నందుకు ధన్యవాదాలు!_"
        Language.HINDI -> "_कष्ट-कला चुनने के लिए धन्यवाद!_"
        else -> "_Thank you for choosing Kashta-Kala!_"
    }
    val ftUnit = when(lang) {
        Language.KANNADA -> "ಅಡಿ"
        Language.TELUGU -> "అడుగులు"
        Language.HINDI -> "फिट"
        else -> "ft"
    }

    return """
        $header
        ------------------------------------
        *$custLabel:* $customerName
        *$itemLabel:* $designName
        *$woodLabel:* $woodName
        *$dimLabel:* $length $ftUnit x $width $ftUnit x $height $ftUnit
        
        *$costLabel:*
        - $matLabel: ₹${"%,.0f".format(matCost)}
        - $labLabel: ₹${"%,.0f".format(labour)}
        - $overLabel: ₹${"%,.0f".format((matCost + labour) * overhead / 100)}
        ------------------------------------
        *$totLabel: ₹${"%,.0f".format(total)}*
        
        $footer
    """.trimIndent()
}

fun formatSavedQuoteForSharing(quote: SavedQuote, lang: Language): String {
    val woodName = when(quote.woodType.lowercase()) {
        "teak" -> TranslationHelper.getString("wood_teak", lang)
        "sheesham" -> TranslationHelper.getString("wood_sheesham", lang)
        "plywood" -> TranslationHelper.getString("wood_plywood", lang)
        "mdf" -> TranslationHelper.getString("wood_mdf", lang)
        "rosewood" -> TranslationHelper.getString("wood_rosewood", lang)
        "mango" -> TranslationHelper.getString("wood_mango", lang)
        else -> quote.woodType
    }
    val header = when(lang) {
        Language.KANNADA -> "🪵 *ಕಷ್ಟ-ಕಲಾ ಬೆಲೆ ಕೋಟ್* 🪵"
        Language.TELUGU -> "🪵 *కష్ట-కల ధర కొటేషన్* 🪵"
        Language.HINDI -> "🪵 *कष्ट-कला मूल्य कोट* 🪵"
        else -> "🪵 *KASHTA-KALA PRICE QUOTE* 🪵"
    }
    val custLabel = when(lang) {
        Language.KANNADA -> "ಗ್ರಾಹಕರ ಹೆಸರು"
        Language.TELUGU -> "కస్టమర్ పేరు"
        Language.HINDI -> "ग्राहक का नाम"
        else -> "Customer Name"
    }
    val itemLabel = when(lang) {
        Language.KANNADA -> "ವಸ್ತು/ವಿನ್ಯಾಸ"
        Language.TELUGU -> "వస్తువు/డిజైన్"
        Language.HINDI -> "वस्तु/डिज़ाइन"
        else -> "Item/Design"
    }
    val woodLabel = when(lang) {
        Language.KANNADA -> "ಮರದ ವಿಧ"
        Language.TELUGU -> "కలప రకం"
        Language.HINDI -> "लकड़ी का प्रकार"
        else -> "Wood Type"
    }
    val dimLabel = when(lang) {
        Language.KANNADA -> "ಅಳತೆಗಳು"
        Language.TELUGU -> "కొలతలు"
        Language.HINDI -> "आयाम"
        else -> "Dimensions"
    }
    val costLabel = when(lang) {
        Language.KANNADA -> "ವೆಚ್ಚದ ವಿವರ"
        Language.TELUGU -> "ధర వివరాలు"
        Language.HINDI -> "लागत विवरण"
        else -> "Cost Breakdown"
    }
    val matLabel = when(lang) {
        Language.KANNADA -> "ಮರ ಮತ್ತು ಸಾಮಗ್ರಿ ವೆಚ್ಚ"
        Language.TELUGU -> "కలప & సామగ్రి ఖర్చు"
        Language.HINDI -> "लकड़ी और सामग्री लागत"
        else -> "Material Cost"
    }
    val labLabel = when(lang) {
        Language.KANNADA -> "ಕೂಲಿ ವೆಚ್ಚ"
        Language.TELUGU -> "కూలి ఖర్చు"
        Language.HINDI -> "श्रम लागत"
        else -> "Labour Cost"
    }
    val overLabel = when(lang) {
        Language.KANNADA -> "ಇತರ ವೆಚ್ಚಗಳು"
        Language.TELUGU -> "ఇతర ఖర్చులు"
        Language.HINDI -> "अन्य व्यय"
        else -> "Overhead / Misc"
    }
    val totLabel = when(lang) {
        Language.KANNADA -> "ಒಟ್ಟು ಅಂದಾಜು"
        Language.TELUGU -> "మొత్తం అంచనా"
        Language.HINDI -> "कुल अनुमान"
        else -> "ESTIMATED TOTAL"
    }
    val footer = when(lang) {
        Language.KANNADA -> "_ಕಷ್ಟ-ಕಲಾ ಆಯ್ಕೆ ಮಾಡಿದ್ದಕ್ಕಾಗಿ ಧನ್ಯವಾದಗಳು!_"
        Language.TELUGU -> "_కష్ట-కల ఎంచుకున్నందుకు ధన్యవాదాలు!_"
        Language.HINDI -> "_कष्ट-कला चुनने के लिए धन्यवाद!_"
        else -> "_Thank you for choosing Kashta-Kala!_"
    }
    val ftUnit = when(lang) {
        Language.KANNADA -> "ಅಡಿ"
        Language.TELUGU -> "అడుగులు"
        Language.HINDI -> "फिट"
        else -> "ft"
    }

    return """
        $header
        ------------------------------------
        *$custLabel:* ${quote.customerName}
        *$itemLabel:* ${quote.designName}
        *$woodLabel:* $woodName
        *$dimLabel:* ${quote.lengthFt} $ftUnit x ${quote.widthFt} $ftUnit x ${quote.heightFt} $ftUnit
        
        *$costLabel:*
        - $matLabel: ₹${"%,.0f".format(quote.materialCost)}
        - $labLabel: ₹${"%,.0f".format(quote.labourCost)}
        - $overLabel: ₹${"%,.0f".format((quote.materialCost + quote.labourCost) * quote.overheadPercent / 100)}
        ------------------------------------
        *$totLabel: ₹${"%,.0f".format(quote.totalCost)}*
        
        $footer
    """.trimIndent()
}

@Composable
fun PremiumWoodChip(selected: Boolean, label: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (selected) Amber else WoodLight.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = if (selected) WoodDark else Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) Amber else WoodDark,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun QuoteCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, WoodLight.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(16.dp)
                        .background(Amber, RoundedCornerShape(2.dp))
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    color = WoodDark,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(12.dp))
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
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Amber,
            unfocusedBorderColor = WoodLight.copy(alpha = 0.5f),
            focusedLabelColor = Amber,
            unfocusedLabelColor = WoodMedium,
            focusedTextColor = WoodDark,
            unfocusedTextColor = WoodDark,
            focusedContainerColor = Cream.copy(alpha = 0.3f),
            unfocusedContainerColor = Cream.copy(alpha = 0.3f)
        )
    )
}

@Composable
fun QuoteSummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (bold) {
                    Modifier
                        .background(Amber.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                } else {
                    Modifier.padding(vertical = 4.dp)
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            color = if (bold) WoodDark else WoodMedium,
            fontSize = if (bold) 14.sp else 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            value,
            color = if (bold) WoodDark else WoodDark,
            fontSize = if (bold) 15.sp else 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SavedQuoteCard(quote: SavedQuote, context: Context, currentLang: Language, onDelete: () -> Unit) {
    val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(quote.dateCreated))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, WoodLight.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(16.dp)
                            .background(Amber, RoundedCornerShape(2.dp))
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(quote.customerName, fontWeight = FontWeight.Bold,
                        color = WoodDark, fontSize = 15.sp)
                }
                Text("₹%,.0f".format(quote.totalCost),
                    fontWeight = FontWeight.Bold, color = Amber, fontSize = 15.sp)
            }
            Spacer(Modifier.height(6.dp))
            val localizedWood = when(quote.woodType.lowercase()) {
                "teak" -> TranslationHelper.getString("wood_teak", currentLang)
                "sheesham" -> TranslationHelper.getString("wood_sheesham", currentLang)
                "plywood" -> TranslationHelper.getString("wood_plywood", currentLang)
                "mdf" -> TranslationHelper.getString("wood_mdf", currentLang)
                "rosewood" -> TranslationHelper.getString("wood_rosewood", currentLang)
                "mango" -> TranslationHelper.getString("wood_mango", currentLang)
                else -> quote.woodType
            }
            Text("${quote.designName} · $localizedWood",
                color = WoodMedium, fontSize = 13.sp, modifier = Modifier.padding(start = 12.dp))
            val ftUnit = when(currentLang) {
                Language.KANNADA -> "ಅಡಿ"
                Language.TELUGU -> "అడుగులు"
                Language.HINDI -> "फिट"
                else -> "ft"
            }
            Text("${quote.lengthFt}×${quote.widthFt}×${quote.heightFt} $ftUnit · $date",
                color = WoodLight, fontSize = 12.sp, modifier = Modifier.padding(start = 12.dp))
            Spacer(Modifier.height(8.dp))
            
            HorizontalDivider(color = Cream, modifier = Modifier.padding(vertical = 4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val shareLabel = when(currentLang) {
                    Language.KANNADA -> "ಕೋಟ್ ಹಂಚಿಕೊಳ್ಳಿ"
                    Language.TELUGU -> "కొటేషన్ షేర్ చేయండి"
                    Language.HINDI -> "कोट साझा करें"
                    else -> "Share Quote"
                }
                val deleteLabel = when(currentLang) {
                    Language.KANNADA -> "ಅಳಿಸಿ"
                    Language.TELUGU -> "తొలగించు"
                    Language.HINDI -> "हटाएं"
                    else -> "Delete"
                }

                TextButton(
                    onClick = {
                        val text = formatSavedQuoteForSharing(quote, currentLang)
                        shareQuoteText(context, text)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = WoodMedium
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text(shareLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text(deleteLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}