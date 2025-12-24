package com.aritradas.movie_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform