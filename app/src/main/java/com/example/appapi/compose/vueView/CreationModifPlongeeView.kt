package com.example.appapi.compose.vueView

import com.example.appapi.compose.dataClass.Adherent
import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import com.example.appapi.R
import com.example.appapi.compose.dataClass.Bateau
import com.example.appapi.compose.dataClass.Lieux
import com.example.appapi.compose.dataClass.Moment
import com.example.appapi.compose.dataClass.Niveaux
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

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




        val adapterBateau = ArrayAdapter<Bateau>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterLieu = ArrayAdapter<Lieux>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterMoment = ArrayAdapter<Moment>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterNiveau = ArrayAdapter<Niveaux>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterPilote = ArrayAdapter<Adherent>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterSecurite = ArrayAdapter<Adherent>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterDirecteur = ArrayAdapter<Adherent>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())

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
            val lieu = (spinnerLieu.selectedItem as Lieux).id
            val bateau = (spinnerBateau.selectedItem as Bateau).id
            val date = editTextDate.text.toString()
            val moment = (spinnerMoment.selectedItem as Moment).id
            val min = editTextMin.text.toString().toInt()
            val max = editTextMax.text.toString().toInt()
            val niveau = (spinnerNiveau.selectedItem as Niveaux).id
            val pilote = (spinnerPilote.selectedItem as Adherent).id
            val securite = (spinnerSecurite.selectedItem as Adherent).id
            val directeur = (spinnerDirecteur.selectedItem as Adherent).id

            Log.d("Summary", "Lieu : $lieu, Bateau : $bateau, Date : $date, Moment : $moment, Min : $min, Max : $max, Niveau : $niveau, Pilote : $pilote, Securite : $securite, Directeur : $directeur")

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    try {
                        val response = ApiManager.addDive(
                            lieu,
                            bateau,
                            date,
                            moment,
                            min,
                            max,
                            niveau,
                            pilote,
                            securite,
                            directeur
                        )
                        Log.d("ResponseAPI",response)
                    } catch (e: Exception) {
                        Log.e("ButtonClickListener", "Error: ${e.message}", e)

                    }
                }
            }
        }
    }
}