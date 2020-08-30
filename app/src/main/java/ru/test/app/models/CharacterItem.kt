package ru.test.app.models

data class CharacterItem(
    val url: String = "",
    val name: String = "",
)

fun List<CharacterItem>.toUI(): List<CharacterItemUI> {
    val listCharacterItem = mutableListOf<CharacterItemUI>()
    this.map { characterInfo ->
        listCharacterItem.add(
            CharacterItemUI(
                id = characterInfo.url.getId()?.toLongOrNull(),
                name = if(characterInfo.name.isBlank()) "Unnamed" else characterInfo.name
            )
        )
    }
    return listCharacterItem
}

fun String.getId(): String? {
    return if (!this.isBlank()) {
        this.substringAfterLast("/")
    } else null
}