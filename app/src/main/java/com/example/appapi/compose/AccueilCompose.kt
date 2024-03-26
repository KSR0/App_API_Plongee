package com.example.appapi.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.appapi.compose.ui.Plongee
import com.example.appapi.compose.ui.ISection
import com.example.appapi.compose.ui.Adherent
import com.example.appapi.compose.ui.theme.AppAPITheme
import com.example.appapi.compose.vueView.CreationPlongeeView
import com.example.appapi.compose.vueView.ModificationPlongeeView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppAPITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val section = remember {
                        mutableStateOf<ISection>(Adherent())
                    }
                    Section(section)
                }
            }
        }
    }
}


@Composable
fun Section(section: MutableState<ISection>) {
    val indexPage = remember {
        mutableStateOf(PageEnum.GESTION_ADHERENT)
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.2f))

            OutlinedButton(
                onClick = {
                    indexPage.value = PageEnum.GESTION_ADHERENT
                    section.value = Adherent()
                },
                enabled = (indexPage.value != PageEnum.GESTION_ADHERENT),
                modifier = Modifier.padding(7.dp)
            ) {
                Text(
                    text = "Gestion Adherent"
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))

            OutlinedButton(
                onClick = {
                    indexPage.value = PageEnum.GESTION_PLONGEE
                    section.value = Plongee()
                },
                enabled = (indexPage.value != PageEnum.GESTION_PLONGEE),
                modifier = Modifier.padding(7.dp)
            ) {
                Text(
                    text = "Gestion Plongee"
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }

        Component(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            indexPage,
            section.value
        )

    }
}


@Composable
fun Component(modifier: Modifier, indexPage: MutableState<PageEnum>, section: ISection) {
    val selectedPlongee = remember { mutableStateOf<com.example.appapi.compose.dataClass.Plongee?>(null) }
    val selectedAdherent = remember { mutableStateOf<com.example.appapi.compose.dataClass.Adherent?>(null) }
    return Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        when (indexPage.value) {
            PageEnum.GESTION_ADHERENT -> section.GestionAdherent(modifier = Modifier, indexPage, selectedAdherent)
            PageEnum.ADHERENT_DETAIL -> {
                selectedAdherent.value?.let { adherent ->
                    section.DetailsAdherent(Modifier, adherent, indexPage)
                }
            }
            PageEnum.MODIFICATION_ADHERENT -> selectedAdherent.value?.let {
                section.ModificationAdherent(modifier = Modifier,
                    it
                )
            }
            PageEnum.CREATION_ADHERENT -> section.CreationAdherent(modifier = Modifier)


            PageEnum.GESTION_PLONGEE -> section.GestionPlongee(modifier = Modifier, indexPage, selectedPlongee)
            PageEnum.PLONGEE_DETAIL -> {
                selectedPlongee.value?.let { plongee ->
                    section.PlongeeDetails(modifier = Modifier, plongee, indexPage)
                }
            }
            PageEnum.MODIFICATION_PLONGEE -> { AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    ModificationPlongeeView(context, plongee = selectedPlongee.value)
                }
            )}
            PageEnum.CREATION_PLONGEE -> {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        CreationPlongeeView(context)
                    }
                )
            }

        }
    }
}