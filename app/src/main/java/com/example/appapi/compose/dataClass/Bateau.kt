package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Bateau(
    val id: Int,
    val libelle: String,
    val max_personnes: Int,
    val actif: Boolean,
) {
    override fun toString(): String {
        return libelle
    }
}

