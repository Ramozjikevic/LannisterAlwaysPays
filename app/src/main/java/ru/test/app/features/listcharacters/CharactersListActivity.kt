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


class CharactersListActivity : AppCompatActivity(), CharactersListView,
    SwipeRefreshLayout.OnRefreshListener {

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

    override fun onRefresh() {
        paginationAdapter.clear()
        presenter.onRefresh()
    }

    override fun showEmptyError() {
        emptyError.setGone(false)
    }

    override fun hideEmptyError() {
        emptyError.setGone()
    }

    override fun showRefreshing() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideRefreshing() {
        swipeRefresh.isRefreshing = false
    }

    override fun showPageLoader() {
        paginationAdapter.addLoading()
    }

    override fun hidePageLoader() {
        paginationAdapter.removeLoading()
    }

    override fun showPageLoadError() {
        paginationAdapter.addError()
    }

    override fun hidePageLoadError() {
        paginationAdapter.removeError()
    }

    override fun showListCharacters(data: List<CharacterItemUI>) {
        paginationAdapter.addItems(data)
        listCharacters.setGone(false)
    }

    override fun hideListCharacters() {
        listCharacters.setGone()
    }

    private fun setupListCharacters() {
        val drawableResource = ContextCompat.getDrawable(this, R.drawable.divider)
        val divider = drawableResource?.let { CustomDividerItemDecorator(it) }

        val linearLayoutManager = LinearLayoutManager(this)
        paginationAdapter = PaginationAdapter {
            presenter.getCharacters()
        }

        with(listCharacters) {
            layoutManager = linearLayoutManager
            adapter = paginationAdapter
            setHasFixedSize(true)
            divider?.let { addItemDecoration(divider) }

            addOnScrollListener(object : PaginationListener(linearLayoutManager) {
                override fun loadMoreItems() {
                    presenter.getCharacters()
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