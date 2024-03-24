package com.example.appapi.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.appapi.compose.PageEnum
import com.example.appapi.compose.dataClass.Plongee

interface ISection {
    @Composable
    fun Liste(modifier: Modifier)

    @Composable
    fun Insertion(modifier: Modifier)

    @Composable
    fun Gestion_plongee(
        modifier: Modifier,
        indexPage: MutableState<PageEnum>,
        selectedPlongee: MutableState<Plongee?>
    )

    @Composable
    fun PlongeeDetail(modifier: Modifier, plongee: Plongee, indexPage: MutableState<PageEnum>)
}