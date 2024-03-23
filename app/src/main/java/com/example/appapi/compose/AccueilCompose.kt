package com.example.appapi.compose

import ApiManager
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
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
import com.example.appapi.compose.ui.ISection
import com.example.appapi.compose.ui.Membres
import com.example.appapi.compose.ui.theme.AppAPITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
                    indexPage.value = PageEnum.SAISIE_MODIFICATION
                },
                enabled = (indexPage.value != PageEnum.SAISIE_MODIFICATION),
                modifier = Modifier.padding(horizontal = 7.dp)
            ) {
                Text(
                    text = "Saisie/Modification"
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

class SaisieModificationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val editTextNom: EditText
    private val editTextPrenom: EditText
    private val editTextMail: EditText
    private val editTextMotDePasse: EditText
    private val editTextPhotoProfil: EditText
    private val editTextMembers: EditText
    private val buttonAjouter: Button
    private val buttonLister : Button

    init {
        inflate(context, R.layout.saisie_modification_view, this)

        editTextNom = findViewById(R.id.editTextNom)
        editTextPrenom = findViewById(R.id.editTextPrenom)
        editTextMail = findViewById(R.id.editTextMail)
        editTextMotDePasse = findViewById(R.id.editTextMotDePasse)
        editTextPhotoProfil = findViewById(R.id.editTextPhotoProfil)
        editTextMembers = findViewById(R.id.editTextMembers)

        buttonLister = findViewById(R.id.buttonLister)
        buttonLister.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val members = ApiManager.getMemberList()
                    withContext(Dispatchers.Main) {
                        AlertDialog.Builder(context)
                            .setTitle("Membres")
                            .setMessage(members.joinToString("\n"))
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }
                } catch (e: Exception) {
                    Log.e("SaisieModificationView", "Error fetching member list: ${e.message}")
                }
            }
        }



        buttonAjouter = findViewById(R.id.buttonAjouter)
        buttonAjouter.setOnClickListener {
            val nom = editTextNom.text.toString()
            val prenom = editTextPrenom.text.toString()
            val mail = editTextMail.text.toString()
            val motDePasse = editTextMotDePasse.text.toString()
            val photoProfil = editTextPhotoProfil.text.toString()


        }
    }
}


@Composable
fun Component(modifier: Modifier, indexPage: MutableState<PageEnum>, section: ISection) {
    return Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        when (indexPage.value) {
            PageEnum.LISTE -> section.Liste(modifier = Modifier)
            PageEnum.INSERTION -> section.Insertion(modifier = Modifier)
            PageEnum.SAISIE_MODIFICATION -> {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        SaisieModificationView(context)
                    }
                )
            }
        }
    }
}