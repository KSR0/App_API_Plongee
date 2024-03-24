package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Participant(
    val id: Int,
    val adherent: Int,
    val plongee: Int,
)

