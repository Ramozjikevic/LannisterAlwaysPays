package ru.test.app.features.listcharacters

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_characters_list.*
import ru.test.app.R
import ru.test.app.extension.setGone
import ru.test.app.models.CharacterItemUI
import ru.test.app.ui.recycler.adapters.PaginationAdapter
import ru.test.app.ui.recycler.itemdecorators.CustomDividerItemDecorator
import ru.test.app.ui.recycler.listeners.PaginationListener
import ru.test.app.ui.recycler.listeners.PaginationListener.Companion.PAGE_START


class CharactersListActivity : AppCompatActivity(), CharactersListView,
    SwipeRefreshLayout.OnRefreshListener {

    private var currentPage = PAGE_START
    private var isLastPage = false
    private var isLoading = false

    private lateinit var paginationAdapter: PaginationAdapter
    private lateinit var presenter: CharactersListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_list)
        setupListCharacters()
        setupSwipeToRefresh()
        initPresenter()
    }

    override fun showConnectionError() {
        swipeRefresh.isRefreshing = false
        if (currentPage == PAGE_START) errorText.setGone(false) else paginationAdapter.addError()
    }

    override fun showLoader() {
        isLoading = true
        if (currentPage != PAGE_START) paginationAdapter.addLoading()
    }

    override fun onRefresh() {
        currentPage = PAGE_START
        isLastPage = false
        paginationAdapter.clear()
        presenter.getCharacters(currentPage)
    }

    override fun showCharacters(data: List<CharacterItemUI>) {
        isLoading = false
        errorText.setGone(true)
        if (currentPage != PAGE_START) paginationAdapter.removeLoading()
        currentPage++
        swipeRefresh.isRefreshing = false
        if(data.isEmpty()) isLastPage = true else paginationAdapter.addItems(data)
    }

    private fun setupListCharacters() {
        val drawableResource = ContextCompat.getDrawable(this, R.drawable.divider)
        val divider = drawableResource?.let { CustomDividerItemDecorator(it) }

        val linearLayoutManager = LinearLayoutManager(this)
        paginationAdapter = PaginationAdapter {
            presenter.getCharacters(currentPage)
        }

        with(listCharacters) {
            layoutManager = linearLayoutManager
            adapter = paginationAdapter
            setHasFixedSize(true)
            divider?.let { addItemDecoration(divider) }

            addOnScrollListener(object : PaginationListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    val requestPage = currentPage + 1
                    presenter.getCharacters(requestPage)
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }
            })
        }
    }

    private fun setupSwipeToRefresh() {
        swipeRefresh.setOnRefreshListener(this)
    }

    private fun initPresenter() {
        presenter = CharactersListPresenter(this)
    }
}