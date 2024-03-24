package com.example.appapi.compose.ui

import ApiManager.getNiveauxList
import ApiManager.getParticipantsList
import ApiManager.getPlongeesList
import android.provider.Telephony.Mms.Part
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.appapi.compose.dataClass.Niveaux
import com.example.appapi.compose.dataClass.Participant
import com.example.appapi.compose.dataClass.Plongee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        indexPage: MutableState<PageEnum>
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
                    TextButton(onClick = {  }) {
                        val niveauLibelle = getNiveauLibelle(plongee.niveau)
                        val nombreParticipants = getNombreParticipants(plongee.id)
                        Text(text = "Plongée du  ${plongee.date} de niveau ${niveauLibelle} avec $nombreParticipants participants sur ${plongee.max_plongeurs}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}