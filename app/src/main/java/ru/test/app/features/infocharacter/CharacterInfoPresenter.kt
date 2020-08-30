package ru.test.app.features.infocharacter

import ru.test.app.utils.TaskRunner
import ru.test.app.data.Repository

class CharacterInfoPresenter(
    private val id : Long,
    private val view: CharacterInfoView
) {
    private val repository = Repository()

    init {
        getCharacter()
    }

    fun getCharacter() {
        TaskRunner.run(
            onStartLoad = view::showLoader,
            onGetData = {repository.getCharacterInfo(id)},
            onSuccess = view::showCharacterInformation,
            onCanceled = view::showStub,
            onError = view::showStub
        )
    }
}