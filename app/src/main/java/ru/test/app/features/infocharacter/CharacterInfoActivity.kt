package ru.test.app.features.infocharacter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_character_info.*
import ru.test.app.R
import ru.test.app.ui.recycler.adapters.CharacterInfoAdapter
import ru.test.app.ui.view.ViewSwitcher

class CharacterInfoActivity : AppCompatActivity(), CharacterInfoView {

    private lateinit var presenter: CharacterInfoPresenter
    private lateinit var characterInfoAdapter: CharacterInfoAdapter
    private lateinit var viewSwitch: ViewSwitcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character_info)
        val id = intent.extras?.getLong("id") ?: -1
        viewSwitch = ViewSwitcher(emptyStub, listInfoCharacter, progressBar)
        setupListCharacterInfo()
        setupEmptyStub()
        setupPresenter(id)
    }

    override fun showCharacterInformation(data: List<Pair<String, String>>) {
        viewSwitch.display(listInfoCharacter)
        characterInfoAdapter.addItems(data)
    }

    override fun showLoader() {
        viewSwitch.display(progressBar)
    }

    override fun showStub(e: Exception) {
        viewSwitch.display(emptyStub)
    }

    private fun setupPresenter(id: Long) {
        presenter = CharacterInfoPresenter(id, this)
    }

    private fun setupListCharacterInfo() {
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val linearLayoutManager = LinearLayoutManager(this)
        characterInfoAdapter = CharacterInfoAdapter()

        with(listInfoCharacter) {
            layoutManager = linearLayoutManager
            adapter = characterInfoAdapter
            addItemDecoration(divider)
        }
    }

    private fun setupEmptyStub() {
        bottomRetry.setOnClickListener { presenter.getCharacter() }
    }
}