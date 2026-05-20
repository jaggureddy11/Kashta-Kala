package com.example.kashtakala.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.ai.GeminiClient
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodDark
import com.example.kashtakala.ui.catalog.WoodLight
import com.example.kashtakala.ui.catalog.WoodMedium
import kotlinx.coroutines.launch
import kotlin.random.Random

private enum class AiTool(val label: String, val action: String) {
    Design("Design Guidance", "Create Design Plan"),
    Material("Material Recommendation", "Recommend Wood"),
    Negotiation("Price Negotiation", "Create Strategy"),
    Quote("Quote Description", "Write Description")
}

@Composable
fun AiToolsScreen() {
    val scope = rememberCoroutineScope()
    val geminiClient = remember { GeminiClient() }

    var selectedTool by remember { mutableStateOf(AiTool.Design) }
    var roomDimensions by remember { mutableStateOf("5x4 feet bedroom") }
    var stylePreference by remember { mutableStateOf("modern, space saving") }
    var budget by remember { mutableStateOf("25000") }
    var durabilityNeed by remember { mutableStateOf("high durability") }
    var region by remember { mutableStateOf("Bengaluru") }
    var customerProfile by remember { mutableStateOf("middle-income apartment owner") }
    var quoteDetails by remember {
        mutableStateOf("Queen bed, 5x6 ft, teak finish, material Rs 18000, labour Rs 7000")
    }
    var result by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .verticalScroll(rememberScrollState())
        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(WoodDark)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    "Gemini AI Workshop",
                    color = Amber,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Build guidance, material advice, pricing and quote writing",
                    color = WoodLight,
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Choose AI task",
                        color = WoodDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        AiTool.entries.chunked(2).forEach { rowTools ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowTools.forEach { tool ->
                                    FilterChip(
                                        selected = selectedTool == tool,
                                        onClick = {
                                            selectedTool = tool
                                            result = ""
                                            error = null
                                        },
                                        label = { Text(tool.label, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = WoodDark,
                                            selectedLabelColor = Amber
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        inputTitle(selectedTool),
                        color = WoodDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    when (selectedTool) {
                        AiTool.Design -> {
                            AiField("Room dimensions", roomDimensions) { roomDimensions = it }
                            AiField("Style preference", stylePreference) { stylePreference = it }
                            AiField("Budget (Rs)", budget, KeyboardType.Number) { budget = it }
                        }
                        AiTool.Material -> {
                            AiField("Budget (Rs)", budget, KeyboardType.Number) { budget = it }
                            AiField("Durability need", durabilityNeed) { durabilityNeed = it }
                            AiField("Aesthetic preference", stylePreference) { stylePreference = it }
                        }
                        AiTool.Negotiation -> {
                            AiField("Region", region) { region = it }
                            AiField("Customer profile", customerProfile) { customerProfile = it }
                            AiField("Project and quoted price", quoteDetails) { quoteDetails = it }
                        }
                        AiTool.Quote -> {
                            AiField("Technical quote details", quoteDetails) { quoteDetails = it }
                            AiField("Customer style preference", stylePreference) { stylePreference = it }
                        }
                    }
                    Button(
                        onClick = {
                            isLoading = true
                            error = null
                            result = ""
                            scope.launch {
                                val reliableSuggestion = buildOfflineFallback(
                                    selectedTool,
                                    roomDimensions,
                                    stylePreference,
                                    budget,
                                    durabilityNeed,
                                    region,
                                    customerProfile,
                                    quoteDetails
                                )
                                val prompt = buildPrompt(
                                    selectedTool,
                                    roomDimensions,
                                    stylePreference,
                                    budget,
                                    durabilityNeed,
                                    region,
                                    customerProfile,
                                    quoteDetails
                                )
                                geminiClient.generate(prompt)
                                    .onSuccess {
                                        result = if (isUsefulSuggestion(selectedTool, it)) {
                                            it
                                        } else {
                                            reliableSuggestion
                                        }
                                    }
                                    .onFailure {
                                        result = reliableSuggestion
                                        error = null
                                    }
                                isLoading = false
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WoodDark),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Amber,
                                strokeWidth = 2.dp,
                                modifier = Modifier.height(22.dp)
                            )
                        } else {
                            Text(
                                selectedTool.action,
                                color = Amber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            if (result.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = WoodDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Design suggestion",
                            color = Amber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(result, color = Color.White, fontSize = 13.sp, lineHeight = 19.sp)
                    }
                }
            }
        }
        }
    }
}

@Composable
private fun AiField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label, fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = false,
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        ),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WoodMedium,
            unfocusedBorderColor = WoodLight,
            focusedLabelColor = WoodMedium
        )
    )
}

