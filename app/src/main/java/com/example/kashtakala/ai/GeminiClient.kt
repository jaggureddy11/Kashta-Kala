package com.example.kashtakala.ai

import com.example.kashtakala.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class GeminiClient(
    private val apiKey: String = BuildConfig.GEMINI_API_KEY,
    private val models: List<String> = listOf("gemini-2.5-flash", "gemini-2.0-flash")
) {
    suspend fun generate(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Add GEMINI_API_KEY to gradle.properties to enable Gemini.")
            )
        }

        val failures = mutableListOf<String>()
        for (model in models) {
            val response = requestModel(prompt, model)
            response
                .onSuccess { return@withContext Result.success(it) }
                .onFailure { failures += "$model: ${it.message}" }
        }

        Result.failure(
            IllegalStateException(
                "Gemini request failed for all configured models.\n${failures.joinToString("\n")}"
            )
        )
    }

    suspend fun chat(
        history: List<Pair<String, String>>,
        systemInstruction: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Add GEMINI_API_KEY to gradle.properties to enable Gemini.")
            )
        }

        val failures = mutableListOf<String>()
        for (model in models) {
            val response = requestChat(history, systemInstruction, model)
            response
                .onSuccess { return@withContext Result.success(it) }
                .onFailure { failures += "$model: ${it.message}" }
        }

        Result.failure(
            IllegalStateException(
                "Gemini request failed for all configured models.\n${failures.joinToString("\n")}"
            )
        )
    }

    private fun requestChat(
        history: List<Pair<String, String>>,
        systemInstruction: String?,
        model: String
    ): Result<String> = runCatching {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 20_000
            readTimeout = 30_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("x-goog-api-key", apiKey)
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(buildChatRequestBody(history, systemInstruction).toString())
        }

        val responseCode = connection.responseCode
        val responseText = if (responseCode in 200..299) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }

        connection.disconnect()

        if (responseCode !in 200..299) {
            error("HTTP $responseCode: ${extractErrorMessage(responseText)}")
        }

        parseText(responseText).ifBlank {
            "Gemini returned an empty response."
        }
    }

    private fun buildChatRequestBody(
        history: List<Pair<String, String>>,
        systemInstruction: String?
    ): JSONObject {
        val contentsArray = JSONArray()
        for ((role, text) in history) {
            val part = JSONObject().put("text", text)
            val content = JSONObject()
                .put("role", role)
                .put("parts", JSONArray().put(part))
            contentsArray.put(content)
        }

        val body = JSONObject()
            .put("contents", contentsArray)
            .put(
                "generationConfig",
                JSONObject()
                    .put("temperature", 0.7)
                    .put("maxOutputTokens", 1200)
            )

        if (!systemInstruction.isNullOrBlank()) {
            val systemPart = JSONObject().put("text", systemInstruction)
            val systemContent = JSONObject().put("parts", JSONArray().put(systemPart))
            body.put("systemInstruction", systemContent)
        }

        return body
    }

    private fun requestModel(prompt: String, model: String): Result<String> = runCatching {
        val url = URL("https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent")
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 20_000
            readTimeout = 30_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("x-goog-api-key", apiKey)
        }

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(buildRequestBody(prompt).toString())
        }

        val responseCode = connection.responseCode
        val responseText = if (responseCode in 200..299) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            connection.errorStream?.bufferedReader()?.use { it.readText() }.orEmpty()
        }

        connection.disconnect()

        if (responseCode !in 200..299) {
            error("HTTP $responseCode: ${extractErrorMessage(responseText)}")
        }

        parseText(responseText).ifBlank {
            "Gemini returned an empty response. Try adding more project details."
        }
    }

    private fun extractErrorMessage(responseText: String): String {
        if (responseText.isBlank()) return "No error body"
        return runCatching {
            JSONObject(responseText)
                .optJSONObject("error")
                ?.optString("message")
                .orEmpty()
                .ifBlank { responseText }
        }.getOrDefault(responseText)
    }

    private fun buildRequestBody(prompt: String): JSONObject {
        val part = JSONObject().put("text", prompt)
        val content = JSONObject()
            .put("role", "user")
            .put("parts", JSONArray().put(part))

        return JSONObject()
            .put("contents", JSONArray().put(content))
            .put(
                "generationConfig",
                JSONObject()
                    .put("temperature", 0.7)
                    .put("maxOutputTokens", 1200)
            )
    }

    private fun parseText(responseText: String): String {
        val root = JSONObject(responseText)
        val candidates = root.optJSONArray("candidates") ?: return ""
        val first = candidates.optJSONObject(0) ?: return ""
        val parts = first
            .optJSONObject("content")
            ?.optJSONArray("parts")
            ?: return ""

        return buildString {
            for (index in 0 until parts.length()) {
                val text = parts.optJSONObject(index)?.optString("text").orEmpty()
                if (text.isNotBlank()) append(text)
            }
        }.trim()
    }
}
