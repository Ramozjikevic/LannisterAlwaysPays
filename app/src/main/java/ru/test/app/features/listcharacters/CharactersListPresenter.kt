package ru.test.app.features.listcharacters

import ru.test.app.data.Repository
import ru.test.app.models.CharacterItemUI
import ru.test.app.utils.Paginator

class CharactersListPresenter(
    private val view: CharactersListView
) {
    private val repository = Repository()
    private var paginator: Paginator<CharacterItemUI> = Paginator(
        repository::getListCharacters,
        object : Paginator.ViewController<CharacterItemUI> {
            override fun showEmptyProgress() {
                view.showRefreshing()
            }

            override fun hideEmptyProgress() {
                view.hideRefreshing()
            }

            override fun showEmptyView() {
                view.showEmptyError()
            }

            override fun hideEmptyView() {
                view.hideEmptyError()
            }

            override fun showEmptyError(error: Throwable) {
                view.showEmptyError()
            }

            override fun hideEmptyError() {
                view.hideEmptyError()
            }

            override fun showPageLoadingProgress() {
                view.showPageLoader()
            }

            override fun hidePageLoadingProgress() {
                view.hidePageLoader()
            }

            override fun showData(data: List<CharacterItemUI>) {
                view.showListCharacters(data)
            }

            override fun hideData() {
                view.hideListCharacters()
            }

            override fun showPageLoadingError(error: Throwable) {
                view.showPageLoadError()
            }

            override fun hidePageLoadingError() {
                view.hidePageLoadError()
            }
        }
    )

    init {
        onRefresh()
    }

    fun onRefresh() = paginator.restart()

    fun getCharacters() = paginator.loadNewPage()

}