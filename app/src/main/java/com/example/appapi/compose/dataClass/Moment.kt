package com.example.appapi.compose.dataClass

import kotlinx.serialization.Serializable

@Serializable
data class Moment(
    val id: Int,
    val libelle : String,
){
    override fun toString(): String {
        return libelle
    }
}


