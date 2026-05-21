package com.example.kashtakala.ui.ai

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashtakala.ai.GeminiClient
import com.example.kashtakala.ui.catalog.Amber
import com.example.kashtakala.ui.SharedViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import androidx.compose.ui.text.input.ImeAction
import com.example.kashtakala.ui.catalog.Cream
import com.example.kashtakala.ui.catalog.WoodDark
import com.example.kashtakala.ui.catalog.WoodLight
import com.example.kashtakala.ui.catalog.WoodMedium
import com.example.kashtakala.ui.common.LanguageSelector
import com.example.kashtakala.util.Language
import com.example.kashtakala.util.TranslationHelper
import kotlinx.coroutines.launch
import kotlin.random.Random

private enum class AiTool(val labelKey: String, val actionKey: String) {
    Design("ai_task_design", "ai_btn_action_design"),
    Material("ai_task_material", "ai_btn_action_material"),
    Negotiation("ai_task_negotiation", "ai_btn_action_negotiation"),
    Quote("ai_task_quote", "ai_btn_action_quote"),
    Chat("ai_task_chat", "ai_btn_action_chat")
}

@Composable
fun AiToolsScreen(sharedViewModel: SharedViewModel) {
    val scope = rememberCoroutineScope()
    val geminiClient = remember { GeminiClient() }
    val context = LocalContext.current
    val currentLang by sharedViewModel.selectedLanguage.collectAsState()

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        TranslationHelper.getString("ai_title", currentLang),
                        color = Amber,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        TranslationHelper.getString("ai_subtitle", currentLang),
                        color = WoodLight,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                LanguageSelector(sharedViewModel = sharedViewModel)
            }
        }

        if (selectedTool == AiTool.Chat) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card with Chips
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            TranslationHelper.getString("ai_choose_task", currentLang),
                            color = WoodDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.height(8.dp))
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
                                            label = { Text(TranslationHelper.getString(tool.labelKey, currentLang), fontSize = 11.sp) },
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
                        }
                    }
                }

                // AI Chat Card holding history and input
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    AiChatInterface(sharedViewModel, geminiClient, scope, currentLang)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
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
                            TranslationHelper.getString("ai_choose_task", currentLang),
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
                                            label = { Text(TranslationHelper.getString(tool.labelKey, currentLang), fontSize = 11.sp) },
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
                            inputTitle(selectedTool, currentLang),
                            color = WoodDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        when (selectedTool) {
                            AiTool.Design -> {
                                AiField(TranslationHelper.getString("ai_input_room", currentLang), roomDimensions) { roomDimensions = it }
                                AiField(TranslationHelper.getString("ai_input_style", currentLang), stylePreference) { stylePreference = it }
                                AiField(TranslationHelper.getString("ai_input_budget", currentLang), budget, KeyboardType.Number) { budget = it }
                            }
                            AiTool.Material -> {
                                AiField(TranslationHelper.getString("ai_input_budget", currentLang), budget, KeyboardType.Number) { budget = it }
                                AiField(TranslationHelper.getString("ai_input_durability", currentLang), durabilityNeed) { durabilityNeed = it }
                                AiField(TranslationHelper.getString("ai_input_style", currentLang), stylePreference) { stylePreference = it }
                            }
                            AiTool.Negotiation -> {
                                AiField(TranslationHelper.getString("ai_input_region", currentLang), region) { region = it }
                                AiField(TranslationHelper.getString("ai_input_customer", currentLang), customerProfile) { customerProfile = it }
                                val projectAndQuotedPriceLabel = when(currentLang) {
                                    Language.KANNADA -> "ಯೋಜನೆ ಮತ್ತು ಕೋಟ್ ಮಾಡಿದ ಬೆಲೆ"
                                    Language.TELUGU -> "ప్రాజెక్ట్ మరియు కోట్ చేసిన ధర"
                                    Language.HINDI -> "परियोजना और उद्धृत मूल्य"
                                    else -> "Project and quoted price"
                                }
                                AiField(projectAndQuotedPriceLabel, quoteDetails) { quoteDetails = it }
                            }
                            AiTool.Quote -> {
                                AiField(TranslationHelper.getString("ai_input_quote", currentLang), quoteDetails) { quoteDetails = it }
                                val customerStylePrefLabel = when(currentLang) {
                                    Language.KANNADA -> "ಗ್ರಾಹಕರ ಶೈಲಿಯ ಆದ್ಯತೆ"
                                    Language.TELUGU -> "కస్టమర్ శైలి ప్రాధాన్యత"
                                    Language.HINDI -> "ग्राहक शैली प्राथमिकता"
                                    else -> "Customer style preference"
                                }
                                AiField(customerStylePrefLabel, stylePreference) { stylePreference = it }
                            }
                            else -> {}
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
                                    TranslationHelper.getString(selectedTool.actionKey, currentLang),
                                    color = Amber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                if (result.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, WoodLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    TranslationHelper.getString("ai_recommendation_title", currentLang),
                                    color = WoodDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    TextButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("AI Advice", result)
                                            clipboard.setPrimaryClip(clip)
                                        }
                                    ) {
                                        Text(
                                            TranslationHelper.getString("ai_btn_copy", currentLang),
                                            fontSize = 12.sp, color = WoodMedium, fontWeight = FontWeight.Bold
                                        )
                                    }
                                    TextButton(
                                        onClick = {
                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, result)
                                                type = "text/plain"
                                            }
                                            val shareIntent = Intent.createChooser(sendIntent, "Share advice via")
                                            context.startActivity(shareIntent)
                                        }
                                    ) {
                                        Text(
                                            TranslationHelper.getString("ai_btn_share", currentLang),
                                            fontSize = 12.sp, color = WoodMedium, fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = result,
                                color = WoodDark,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AiChatInterface(
    sharedViewModel: SharedViewModel,
    geminiClient: GeminiClient,
    scope: CoroutineScope,
    lang: Language
) {
    val messages by sharedViewModel.chatMessages.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val chatAssistantLabel = when(lang) {
        Language.KANNADA -> "ಕಾರ್ಪೆಂಟ್ರಿ ಸಹಾಯಕ"
        Language.TELUGU -> "కార్పెంట్రీ సహాయకుడు"
        Language.HINDI -> "बढ़ईगीरी सहायक"
        else -> "Carpentry Assistant"
    }

    val clearChatDesc = when(lang) {
        Language.KANNADA -> "ಚಾಟ್ ತೆರವುಗೊಳಿಸಿ"
        Language.TELUGU -> "చాట్ క్లియర్ చేయి"
        Language.HINDI -> "चैट साफ करें"
        else -> "Clear Chat"
    }

    val aiThinkingLabel = when(lang) {
        Language.KANNADA -> "AI ಯೋಚಿಸುತ್ತಿದೆ..."
        Language.TELUGU -> "AI ఆలోచిస్తోంది..."
        Language.HINDI -> "AI सोच रहा है..."
        else -> "AI is thinking..."
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(WoodLight.copy(alpha = 0.2f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                chatAssistantLabel,
                color = WoodDark,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            IconButton(
                onClick = { sharedViewModel.clearChat() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = clearChatDesc,
                    tint = WoodMedium
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
            if (isSending) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CircularProgressIndicator(
                            color = WoodMedium,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            aiThinkingLabel,
                            fontSize = 12.sp,
                            color = WoodMedium,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text(TranslationHelper.getString("ai_chat_placeholder", lang), fontSize = 13.sp) },
                singleLine = true,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                    onSend = {
                        if (inputText.isNotBlank() && !isSending) {
                            sendChat(inputText, sharedViewModel, geminiClient, scope, lang) {
                                isSending = it
                            }
                            inputText = ""
                        }
                    }
                ),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = WoodMedium,
                    unfocusedBorderColor = WoodLight,
                    focusedTextColor = WoodDark,
                    unfocusedTextColor = WoodDark,
                    cursorColor = WoodDark
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (inputText.isNotBlank() && !isSending) {
                        sendChat(inputText, sharedViewModel, geminiClient, scope, lang) {
                            isSending = it
                        }
                        inputText = ""
                    }
                },
                enabled = inputText.isNotBlank() && !isSending
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = TranslationHelper.getString("ai_btn_send", lang),
                    tint = if (inputText.isNotBlank() && !isSending) WoodDark else WoodLight
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: com.example.kashtakala.data.ChatMessage) {
    val isUser = message.sender == "user"
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val bg = if (isUser) WoodDark else Cream
    val textColors = if (isUser) Color.White else WoodDark
    val corners = if (isUser) {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 12.dp)
    }
    val border = if (isUser) null else BorderStroke(1.dp, WoodLight.copy(alpha = 0.5f))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = alignment
    ) {
        Card(
            shape = corners,
            colors = CardDefaults.cardColors(containerColor = bg),
            border = border,
            modifier = Modifier.widthIn(max = 280.dp),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = message.text,
                    color = textColors,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

private fun sendChat(
    text: String,
    sharedViewModel: SharedViewModel,
    geminiClient: GeminiClient,
    scope: CoroutineScope,
    lang: Language,
    setSending: (Boolean) -> Unit
) {
    setSending(true)
    sharedViewModel.addChatMessage("user", text)
    
    val systemInstruction = """
        You are the Kashta-Kala AI assistant, a dedicated mentor for Indian carpenters and users of the Kashta-Kala app.
        Your expertise is strictly limited to carpentry, woodworking, furniture building, wood types (like teak, sheesham, plywood, MDF), measurements, joinery, and the Kashta-Kala application features (such as Catalog, Estimator, Quotes, and Portfolio).
        
        CRITICAL RULE: You must refuse to discuss any topic unrelated to carpentry, woodworking, furniture, or the Kashta-Kala app.
        If the user asks an off-topic question (e.g. politics, coding, science, sports, cooking, history, general chit-chat not related to carpentry), politely decline and state that you are programmed to discuss only carpentry, woodworking, and the Kashta-Kala application.
        
        Keep responses practical, concise, and structured in a friendly, helpful workshop-style tone. Use Indian Rupees (₹) for pricing discussions.
    """.trimIndent()

    scope.launch {
        val history = sharedViewModel.chatMessages.value.map { msg ->
            msg.sender to msg.text
        }

        val result = geminiClient.chat(history, systemInstruction)
        result.onSuccess { reply ->
            sharedViewModel.addChatMessage("model", reply)
            setSending(false)
        }.onFailure { err ->
            val fallback = if (err.message?.contains("GEMINI_API_KEY") == true) {
                when(lang) {
                    Language.KANNADA -> "ಚಾಟ್ ಅನ್ನು ಸಕ್ರಿಯಗೊಳಿಸಲು gradle.properties ಅಥವಾ local.properties ಗೆ ಮಾನ್ಯವಾದ GEMINI_API_KEY ಅನ್ನು ಸೇರಿಸಿ. ಆಫ್‌ಲೈನ್ ಬ್ಯಾಕಪ್: ಮೇಲಿನ ಇತರ AI ಪರಿಕರಗಳ ಮೇಲೆ ಕ್ಲಿಕ್ ಮಾಡುವ ಮೂಲಕ ನೀವು ಮರದ ವೆಚ್ಚವನ್ನು ಅಂದಾಜು ಮಾಡಲು ಅಥವಾ ಕಾರ್ಪೆಂಟ್ರಿ ಕೋಟ್‌ಗಳನ್ನು ಸಿದ್ಧಪಡಿಸಲು ಸಹಾಯ ಪಡೆಯಬಹುದು!"
                    Language.TELUGU -> "చాట్‌ని ప్రారంభించడానికి gradle.properties లేదా local.properties కి సరైన GEMINI_API_KEYని జోడించండి. ఆఫ్‌లైన్ బ్యాకప్: పైన ఉన్న ఇతర AI టూల్స్ పై క్లిక్ చేయడం ద్వారా మీరు కలప ధరను అంచనా వేయడానికి లేదా ఆర్టీసియల్ కోట్‌లను సిద్ధం చేయడానికి సహాయం పొందవచ్చు!"
                    Language.HINDI -> "चैट सक्षम करने के लिए gradle.properties या local.properties में एक मान्य GEMINI_API_KEY जोड़ें। ऑफ़लाइन बैकअप: ऊपर दिए गए अन्य AI टूल पर क्लिक करके आप लकड़ी की लागत का अनुमान लगाने या बढ़ईगीरी कोट तैयार करने में सहायता प्राप्त कर सकते हैं!"
                    else -> "Add a valid GEMINI_API_KEY to gradle.properties or local.properties to enable chat. Offline fallback: I can help you estimate wood or draft carpentry quotes if you click on the other AI tools above!"
                }
            } else {
                when(lang) {
                    Language.KANNADA -> "ನನಗೆ ಸಂಪರ್ಕಿಸಲು ತೊಂದರೆಯಾಗುತ್ತಿದೆ. ದಯವಿಟ್ಟು ಇಂಟರ್ನೆಟ್ ಸಂಪರ್ಕವನ್ನು ಪರಿಶೀಲಿಸಿ. ಆಫ್‌ಲೈನ್ ಸಲಹೆ: ಬಾಳಿಕೆ ಬರುವ ಹೊರಾಂಗಣ ಪೀಠೋಪಕರಣಗಳಿಗಾಗಿ ತೇಗದ ಮರವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ, ಆದರೆ ಒಳಾಂಗಣ ಹಾಸಿಗೆಗಳು ಮತ್ತು ಕ್ಯಾಬಿನೆಟ್‌ಗಳಿಗೆ ಶೀಶಮ್ ಅತ್ಯುತ್ತಮವಾಗಿದೆ."
                    Language.TELUGU -> "నాకు కనెక్ట్ కావడంలో ఇబ్బందిగా ఉంది. దయచేసి ఇంటర్నెట్ కనెక్షన్‌ని తనిఖీ చేయండి. ఆఫ్‌లైన్ సలహా: మన్నికైన అవుట్‌డోర్ ఫర్నిచర్ కోసం టేకు కలపను సిఫార్సు చేస్తారు, అయితే ఇండోర్ బెడ్‌లు మరియు క్యాబినెట్‌ల కోసం శీశం కలప చాలా బాగుంటుంది."
                    Language.HINDI -> "मुझे कनेक्ट होने में परेशानी हो रही है। कृपया इंटरनेट कनेक्शन की जांच करें। ऑफ़लाइन सुझाव: टिकाऊ बाहरी फर्नीचर के लिए सागौन की लकड़ी की सिफारिश की जाती है, जबकि इनडोर बेड और अलमारियाँ के लिए शीशम उत्कृष्ट है।"
                    else -> "I am having trouble connecting right now. Please verify your internet connection. Offline suggestion: Teak wood (Sagan) is generally recommended for durable outdoor furniture, whereas Sheesham is excellent for indoor beds and cabinets."
                }
            }
            sharedViewModel.addChatMessage("model", fallback)
            setSending(false)
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
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = WoodMedium,
            unfocusedBorderColor = WoodLight,
            focusedLabelColor = WoodMedium,
            focusedTextColor = WoodDark,
            unfocusedTextColor = WoodDark
        )
    )
}

private fun inputTitle(tool: AiTool, lang: Language): String = when (tool) {
    AiTool.Design -> when(lang) {
        Language.KANNADA -> "ಕೊಠಡಿ ಮತ್ತು ಶೈಲಿ"
        Language.TELUGU -> "గది మరియు శైలి"
        Language.HINDI -> "कमरा और शैली"
        else -> "Room and style"
    }
    AiTool.Material -> when(lang) {
        Language.KANNADA -> "ಮರದ ಆಯ್ಕೆಯ ಅಗತ್ಯಗಳು"
        Language.TELUGU -> "కలప ఎంపిక అవసరాలు"
        Language.HINDI -> "लकड़ी चयन की आवश्यकताएं"
        else -> "Wood selection needs"
    }
    AiTool.Negotiation -> when(lang) {
        Language.KANNADA -> "ಬೆಲೆ ಸಂದರ್ಭ"
        Language.TELUGU -> "ధర సందర్భం"
        Language.HINDI -> "मूल्य निर्धारण संदर्भ"
        else -> "Pricing context"
    }
    AiTool.Quote -> when(lang) {
        Language.KANNADA -> "ಕೋಟ್ ವಿವರಗಳು"
        Language.TELUGU -> "కొటేషన్ వివరాలు"
        Language.HINDI -> "कोटेशन विवरण"
        else -> "Quote details"
    }
    else -> ""
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
        else -> ""
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
    else -> ""
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
        else -> false
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
