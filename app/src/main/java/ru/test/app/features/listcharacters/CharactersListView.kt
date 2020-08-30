package ru.test.app.features.listcharacters

import ru.test.app.models.CharacterItemUI


interface CharactersListView {
    fun showLoader()
    fun showCharacters(data: List<CharacterItemUI>)
    fun showConnectionError()
}