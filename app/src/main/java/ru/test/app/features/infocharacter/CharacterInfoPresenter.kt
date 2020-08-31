package ru.test.app.features.infocharacter

import ru.test.app.utils.TaskRunner
import ru.test.app.data.Repository

class CharacterInfoPresenter(
    private val id: Long,
    private val view: CharacterInfoView
) {
    private val repository = Repository()

    init {
        getCharacter()
    }

    fun getCharacter() {
        TaskRunner.runWithIntValue(
            value = id.toInt(),
            onStartLoad = view::showLoader,
            request = repository::getCharacterInfo,
            onSuccess = view::showCharacterInformation,
            onError = view::showStub
        )
    }
}