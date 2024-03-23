package com.example.appapi.compose

import ApiManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
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
import java.util.Calendar


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
                    indexPage.value = PageEnum.CREATION_MODIFICATION_DIVE
                },
                enabled = (indexPage.value != PageEnum.CREATION_MODIFICATION_DIVE),
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

class CreationModifPlongeeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ScrollView(context, attrs, defStyle) {

    private val spinnerLieu: Spinner
    private val spinnerBateau: Spinner
    private val editTextDate: EditText
    private val spinnerMoment: Spinner
    private val editTextMin: EditText
    private val editTextMax: EditText
    private val spinnerNiveau: Spinner
    private val spinnerPilote: Spinner
    private val spinnerSecurite: Spinner
    private val spinnerDirecteur: Spinner
    private val buttonAjouter: Button

    init {
        inflate(context, R.layout.creation_modif_plongee_view, this)

        spinnerLieu = findViewById(R.id.lieu)
        spinnerBateau = findViewById(R.id.bateau)
        editTextDate = findViewById(R.id.date)
        spinnerMoment = findViewById(R.id.moment)
        editTextMin = findViewById(R.id.min)
        editTextMax = findViewById(R.id.max)
        spinnerNiveau = findViewById(R.id.niveau)
        spinnerPilote = findViewById(R.id.pilote)
        spinnerSecurite = findViewById(R.id.securite)
        spinnerDirecteur = findViewById(R.id.directeur)

        fun showDatePickerDialog() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, dayOfMonth ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDayOfMonth = String.format("%02d", dayOfMonth)
                val selectedDate = "$selectedYear-${formattedMonth}-$formattedDayOfMonth"
                editTextDate.setText(selectedDate)
            }, year, month, dayOfMonth)

            datePickerDialog.show()
        }

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }




        val adapterBateau = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterLieu = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterMoment = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterNiveau = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterPilote = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterSecurite = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterDirecteur = ArrayAdapter<Item>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())

        spinnerLieu.adapter = adapterLieu
        spinnerMoment.adapter = adapterMoment
        spinnerNiveau.adapter = adapterNiveau
        spinnerBateau.adapter = adapterBateau
        spinnerPilote.adapter = adapterPilote
        spinnerSecurite.adapter = adapterSecurite
        spinnerDirecteur.adapter = adapterDirecteur

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bateaux = ApiManager.getBateauxList()
                val lieux = ApiManager.getLieuxList()
                val moments = ApiManager.getMomentsList()
                val niveaux = ApiManager.getNiveauxList()
                val pilote = ApiManager.getAdherentsList()
                val securite = ApiManager.getAdherentsList()
                val directeur = ApiManager.getAdherentsList()

                withContext(Dispatchers.Main) {
                    adapterBateau.clear()
                    adapterBateau.addAll(bateaux)
                    adapterBateau.notifyDataSetChanged()

                    adapterLieu.clear()
                    adapterLieu.addAll(lieux)
                    adapterLieu.notifyDataSetChanged()

                    adapterMoment.clear()
                    adapterMoment.addAll(moments)
                    adapterMoment.notifyDataSetChanged()

                    adapterNiveau.clear()
                    adapterNiveau.addAll(niveaux)
                    adapterNiveau.notifyDataSetChanged()

                    adapterPilote.clear()
                    adapterPilote.addAll(pilote)
                    adapterPilote.notifyDataSetChanged()

                    adapterSecurite.clear()
                    adapterSecurite.addAll(securite)
                    adapterSecurite.notifyDataSetChanged()

                    adapterDirecteur.clear()
                    adapterDirecteur.addAll(directeur)
                    adapterDirecteur.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("CreationModifPlongeeView", "Error fetching bateaux list: ${e.message}")
            }
        }

        buttonAjouter = findViewById(R.id.buttonAjouter)
        buttonAjouter.setOnClickListener {
            val lieu = spinnerLieu.selectedItem as Item
            val lieuId = lieu.id
            val bateau = spinnerBateau.selectedItem as Item
            val bateauId = bateau.id
            val date = editTextDate.text.toString()
            val moment = spinnerMoment.selectedItem as Item
            val momentId = moment.id
            val min = editTextMin.text.toString().toInt()
            val max = editTextMax.text.toString().toInt()
            val niveau = spinnerNiveau.selectedItem as Item
            val niveauId = niveau.id
            val pilote = spinnerPilote.selectedItem as Item
            val piloteId = pilote.id
            val securite = spinnerSecurite.selectedItem as Item
            val securiteId = securite.id
            val directeur = spinnerDirecteur.selectedItem as Item
            val directeurId = directeur.id

            Log.d("Summary", "Lieu : $lieuId, Bateau : $bateauId, Date : $date, Moment : $momentId, Min : $min, Max : $max, Niveau : $niveauId, Pilote : $piloteId, Securite : $securiteId, Directeur : $directeurId")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ApiManager.addDive(
                        lieuId,
                        bateauId,
                        date,
                        momentId,
                        min,
                        max,
                        niveauId,
                        piloteId,
                        securiteId,
                        directeurId
                    )
                    Log.d("ResponseAPI",response)
                } catch (e: Exception) {
                    Log.e("ButtonClickListener", "Error: ${e.message}", e)

                }
            }
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
            PageEnum.CREATION_MODIFICATION_DIVE -> {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        CreationModifPlongeeView(context)
                    }
                )
            }
        }
    }
}