package ru.test.app.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import ru.test.app.models.Character
import ru.test.app.models.CharacterItem
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class HttpsClient {
    private var connection: HttpsURLConnection? = null
    private val gsonBuilder = GsonBuilder().create()

    fun getCharacters(url: URL): MutableList<CharacterItem>? {
        val listNames = mutableListOf<CharacterItem>()
        val jsonElement = getJson(url)
        jsonElement?.let {
            val jsonArray = jsonElement.asJsonArray
            jsonArray.forEach {
                val characterInfo = gsonBuilder.fromJson(
                    it, CharacterItem::class.java
                )
                listNames.add(characterInfo)
            }
        }
        return listNames
    }

    fun getCharacter(url: URL) : Character? {
        val jsonElement = getJson(url)
        return gsonBuilder.fromJson(jsonElement, Character::class.java)
    }

    private fun getJson(url: URL): JsonElement? {
        var jsonElement : JsonElement? = null
         try {
            setupConnection(url)?.getJsonElement { element ->
                jsonElement = element
            }
        } catch (e: Exception){
            return null
        } finally {
            connection?.closeConnect()
        }
        return jsonElement
    }

    private fun setupConnection(url: URL): HttpsURLConnection? {
        connection = (url.openConnection() as? HttpsURLConnection)
        connection?.run {
            setSettings()
            checkConnect()
            connect()
        }
        return connection
    }
}

fun HttpsURLConnection.closeConnect() {
    this.inputStream?.close()
    this.disconnect()
}

fun HttpsURLConnection.setSettings() {
    this.readTimeout = 3000
    this.connectTimeout = 3000
    this.requestMethod = "GET"
    this.doInput = true
}

fun HttpsURLConnection.checkConnect() {
    if (responseCode != HttpsURLConnection.HTTP_OK) {
        throw IOException("HTTP error code: $responseCode")
    }
}

fun HttpsURLConnection.getJsonElement(receiver: (JsonElement) -> Unit) {
    this.inputStream?.let {
        val jsonParser = JsonParser()
        val reader = InputStreamReader(inputStream)
        val jsonArray = jsonParser.parse(reader)
        receiver(jsonArray)
    }
}
