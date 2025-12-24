package com.aritradas.movieapp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetails(
    @SerialName("id") val id: Int,
    @SerialName("username") val username: String? = null
)
