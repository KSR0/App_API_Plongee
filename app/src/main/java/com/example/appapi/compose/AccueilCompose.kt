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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.appapi.R
import com.example.appapi.compose.dataClass.Plongee
import com.example.appapi.compose.ui.ISection
import com.example.appapi.compose.ui.Membres
import com.example.appapi.compose.ui.theme.AppAPITheme
import com.example.appapi.compose.vueView.CreationPlongeeView
import com.example.appapi.compose.vueView.ModificationPlongeeView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


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
                        mutableStateOf<ISection>(Membres())
                    }
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    val onClickOpenMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                    val onClickCloseMenu = {
                        scope.launch {
                            drawerState.close()
                        }
                    }

                    ModalNavigationDrawer(
                        drawerContent = { Menu(section = section, onClickCloseMenu = onClickCloseMenu)},
                        drawerState = drawerState,
                        modifier = Modifier.width(10.dp)
                    ) {
                        Section(section.value, onClickOpenMenu)
                    }

                }
            }
        }
    }
}

@Composable
fun Menu(section: MutableState<ISection>, onClickCloseMenu: () -> Job) {
    Column {
        TextButton(
            onClick = {
                section.value = Membres()
                onClickCloseMenu()
            },
            enabled = (section.value.javaClass.simpleName != "Membres")
        ) {
            Text(text = "Membres", modifier = Modifier.fillMaxWidth())
        }
    }

}

@Composable
fun Section(section: ISection, onClickOpenMenu: () -> Job) {
    val indexPage = remember {
        mutableStateOf(PageEnum.LISTE)
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    onClickOpenMenu()
                },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = "")
            }
            Spacer(modifier = Modifier.weight(0.5f))
            OutlinedButton(
                onClick = {
                    indexPage.value = PageEnum.LISTE
                },
                enabled = (indexPage.value != PageEnum.LISTE),
                modifier = Modifier.padding(7.dp)
            ) {
                Text(
                    text = "Liste"
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            OutlinedButton(
                onClick = {
                    indexPage.value = PageEnum.INSERTION
                },
                enabled = (indexPage.value != PageEnum.INSERTION),
                modifier = Modifier.padding(7.dp)
            ) {
                Text(
                    text = "Création"
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            OutlinedButton(
                onClick = {
                    indexPage.value = PageEnum.GESTION_PLONGEE
                },
                enabled = (indexPage.value != PageEnum.GESTION_PLONGEE),
                modifier = Modifier.padding(horizontal = 7.dp)
            ) {
                Text(
                    text = "Gestion Plongee"
                )
            }
            Spacer(modifier = Modifier.weight(1f))

        }
        Component(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            indexPage,
            section
        )
    }
}


@Composable
fun Component(modifier: Modifier, indexPage: MutableState<PageEnum>, section: ISection) {
    val selectedPlongee = remember { mutableStateOf<Plongee?>(null) }
    return Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        when (indexPage.value) {
            PageEnum.LISTE -> section.Liste(modifier = Modifier)
            PageEnum.INSERTION -> section.Insertion(modifier = Modifier)
            PageEnum.CREATION_PLONGEE -> {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        CreationPlongeeView(context)
                    }
                )
            }
            PageEnum.GESTION_PLONGEE -> section.Gestion_plongee(modifier = Modifier, indexPage, selectedPlongee)
            PageEnum.PLONGEE_DETAIL -> {
                selectedPlongee.value?.let { plongee ->
                    section.PlongeeDetail(modifier = Modifier, plongee, indexPage)
                }
            }
            PageEnum.MODIFICATION_PLONGEE -> { AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    ModificationPlongeeView(context, plongee = selectedPlongee.value)
                }
            )}
        }
    }
}