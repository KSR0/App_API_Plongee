package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Personne(
    val id: Int,
    val nom: String,
    val prenom: String,
    val actif: Boolean,
)

