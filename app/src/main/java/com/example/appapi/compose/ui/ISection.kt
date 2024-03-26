package com.example.appapi.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.appapi.compose.PageEnum
import com.example.appapi.compose.dataClass.Adherent
import com.example.appapi.compose.dataClass.Plongee

interface ISection {
    @Composable
    fun GestionAdherent(modifier: Modifier,  indexPage: MutableState<PageEnum>, selectedAdherent: MutableState<Adherent?>)

    @Composable
    fun DetailsAdherent(modifier: Modifier, adherent: Adherent, indexPage: MutableState<PageEnum>)

    @Composable
    fun ModificationAdherent(modifier: Modifier, adherent: Adherent)

    @Composable
    fun CreationAdherent(modifier: Modifier)


    @Composable
    fun GestionPlongee(
        modifier: Modifier,
        indexPage: MutableState<PageEnum>,
        selectedPlongee: MutableState<Plongee?>
    )

    @Composable
    fun PlongeeDetails(modifier: Modifier, plongee: Plongee, indexPage: MutableState<PageEnum>)
}