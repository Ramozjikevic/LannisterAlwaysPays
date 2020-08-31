package ru.test.app.features.listcharacters

import ru.test.app.models.CharacterItemUI


interface CharactersListView {

    fun showEmptyError()
    fun hideEmptyError()

    fun showRefreshing()
    fun hideRefreshing()

    fun showPageLoader()
    fun hidePageLoader()

    fun showPageLoadError()
    fun hidePageLoadError()

    fun showListCharacters(data: List<CharacterItemUI>)
    fun hideListCharacters()
}