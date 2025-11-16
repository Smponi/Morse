package com.mouse.mouse

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
