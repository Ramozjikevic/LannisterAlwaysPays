package ru.test.app.ui.recycler.listeners

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ru.test.app.data.Repository.Companion.PAGE_SIZE


abstract class PaginationListener
    (private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    private var isDownloadAgain = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
            firstVisibleItemPosition >= 0 &&
            totalItemCount >= PAGE_SIZE && isDownloadAgain
        ) {
            isDownloadAgain = false
            loadMoreItems()
        }

        if (visibleItemCount + firstVisibleItemPosition <= (totalItemCount - PAGE_SIZE / 6)) {
            isDownloadAgain = true
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}