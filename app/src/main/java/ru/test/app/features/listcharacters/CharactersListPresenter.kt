package ru.test.app.features.listcharacters

import ru.test.app.data.Repository
import ru.test.app.utils.TaskRunner
import ru.test.app.ui.recycler.listeners.PaginationListener

class CharactersListPresenter(
    private val view: CharactersListView
) {
    private val repository = Repository()

    init {
        getCharacters(PaginationListener.PAGE_START)
    }

    fun getCharacters(page: Int) {
        TaskRunner.run(
            onStartLoad = view::showLoader,
            onGetData = {repository.getListCharacters(page)},
            onSuccess = view::showCharacters,
            onError = view::showConnectionError
        )
    }
}