package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Lieux(
    val id: Int,
    val libelle: String,
    val description: String,
    val actif: Boolean,
){
    override fun toString(): String {
        return libelle
    }
}

