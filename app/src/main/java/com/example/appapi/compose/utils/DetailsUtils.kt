package com.example.appapi.compose.utils

import com.example.appapi.compose.dataClass.Bateau
import com.example.appapi.compose.dataClass.Lieux
import com.example.appapi.compose.dataClass.Moment
import com.example.appapi.compose.dataClass.Niveaux
import com.example.appapi.compose.dataClass.Participant
import com.example.appapi.compose.dataClass.Personne

fun List<Niveaux>.findNiveau(id: Int): Niveaux? = this.find { it.id == id }
fun List<Bateau>.findBateau(id: Int): Bateau? = this.find { it.id == id }
fun List<Moment>.findMoment(id: Int): Moment? = this.find { it.id == id }
fun List<Personne>.findPersonne(id: Int): String? {
    val personne = this.find { it.id == id } ?: return null
    return "${personne.nom} ${personne.prenom}"
}
fun List<Lieux>.findLieu(id: Int): Lieux? = this.find { it.id == id }

fun getNombreParticipants(participants: List<Participant>, plongeeId: Int): Int =
    participants.count { it.plongee == plongeeId }