package ru.test.app.extension

import android.view.View

fun View.setGone(gone: Boolean = true) {
    this.visibility = if (gone) View.GONE else View.VISIBLE
}

fun View.setVisible(visible: Boolean = true) {
    this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}