private fun inputTitle(tool: AiTool): String = when (tool) {
    AiTool.Design -> "Room and style"
    AiTool.Material -> "Wood selection needs"
    AiTool.Negotiation -> "Pricing context"
    AiTool.Quote -> "Quote details"
}

private fun buildPrompt(
    tool: AiTool,
    roomDimensions: String,
    stylePreference: String,
    budget: String,
    durabilityNeed: String,
    region: String,
    customerProfile: String,
    quoteDetails: String
): String {
    val instruction = """
        You are Kashta-Kala's furniture design mentor for Indian carpenters.
        Give practical workshop-ready advice in concise bullets.
        Use rupees where prices are mentioned.
        Avoid inventing exact market rates; state assumptions when needed.
        Do not recommend items from an existing catalog.
        Do not start with greetings or generic assistant text.
    """.trimIndent()

    return when (tool) {
        AiTool.Design -> """
            $instruction
            Task: Create original furniture design guidance for a carpenter.
            Room dimensions: $roomDimensions
            Style preference: $stylePreference
            Budget: Rs $budget
            Return:
            1. Best product concept for this room and why it fits.
            2. Recommended outer dimensions in feet/inches, including walking clearance.
            3. Layout details: headboard/doors/drawers/shelves/storage placement as relevant.
            4. Suggested wood or board material, hardware, finish and budget trade-offs.
            5. Carpentry build notes: frame structure, joinery, panel thickness, support points and installation cautions.
            6. Two optional upgrades the carpenter can offer the customer.
        """.trimIndent()
        AiTool.Material -> """
            $instruction
            Task: Recommend the best wood type.
            Budget: Rs $budget
            Durability need: $durabilityNeed
            Aesthetic preference: $stylePreference
            Compare teak, sheesham, plywood and MDF with pros, cons and a final recommendation.
        """.trimIndent()
        AiTool.Negotiation -> """
            $instruction
            Task: Help a carpenter negotiate price.
            Region: $region
            Customer profile: $customerProfile
            Project and quoted price: $quoteDetails
            Return a fair pricing explanation, negotiation talking points, discount limits and upsell ideas.
        """.trimIndent()
        AiTool.Quote -> """
            $instruction
            Task: Convert technical quote data into a professional customer quote description.
            Technical details: $quoteDetails
            Customer style preference: $stylePreference
            Return a polished paragraph, included scope, value highlights and optional upgrade line.
        """.trimIndent()
    }
}

private fun buildOfflineFallback(
    tool: AiTool,
    roomDimensions: String,
    stylePreference: String,
    budget: String,
    durabilityNeed: String,
    region: String,
    customerProfile: String,
    quoteDetails: String
): String = when (tool) {
    AiTool.Design -> offlineDesignPlan(roomDimensions, stylePreference, budget)
    AiTool.Material -> offlineMaterialPlan(budget, durabilityNeed, stylePreference)
    AiTool.Negotiation -> offlineNegotiationPlan(region, customerProfile, quoteDetails)
    AiTool.Quote -> offlineQuoteDescription(quoteDetails, stylePreference)
}

private fun isUsefulSuggestion(tool: AiTool, text: String): Boolean {
    val normalized = text.lowercase()
    if (text.length < 350) return false
    if ("hello" in normalized || "i understand" in normalized || "as an ai" in normalized) return false

    return when (tool) {
        AiTool.Design -> listOf("concept", "dimension", "material", "carpentry").all { it in normalized }
        AiTool.Material -> listOf("teak", "plywood", "recommend").all { it in normalized }
        AiTool.Negotiation -> listOf("price", "customer", "discount").all { it in normalized }
        AiTool.Quote -> listOf("scope", "finish", "installation").any { it in normalized }
    }
}

