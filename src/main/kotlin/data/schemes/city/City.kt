package com.tiooooo.data.schemes.city

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val name: String,
    val population: Int,
)