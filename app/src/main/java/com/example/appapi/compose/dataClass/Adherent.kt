package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Adherent(
    val id: Int,
    val licence : String,
    val date_certificat_medical : String,
    val forfait : String,
    val niveau : Int,
    val nom: String,
    val prenom: String,
    val mail: String,
    val actif : Boolean
){
    override fun toString(): String {
        return "$nom $prenom"
    }
}
