package ru.test.app.data

import ru.test.app.models.CharacterItemUI
import ru.test.app.models.toUI
import ru.test.app.network.HttpsClient
import ru.test.app.data.mapper.serializeToMap
import java.net.URL

class Repository {
    private val httpsClient = HttpsClient()

    fun getListCharacters(page: Int): List<CharacterItemUI> {
        val url = URL("$URL?page=${page}&pageSize=$PAGE_SIZE")
        return httpsClient.getCharacters(url)?.toUI() ?: emptyList()
    }

    fun getCharacterInfo(id: Long): List<Pair<String, String>> {
        val url = URL("$URL/$id")
        return httpsClient.getCharacter(url).serializeToMap()
            .prepareMap()
            .toList()
    }

    companion object {
        const val PAGE_SIZE = 15
        const val URL = "https://www.anapioficeandfire.com/api/characters"
    }
}

fun Map<String, Any>.prepareMap(): Map<String, String> {
    val mutableMap = mutableMapOf<String, String>()

    this.forEach{ item ->
        when (val value = item.value) {
            is String -> if (value.isNotBlank()) {
                mutableMap[item.key.fixKeyValue()] = value
            }
            is List<*> -> if (value.isNotEmpty()) {
                val string= value.joinToString()
                if(string.isNotBlank()) mutableMap[item.key.fixKeyValue()] = string
            }
        }
    }
    return mutableMap
}

fun String.fixKeyValue(): String {
    return when(this) {
        "playedBy" -> "Played by"
        "tvSeries" -> "TV Series"
        "povBooks" -> "POV Books"
        else -> this.capitalize()
    }
}