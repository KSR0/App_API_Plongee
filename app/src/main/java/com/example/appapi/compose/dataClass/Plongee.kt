package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Plongee(
    val id: Int,
    val lieu: Int,
    val bateau: Int,
    val date: String,
    val moment: Int,
    val min_plongeurs: Int,
    val max_plongeurs: Int,
    val niveau: Int,
    val active: Boolean,
    val etat: Int,
    val pilote: Int,
    val securite_de_surface: Int,
    val directeur_de_plongee: Int
)

