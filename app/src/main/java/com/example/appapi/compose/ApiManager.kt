import android.util.Log
import com.example.appapi.compose.dataClass.Adherent
import com.example.appapi.compose.dataClass.Bateau
import com.example.appapi.compose.dataClass.Lieux
import com.example.appapi.compose.dataClass.Moment
import com.example.appapi.compose.dataClass.Niveaux
import com.example.appapi.compose.dataClass.Participant
import com.example.appapi.compose.dataClass.Personne
import com.example.appapi.compose.dataClass.Plongee
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter

object ApiManager {
    private var API_URL = ""

    fun addDive(lieu: Int, bateau: Int, date: String, moment: Int, minPlongeurs: Int, maxPlongeurs: Int, niveau: Int, pilote: Int, securiteDeSurface: Int, directeurDePlongee: Int): String {
        val API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/plongees"

        val plongeeJson = JSONObject().apply {
            put("lieu", lieu)
            put("bateau", bateau)
            put("date", date)
            put("moment", moment)
            put("min_plongeurs", minPlongeurs)
            put("max_plongeurs", maxPlongeurs)
            put("niveau", niveau)
            put("pilote", pilote)
            put("securite_de_surface", securiteDeSurface)
            put("directeur_de_plongee", directeurDePlongee)
        }

        var response = ""
        try {
            val url = URL(API_URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true

            val outputStreamWriter = OutputStreamWriter(connection.outputStream)
            outputStreamWriter.write(plongeeJson.toString())
            outputStreamWriter.flush()

            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            val responseBuilder = StringBuilder()
            while (inputStream.readLine().also { inputLine = it } != null) {
                responseBuilder.append(inputLine)
            }
            inputStream.close()

            response = responseBuilder.toString()

            connection.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return response
    }

    fun getBateauxList(): List<Bateau> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/bateaux"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val bateauxList = mutableListOf<Bateau>()
        try {
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val bateauxArray = gson.fromJson(responseText, Array<Bateau>::class.java)
            bateauxList.addAll(bateauxArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return bateauxList
    }

    fun getLieuxList(): List<Lieux> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/lieux"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val lieuxList = mutableListOf<Lieux>()
        try {
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val lieuxArray = gson.fromJson(responseText, Array<Lieux>::class.java)
            lieuxList.addAll(lieuxArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return lieuxList
    }

    fun getMomentsList(): List<Moment> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/moments"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val momentsList = mutableListOf<Moment>()
        try {
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val momentsArray = gson.fromJson(responseText, Array<Moment>::class.java)
            momentsList.addAll(momentsArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return momentsList
    }

    fun getNiveauxList(): List<Niveaux> {
        val API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/niveaux"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val niveauxList = mutableListOf<Niveaux>()
        try {
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val niveauxArray = gson.fromJson(responseText, Array<Niveaux>::class.java)
            niveauxList.addAll(niveauxArray)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return niveauxList
    }

    fun getAdherentsList(): List<Adherent> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/adherents"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val adherentsList = mutableListOf<Adherent>()
        try {
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val adherentsArray = gson.fromJson(responseText, Array<Adherent>::class.java)
            adherentsList.addAll(adherentsArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return adherentsList
    }

    fun getPersonnesList(): List<Personne> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/personnes"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val personnesList = mutableListOf<Personne>()
        try {
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val personnesArray = gson.fromJson(responseText, Array<Personne>::class.java)
            personnesList.addAll(personnesArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return personnesList
    }

    fun getPlongeesList(): List<Plongee> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/plongees"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val plongeesList = mutableListOf<Plongee>()
        try {
            connection.connect()
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }
            println("Response Text: $responseText")

            val gson = Gson()
            val plongeesArray = gson.fromJson(responseText, Array<Plongee>::class.java)
            plongeesList.addAll(plongeesArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return plongeesList
    }

    fun getParticipantsList(): List<Participant> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/participants"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val participantsList = mutableListOf<Participant>()
        try {
            connection.connect()
            val inputStream = connection.inputStream
            val responseText = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val participantsArray = gson.fromJson(responseText, Array<Participant>::class.java)
            participantsList.addAll(participantsArray)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return participantsList
    }

    fun updateDive(plongee: Plongee, newPlongee: Plongee): String {
        val API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/plongees/${plongee.id}"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        val jsonInputString = JSONObject().apply {
            put("lieu", newPlongee.lieu)
            put("bateau", newPlongee.bateau)
            put("date", newPlongee.date)
            put("moment", newPlongee.moment)
            put("min_plongeurs", newPlongee.min_plongeurs)
            put("max_plongeurs", newPlongee.max_plongeurs)
            put("niveau", newPlongee.niveau)
            put("active", newPlongee.active)
            put("etat", newPlongee.etat)
            put("pilote", newPlongee.pilote)
            put("securite_de_surface", newPlongee.securite_de_surface)
            put("directeur_de_plongee", newPlongee.directeur_de_plongee)
        }.toString()

        connection.outputStream.use { os ->
            val input = jsonInputString.toByteArray(Charsets.UTF_8)
            os.write(input, 0, input.size)
        }

        val response = StringBuilder()
        try {
            connection.connect()
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            while (inputStream.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            inputStream.close()
        } catch (e: Exception) {
            Log.e("Error", "Error: ${e.message}", e)
        } finally {
            connection.disconnect()
        }

        return response.toString()
    }


}
