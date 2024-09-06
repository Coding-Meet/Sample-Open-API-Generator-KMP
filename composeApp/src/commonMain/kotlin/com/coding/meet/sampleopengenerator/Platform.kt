package com.coding.meet.sampleopengenerator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform