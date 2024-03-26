package com.example.appapi.compose.ui

import ApiManager.getNiveauxList
import android.util.Log
import android.widget.Spinner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appapi.compose.PageEnum
import com.example.appapi.compose.dataClass.Adherent
import com.example.appapi.compose.dataClass.Niveaux
import com.example.appapi.compose.dataClass.Plongee
import com.example.appapi.compose.utils.findNiveau
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Adherent: ISection {
    @Composable
    override fun GestionAdherent(modifier: Modifier, indexPage: MutableState<PageEnum>, selectedAdherent: MutableState<com.example.appapi.compose.dataClass.Adherent?>) {
        Column(modifier = modifier.padding(16.dp)) {
            Text(text = "Liste des adherents", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {indexPage.value = PageEnum.CREATION_ADHERENT}) {
                Text(text = "Ajouter un adherent")
            }
            Spacer(modifier = Modifier.height(10.dp))

            var adherents by remember { mutableStateOf<List<com.example.appapi.compose.dataClass.Adherent>>(emptyList()) }
            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    adherents = ApiManager.getAdherentsList()
                }
            }

            LazyColumn {
                items(adherents) { adherent ->
                    TextButton(onClick = {
                        indexPage.value = PageEnum.ADHERENT_DETAIL;
                        selectedAdherent.value = adherent
                    }
                    ) {
                        Text(text = "${adherent.prenom} ${adherent.nom}, ${adherent.email}")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @Composable
    override fun DetailsAdherent(
        modifier: Modifier,
        adherent: Adherent,
        indexPage: MutableState<PageEnum>
    ) {
        var niveaux by remember { mutableStateOf<List<Niveaux>>(emptyList()) }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Détails de l'adherent", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(10.dp))

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    niveaux = getNiveauxList()
                }
            }

            Text("Nom: ${adherent.nom}")
            Text("Prenom: ${adherent.prenom}")
            Text("Licence: ${adherent.licence}")
            Text("Forfait: ${adherent.forfait}")
            Text("Niveau: ${niveaux.findNiveau(adherent.niveau)}")
            Text("Mail: ${adherent.email}")
            Text("Date certificat: ${adherent.date_certificat_medical}")


            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {indexPage.value = PageEnum.MODIFICATION_ADHERENT}) {
                Text(text = "Modifier l'adherent")
            }
        }

    }

    @Composable
    override fun ModificationAdherent(modifier: Modifier, adherent: Adherent) {
        var niveaux by remember { mutableStateOf<List<Niveaux>>(emptyList()) }

        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                niveaux = getNiveauxList()
            }
        }

        Column (modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally){
            val nom = remember {
                mutableStateOf(adherent.nom)
            }
            val prenom = remember {
                mutableStateOf(adherent.prenom)
            }
            val email = remember {
                mutableStateOf(adherent.email)
            }
            val forfait = remember {
                mutableStateOf(adherent.forfait)
            }

            Spacer(modifier = Modifier.weight(0.25f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Text(text = "Modification de l'adhérent(e): ", fontSize = 18.sp)
                }
                Row {
                    Text(text = "${adherent.prenom} ${adherent.nom}", fontSize = 18.sp)
                }

            }
            Spacer(modifier = Modifier.weight(0.5f))
            Row {
                TextField(
                    value = nom.value,
                    onValueChange = {
                        nom.value = it
                    },
                    label = {
                        Text(text = "Nom")
                    }
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Row {
                TextField(
                    value = prenom.value,
                    onValueChange = {
                        prenom.value = it
                    },
                    label = {
                        Text(text = "Prénom")
                    }
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Row {
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    label = {
                        Text(text = "Email")
                    }
                )
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Row {
                TextField(
                    value = forfait.value,
                    onValueChange = {
                        forfait.value = it
                    },
                    label = {
                        Text(text = "Forfait")
                    }
                )
            }


            val modifiedAdherent = Adherent(adherent.id, adherent.licence,adherent.date_certificat_medical,forfait.value,adherent.niveau,nom.value,prenom.value,email.value,adherent.actif)
            Spacer(modifier = Modifier.weight(0.5f))
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val response = ApiManager.updateAdherent(adherent, modifiedAdherent)
                            Log.d("ResponseAPI", response)
                        } catch (e: Exception) {
                            Log.e("UpdateAdherent", "Error: ${e.message}", e)
                        }
                    }
                }
            }) {
                Text(text = "Modifier l'adhérent")
            }
            Spacer(modifier = Modifier.weight(5f))

        }


    }

    @Composable
    override fun CreationAdherent(modifier: Modifier) {
        Text(text = "Creation d'un adherent")
    }

    @Composable
    override fun GestionPlongee(
        modifier: Modifier,
        indexPage: MutableState<PageEnum>,
        selectedPlongee: MutableState<Plongee?>
    ) {}

    @Composable
    override fun PlongeeDetails(
        modifier: Modifier,
        plongee: Plongee,
        indexPage: MutableState<PageEnum>
    ) {}

}