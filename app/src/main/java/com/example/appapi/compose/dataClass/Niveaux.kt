package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Niveaux(
    val id:Int,
    val libelle: String,
    val code : String,
    val profondeur_si_encadre : Int,
    val profondeur_si_autonome : Int,
    val niveau : Int,
    val guide_de_palanquee : Boolean,
    val directeur_de_plongee : Boolean
){
    override fun toString(): String {
        return libelle
    }
}
