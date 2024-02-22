package com.example.appapi.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ISection {
    @Composable
    fun Liste(modifier: Modifier)

    @Composable
    fun Insertion(modifier: Modifier)
}