private fun offlineDesignPlan(
    roomDimensions: String,
    stylePreference: String,
    budget: String
): String {
    val lowerRoom = roomDimensions.lowercase()
    val random = offlineRandom(roomDimensions, stylePreference, budget)
    val sizeNote = sizeCategory(lowerRoom)
    val concept = when {
        "bed" in lowerRoom || "bedroom" in lowerRoom -> random.pick(
            "hydraulic storage bed with a slim wall-mounted headboard",
            "single bed with pull-out drawer storage and side ledge",
            "foldable wall bed with overhead loft storage",
            "platform bed with open cubbies on one side"
        )
        "kitchen" in lowerRoom -> random.pick(
            "modular base cabinet with two drawers and one tall shutter",
            "L-shaped counter unit with overhead shutter cabinets",
            "compact pantry cabinet with bottle pull-out and open spice shelf",
            "sink-side storage unit with waterproof plywood carcass"
        )
        "study" in lowerRoom || "office" in lowerRoom -> random.pick(
            "wall-mounted study table with overhead shelves",
            "folding study desk with side book tower",
            "compact work desk with keyboard tray and closed files cabinet",
            "corner study unit with floating shelves"
        )
        "living" in lowerRoom || "hall" in lowerRoom -> random.pick(
            "floating TV unit with closed base storage",
            "low media console with vertical display shelves",
            "wall panel TV unit with hidden wire channel",
            "compact pooja-and-TV combined wall unit"
        )
        else -> random.pick(
            "multi-storage wall unit customized to the available wall",
            "compact cabinet with open display and closed lower storage",
            "folding utility unit with shallow shelves",
            "modular storage unit with removable shelves"
        )
    }

    val dimensions = when {
        "5x4" in lowerRoom || "5 x 4" in lowerRoom -> random.pick(
            "Use a 3.0 x 6.0 ft bed only if one side can stay against the wall; keep 18 in walking clearance near the entry.",
            "Prefer a 2.5-3.0 ft wide folding or storage unit on the longest wall; keep shutter swing within 15 in.",
            "Keep the furniture depth under 18 in for wall storage or choose a single bed with drawers opening toward the free side."
        )
        "6x6" in lowerRoom || "6 x 6" in lowerRoom -> random.pick(
            "Use a 4.0 x 6.0 ft main unit and keep 18-24 in movement space on one side.",
            "A 3.5 x 6.0 ft storage bed works better than a queen bed; keep overhead storage depth near 12 in.",
            "Use one full-height wall for storage and keep the center area open for movement."
        )
        "8x10" in lowerRoom || "10x8" in lowerRoom -> random.pick(
            "A 5.0 x 6.5 ft queen bed can fit with 24 in clearance on one side and a 15 in side table.",
            "Use a 6 ft wardrobe on the shorter wall and keep bed placement parallel to the longer wall.",
            "Plan a bed plus study ledge combination, keeping wardrobe depth at 21-24 in."
        )
        else -> random.pick(
            "Measure the longest usable wall and keep 18-24 in clear walking space around doors and windows.",
            "Keep tall storage on one wall only and avoid blocking window ventilation.",
            "Limit furniture depth to 18-24 in when the room is narrow; use vertical storage to save floor space."
        )
    }

    val material = random.pick(
        "18 mm BWP plywood for the main carcass with 1 mm laminate outside and 0.8 mm balancing laminate inside",
        "18 mm commercial plywood for dry areas with teak edge lipping on visible corners",
        "sheesham or teak only for exposed legs/front frame, with plywood panels to control cost",
        "pre-laminated plywood for faster delivery, plus solid-wood handles for a premium touch"
    )

    val buildNote = random.pick(
        "Make the base frame first, check diagonals, then fix side panels and storage partitions.",
        "Keep service gaps for hinges, channels and lift hardware before final lamination.",
        "Use confirmat screws or wood screws with adhesive for plywood joints, and clamp until set.",
        "Add hidden cross supports wherever span is more than 3 ft, especially under beds and shelves."
    )

    return """
        Best concept:
        - $concept.
        - This suits "$roomDimensions" with a "$stylePreference" look and keeps the design practical for a Rs $budget budget.
        - Room size reading: $sizeNote.

        Suggested dimensions and clearance:
        - $dimensions
        - Keep height visually light: 30-36 in for beds/tables, 7 ft maximum for wardrobes, and 10-12 in shelf depth for overhead storage.

        Layout details:
        - Use closed storage at the bottom for heavy items and open shelves only where quick access is needed.
        - For a bedroom, add box storage below the mattress, a 3-4 in recessed toe space, and a 2-3 in rounded or laminated edge for comfort.
        - For a wall unit, split the layout into base cabinet, display shelf, and overhead storage so the product does not look bulky.

        Material and finish:
        - $material.
        - Use 18 mm board for main panels, 12 mm for drawer bottoms/back panels, and laminate or polish based on the customer's style.
        - Matte walnut, natural teak, or light oak laminate works well for modern space-saving interiors.

        Carpentry build notes:
        - $buildNote.
        - Use screws plus adhesive for plywood joints; use dowel or mortise-tenon joints for exposed solid-wood members.
        - Add center support legs or cross members for beds wider than 4 ft.
        - Keep drawer channels, hinges, and lift-up hardware accessible for future service.

        Optional upgrades:
        - ${random.pick("Soft-close drawer channels", "hydraulic lift storage", "lockable drawer", "premium edge banding")}.
        - ${random.pick("Integrated warm LED strip", "hidden wire channel", "CNC-cut handle profile", "anti-termite coating")}.
    """.trimIndent()
}

