package ru.test.app.models

data class Character(
    val url: String = "",
    val name: String = "",
    val gender: String = "",
    val culture: String = "",
    val born: String = "",
    val died: String = "",
    val father: String = "",
    val spouse: String = "",
    val allegiances: List<String> = listOf(),
    val titles:	List<String> = listOf(),
    val aliases: List<String> = listOf(),
    val books: List<String> = listOf(),
    val povBooks: List<String> = listOf(),
    val tvSeries: List<String> = listOf(),
    val playedBy: List<String> = listOf()
)