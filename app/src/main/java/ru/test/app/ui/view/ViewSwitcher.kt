package ru.test.app.ui.view

import android.view.View
import ru.test.app.extension.setVisible

class ViewSwitcher(
    vararg val views: View,
    private val mode: Int = View.GONE
) {
    init {
        if (views.isNotEmpty()) displayAt(0)
    }

    fun displayAt(index: Int) {
        views.forEachIndexed { i, view ->
            if (i != index)
                view.visibility = mode
            else view.setVisible()
        }
    }

    fun display(view: View) {
        views.forEach {
            if (it !== view)
                it.visibility = mode
            else it.setVisible()
        }
    }
}