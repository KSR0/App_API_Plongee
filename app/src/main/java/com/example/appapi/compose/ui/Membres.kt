package com.example.appapi.compose.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class Membres: ISection {
    @Composable
    override fun Liste(modifier: Modifier) {
        Text(text = "Liste")
    }

    @Composable
    override fun Insertion(modifier: Modifier) {
        Text(text = "Insertion")
    }
}