package com.example.appapi.compose.ui

import ApiManager.getBateauxList
import ApiManager.getLieuxList
import ApiManager.getMomentsList
import ApiManager.getNiveauxList
import ApiManager.getParticipantsList
import ApiManager.getPersonnesList
import ApiManager.getPlongeesList
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appapi.compose.PageEnum
import com.example.appapi.compose.dataClass.Bateau
import com.example.appapi.compose.dataClass.Lieux
import com.example.appapi.compose.dataClass.Moment
import com.example.appapi.compose.dataClass.Niveaux
import com.example.appapi.compose.dataClass.Participant
import com.example.appapi.compose.dataClass.Personne
import com.example.appapi.compose.dataClass.Plongee
import com.example.appapi.compose.utils.findBateau
import com.example.appapi.compose.utils.findLieu
import com.example.appapi.compose.utils.findMoment
import com.example.appapi.compose.utils.findNiveau
import com.example.appapi.compose.utils.findPersonne
import com.example.appapi.compose.utils.getNombreParticipants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Membres: ISection {
    @Composable
    override fun Liste(modifier: Modifier) {
        Text(text = "Liste")
    }

    @Composable
    override fun Insertion(modifier: Modifier) {
        Text(text = "Insertion")
    }


    @Composable
    override fun Gestion_plongee(
        modifier: Modifier,
        indexPage: MutableState<PageEnum>,
        selectedPlongee: MutableState<Plongee?>
    ) {
        var plongees by remember { mutableStateOf<List<Plongee>>(emptyList()) }
        var niveaux by remember { mutableStateOf<List<Niveaux>>(emptyList()) }
        var participants by remember { mutableStateOf<List<Participant>>(emptyList()) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                plongees = getPlongeesList()
                niveaux = getNiveauxList()
                participants = getParticipantsList()
            }
        }


        fun getNiveauLibelle(niveauId: Int): String {
            return niveaux.find { it.id == niveauId }?.libelle ?: "Niveau inconnu"
        }

        fun getNombreParticipants(plongeeId: Int): Int {
            return participants.filter { it.plongee == plongeeId }.size
        }

        Column(modifier = modifier.padding(16.dp)) {
            Text(text = "Liste des plongées", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {indexPage.value = PageEnum.CREATION_MODIFICATION_DIVE}) {
                Text(text = "Ajouter une plongée")
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn {
                items(plongees) { plongee ->
                    TextButton(onClick = { indexPage.value = PageEnum.PLONGEE_DETAIL; selectedPlongee.value = plongee }) {
                        val niveauLibelle = getNiveauLibelle(plongee.niveau)
                        val nombreParticipants = getNombreParticipants(plongee.id)
                        Text(text = "Plongée du  ${plongee.date} de niveau ${niveauLibelle} avec $nombreParticipants participants sur ${plongee.max_plongeurs}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @Composable
    override fun PlongeeDetail(modifier: Modifier, plongee: Plongee) {

        var niveaux by remember { mutableStateOf<List<Niveaux>>(emptyList()) }
        var participants by remember { mutableStateOf<List<Participant>>(emptyList()) }
        var bateaux by remember { mutableStateOf<List<Bateau>>(emptyList()) }
        var moments by remember { mutableStateOf<List<Moment>>(emptyList()) }
        var personnes by remember { mutableStateOf<List<Personne>>(emptyList()) }
        var lieux by remember { mutableStateOf<List<Lieux>>(emptyList()) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                niveaux = getNiveauxList()
                participants = getParticipantsList()
                bateaux = getBateauxList()
                moments = getMomentsList()
                personnes = getPersonnesList()
                lieux = getLieuxList()
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Détails de la plongée", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(10.dp))

            Text("Date: ${plongee.date}")
            Text("Bateau: ${bateaux.findBateau(plongee.bateau)?.libelle ?: "Bateau inconnu"}")
            Text("Niveau: ${niveaux.findNiveau(plongee.niveau)?.libelle ?: "Niveau inconnu"}")
            Text("Lieu: ${lieux.findLieu(plongee.lieu)?.libelle ?: "Lieu inconnu"}")
            Text("Moment: ${moments.findMoment(plongee.moment)?.libelle ?: "Moment inconnu"}")
            Text("Pilote: ${personnes.findPersonne(plongee.pilote) ?: "Pilote inconnu"}")
            Text("Securite de surface: ${personnes.findPersonne(plongee.securite_de_surface)?: "Securite inconnue"}")
            Text("Directeur de plongee: ${personnes.findPersonne(plongee.directeur_de_plongee)?: "Directeur inconnu"}")
            Text("Plongeurs Min: ${plongee.min_plongeurs}")
            Text("Plongeurs Max: ${plongee.max_plongeurs}")
            Text("Nombre Participants: ${getNombreParticipants(participants,plongee.id)}")
        }
    }

}