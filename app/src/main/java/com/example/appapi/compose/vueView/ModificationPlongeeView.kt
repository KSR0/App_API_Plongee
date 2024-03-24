package com.example.appapi.compose.vueView

import android.app.DatePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View.inflate
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import com.example.appapi.R
import com.example.appapi.compose.dataClass.Adherent
import com.example.appapi.compose.dataClass.Bateau
import com.example.appapi.compose.dataClass.Lieux
import com.example.appapi.compose.dataClass.Moment
import com.example.appapi.compose.dataClass.Niveaux
import com.example.appapi.compose.dataClass.Personne
import com.example.appapi.compose.dataClass.Plongee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class ModificationPlongeeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    plongee: Plongee?
) : ScrollView(context, attrs, defStyle) {

    private var Lieu: Spinner
    private var Bateau: Spinner
    private var Date: EditText
    private var Moment: Spinner
    private var Min: EditText
    private var Max: EditText
    private var Niveau: Spinner
    private var Pilote: Spinner
    private var Securite: Spinner
    private var Directeur: Spinner
    init {
        inflate(context, R.layout.modification_plongee_view, this)

        Date = findViewById(R.id.modifdate)
        Bateau = findViewById(R.id.modifbateau)
        Niveau = findViewById(R.id.modifniveau)
        Moment = findViewById(R.id.modifmoment)
        Min = findViewById(R.id.modifmin)
        Max = findViewById(R.id.modifmax)
        Pilote = findViewById(R.id.modifpilote)
        Securite = findViewById(R.id.modifsecurite)
        Directeur = findViewById(R.id.modifdirecteur)
        Lieu = findViewById(R.id.modiflieu)
        val buttonModifier = findViewById<Button>(R.id.buttonModifier)

        fun showDatePickerDialog() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, dayOfMonth ->
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDayOfMonth = String.format("%02d", dayOfMonth)
                val selectedDate = "$selectedYear-${formattedMonth}-$formattedDayOfMonth"
                Date.setText(selectedDate)
            }, year, month, dayOfMonth)

            datePickerDialog.show()
        }

        Date.setOnClickListener {
            showDatePickerDialog()
        }




        val adapterBateau = ArrayAdapter<Bateau>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterLieu = ArrayAdapter<Lieux>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterMoment = ArrayAdapter<Moment>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterNiveau = ArrayAdapter<Niveaux>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterPilote = ArrayAdapter<Personne>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterSecurite = ArrayAdapter<Personne>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())
        val adapterDirecteur = ArrayAdapter<Personne>(context, android.R.layout.simple_spinner_dropdown_item, mutableListOf())

        Lieu.adapter = adapterLieu
        Moment.adapter = adapterMoment
        Niveau.adapter = adapterNiveau
        Bateau.adapter = adapterBateau
        Pilote.adapter = adapterPilote
        Securite.adapter = adapterSecurite
        Directeur.adapter = adapterDirecteur

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bateaux = ApiManager.getBateauxList()
                val lieux = ApiManager.getLieuxList()
                val moments = ApiManager.getMomentsList()
                val niveaux = ApiManager.getNiveauxList()
                val pilote = ApiManager.getPersonnesList()
                val securite = ApiManager.getPersonnesList()
                val directeur = ApiManager.getPersonnesList()

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
                Log.e("ModificationPlongeeView", "Error fetching bateaux list: ${e.message}")
            }
        }

        buttonModifier.setOnClickListener {
            val newDate = Date.text.toString()
            val newBateau = (Bateau.selectedItem as Bateau).id
            val newNiveau = (Niveau.selectedItem as Niveaux).id
            val newMoment = (Moment.selectedItem as Moment).id
            val newMin = Min.text.toString().toInt()
            val newMax = Max.text.toString().toInt()
            val newPilote = (Pilote.selectedItem as Personne).id
            val newSecurite = (Securite.selectedItem as Personne).id
            val newDirecteur = (Directeur.selectedItem as Personne).id
            val newLieu = (Lieu.selectedItem as Lieux).id

            val modifiedPlongee = plongee?.let { it1 -> Plongee(it1.id,newLieu,newBateau,newDate,newMoment,newMin,newMax,newNiveau,plongee.active,plongee.niveau,newPilote,newSecurite,newDirecteur) }

            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    try {
                        if (modifiedPlongee != null) {
                            val response = ApiManager.updateDive(plongee, modifiedPlongee)
                            Log.d("ResponseAPI", response)
                        } else {
                            Log.e("ButtonClickListener", "Modified plongee is null")
                        }
                    } catch (e: Exception) {
                        Log.e("ButtonClickListener", "Error: ${e.message}", e)
                    }
                }
            }
        }
    }
}
