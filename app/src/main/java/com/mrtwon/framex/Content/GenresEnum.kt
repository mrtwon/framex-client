package com.mrtwon.framex.Content

enum class GenresEnum {
    COMEDY{ override fun toString(): String = "комедия" },
    HORROR{ override fun toString(): String = "ужасы"},
    ADVENTURE{ override fun toString(): String = "приключения"},
    DETECTIVE{ override fun toString(): String = "детектив"},
    ACTION{ override fun toString(): String = "боевик"},
    DRAMA{ override fun toString(): String = "драма"},
    DOCUMENTARYFILM{ override fun toString(): String = "документальный"},
    BIOGRAPHY{ override fun toString(): String = "биография"},
    FANTASY{ override fun toString(): String = "фэнтези"},
    CRIMINAL{ override fun toString(): String = "криминал" }
}