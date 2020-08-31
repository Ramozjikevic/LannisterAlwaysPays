package ru.test.app.utils

class Paginator<T>(
    private val requestFactory: (Int) -> List<T>,
    private val viewController: ViewController<T>
) {

    interface ViewController<T> {
        fun showEmptyProgress()
        fun hideEmptyProgress()
        fun showEmptyView()
        fun hideEmptyView()
        fun showEmptyError(error: Throwable)
        fun hideEmptyError()
        fun showPageLoadingProgress()
        fun hidePageLoadingProgress()
        fun hideData()
        fun showData(data: List<T>)
        fun showPageLoadingError(error: Throwable)
        fun hidePageLoadingError()
    }

    private val FIRST_PAGE = 1

    private var currentState: State<T> = EMPTY()
    private var currentPage = 0

    private interface State<T> {
        fun restart() {}
        fun loadNewPage() {}
        fun newData(data: List<T>) {}
        fun fail(error: Throwable) {}
    }

    fun restart() {
        currentState.restart()
    }

    fun loadNewPage() {
        currentState.loadNewPage()
    }

    private fun loadPage(page: Int) {
        TaskRunner.run(
            onGetData = { requestFactory.invoke(currentPage) },
            onSuccess = currentState::newData,
            onError2 = currentState::fail
        )
    }

    private inner class EMPTY : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }
    }

    private inner class EMPTY_PROGRESS : State<T> {

        override fun restart() {
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentPage = FIRST_PAGE
                viewController.hideEmptyProgress()
                viewController.showData(data)
            } else {
                currentState = EMPTY_DATA()
                viewController.hideEmptyProgress()
                viewController.showEmptyView()
            }
        }

        override fun fail(error: Throwable) {
            currentState = EMPTY_ERROR()
            viewController.hideEmptyProgress()
            viewController.showEmptyError(error)
        }
    }

    private inner class EMPTY_ERROR : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideEmptyError()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }
    }

    private inner class EMPTY_DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideEmptyView()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }
    }

    private inner class DATA : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.showPageLoadingProgress()
            loadPage(currentPage + 1)
        }
    }

    private inner class PAGE_PROGRESS : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hideData()
            viewController.hidePageLoadingProgress()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentPage++
                viewController.hidePageLoadingProgress()
                viewController.showData(data)
            } else {
                currentState = ALL_DATA()
                viewController.hidePageLoadingProgress()
            }
        }

        override fun fail(error: Throwable) {
            currentState = PAGE_ERROR()
            viewController.hidePageLoadingProgress()
            viewController.showPageLoadingError(error)
        }
    }

    private inner class PAGE_ERROR : State<T> {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.hidePageLoadingError()
            viewController.hideData()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.hidePageLoadingError()
            viewController.showPageLoadingProgress()
            loadPage(currentPage + 1)
        }
    }

    private inner class ALL_DATA : State<T> {
        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress()
            loadPage(FIRST_PAGE)
        }
    }
}