private fun offlineMaterialPlan(
    budget: String,
    durabilityNeed: String,
    stylePreference: String
): String {
    val random = offlineRandom(budget, durabilityNeed, stylePreference)
    val bestChoice = random.pick(
        "18 mm plywood with laminate for the main body",
        "BWP plywood for moisture-prone areas and commercial plywood for dry storage",
        "sheesham visible frame with plywood inner carcass",
        "teak edge lipping with plywood panels for a premium-but-controlled budget"
    )

    return """
    Best choice:
    - Use $bestChoice for builds around Rs $budget.
    - Use teak or sheesham only for exposed borders, legs, handles or premium front frames.

    Comparison:
    - Teak: very durable and premium, but expensive.
    - Sheesham: strong, attractive grain, better value than teak for visible parts.
    - Plywood: best practical choice for cabinets, beds and storage carcasses.
    - MDF: smooth for painted shutters, but avoid it in damp or heavy-load areas.

    Recommendation:
    - For "$durabilityNeed" and "$stylePreference", ${random.pick(
        "choose plywood carcass with laminate finish and solid-wood edging where visible",
        "avoid MDF for load-bearing parts and use it only for painted decorative shutters",
        "use BWP plywood near walls exposed to dampness and commercial plywood for inner partitions",
        "spend extra on hardware and edge protection before upgrading every panel to solid wood"
    )}.
""".trimIndent()
}

private fun offlineNegotiationPlan(
    region: String,
    customerProfile: String,
    quoteDetails: String
): String {
    val random = offlineRandom(region, customerProfile, quoteDetails)
    return """
    Positioning:
    - Explain that the quote covers material, labour, hardware, finishing, transport and fitting risk in $region.
    - For $customerProfile, ${random.pick(
        "show one standard option and one upgrade option",
        "start with durability and service support before discussing discount",
        "offer scope choices instead of reducing the same specification",
        "explain the cost of hardware, finish and installation separately"
    )}.

    Talking points:
    - ${random.pick("Break down the price instead of only saying the final amount", "Compare basic, standard and premium options", "Mention measurement, delivery and fitting responsibilities clearly", "Keep a written scope so there is no confusion later")}.
    - Offer a small discount only by ${random.pick("reducing scope", "changing hardware", "simplifying finish", "removing optional lighting or premium handles")}.
    - Do not reduce structural quality, panel thickness, or support members.

    Project context:
    - $quoteDetails
""".trimIndent()
}

private fun offlineQuoteDescription(
    quoteDetails: String,
    stylePreference: String
): String {
    val random = offlineRandom(quoteDetails, stylePreference)
    val opening = random.pick(
        "We propose a custom furniture build designed around your \"$stylePreference\" preference",
        "This quote covers a made-to-measure furniture solution with a \"$stylePreference\" finish direction",
        "The proposed work focuses on a clean, durable furniture design suited to your \"$stylePreference\" requirement",
        "Our plan is to build a practical custom unit with the look and finish aligned to \"$stylePreference\""
    )

    return """
    $opening, using practical materials and reliable carpentry methods for long-term use. The work includes measurement-based fabrication, material preparation, assembly, finishing and final installation.

    Scope:
    - $quoteDetails
    - Includes core structure, visible finish, standard hardware and fitting.

    Value highlights:
    - Built to room measurements.
    - Designed for daily durability.
    - ${random.pick("Finish and storage details can be adjusted before final production", "Hardware and finish upgrades can be selected before production starts", "The final polish or laminate shade can be matched with room decor", "Storage partitions can be tuned after final site measurement")}.
""".trimIndent()
}

private fun offlineRandom(vararg inputs: String): Random {
    return Random(inputs.joinToString("|").hashCode())
}

private fun Random.pick(vararg options: String): String = options[nextInt(options.size)]

private fun sizeCategory(roomText: String): String = when {
    "5x4" in roomText || "5 x 4" in roomText || "4x5" in roomText || "4 x 5" in roomText ->
        "very compact room, so use wall-side placement, shallow storage and sliding/folding parts."
    "6x6" in roomText || "6 x 6" in roomText ->
        "small square room, so keep one wall free and avoid deep swing shutters."
    "8x10" in roomText || "10x8" in roomText || "8 x 10" in roomText || "10 x 8" in roomText ->
        "medium room, so a queen-size product and side storage can fit if walking clearance is planned."
    "10x12" in roomText || "12x10" in roomText || "10 x 12" in roomText || "12 x 10" in roomText ->
        "larger room, so add separate storage zones, side tables or a display unit without crowding movement."
    else ->
        "custom room size, so first measure the longest usable wall, door swing and window position before final cutting."
}
