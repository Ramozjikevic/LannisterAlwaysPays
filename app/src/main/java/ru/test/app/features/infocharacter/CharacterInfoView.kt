package ru.test.app.features.infocharacter


interface CharacterInfoView {
    fun showCharacterInformation(data: List<Pair<String, String>>)
    fun showLoader()
    fun showStub(e: Exception)
}