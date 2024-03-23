import android.util.Log
import com.example.appapi.compose.Item
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiManager {
    private var API_URL = ""
    fun getDivesList(): MutableList<String> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/plongees"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val response = mutableListOf<String>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getString("id")
                val lieu = jsonObject.getString("lieu")
                val bateau = jsonObject.getString("bateau")
                val date = jsonObject.getString("date")
                val moment = jsonObject.getString("moment")
                val min_plongeurs = jsonObject.getString("min_plongeurs")
                val max_plongeurs = jsonObject.getString("max_plongeurs")
                val niveau = jsonObject.getString("niveau")
                val active = jsonObject.getString("active")
                val etat = jsonObject.getString("etat")
                val pilote = jsonObject.getString("pilote")
                val securite_de_surface = jsonObject.getString("securite_de_surface")
                val directeur_de_plongee = jsonObject.getString("directeur_de_plongee")
                response.add("$id: $lieu: $date: $bateau")
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        response.sortBy { it.substringBefore(":").toInt() }

        return response
    }

    fun addDive(lieu: Int, bateau: Int, date: String, moment: Int, minPlongeurs: Int, maxPlongeurs: Int, niveau: Int, pilote: Int, securiteDeSurface: Int, directeurDePlongee: Int): String {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/plongees"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        val jsonInputString = JSONObject().apply {
            put("id",100)
            put("lieu", lieu)
            put("bateau", bateau)
            put("date", date)
            put("moment", moment)
            put("min_plongeurs", minPlongeurs)
            put("max_plongeurs", maxPlongeurs)
            put("niveau", niveau)
            put("active", true)
            put("etat", 1)
            put("pilote", pilote)
            put("securite_de_surface", securiteDeSurface)
            put("directeur_de_plongee", directeurDePlongee)
        }.toString()

        connection.outputStream.use { os ->
            val input = jsonInputString.toByteArray(charset("utf-8"))
            os.write(input, 0, input.size)
        }

        val response = StringBuilder()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            while (inputStream.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return response.toString()
    }

    fun getBateauxList(): List<Item> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/bateaux"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val bateauxList = mutableListOf<Item>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val libelle = jsonObject.getString("libelle")
                val item = Item(id, listOf(libelle))
                bateauxList.add(item)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return bateauxList
    }

    fun getLieuxList(): List<Item> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/lieux"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val lieuxList = mutableListOf<Item>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val libelle = jsonObject.getString("libelle")
                val item = Item(id, listOf(libelle))
                lieuxList.add(item)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return lieuxList
    }

    fun getMomentsList(): List<Item> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/moments"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val momentsList = mutableListOf<Item>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val libelle = jsonObject.getString("libelle")
                val item = Item(id, listOf(libelle))
                momentsList.add(item)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return momentsList
    }

    fun getNiveauxList(): List<Item> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/niveaux"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val niveauxList = mutableListOf<Item>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val id = jsonObject.getInt("id")
                val libelle = jsonObject.getString("libelle")
                val item = Item(id, listOf(libelle))
                niveauxList.add(item)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return niveauxList
    }

    fun getAdherentsList(): List<Item> {
        API_URL = "https://dev-restandroid.users.info.unicaen.fr/api/adherents"
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val adherentsList = mutableListOf<Item>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until 5) {
                val jsonObject = jsonArray.getJSONObject(i)
                val nom = jsonObject.getString("nom")
                val prenom = jsonObject.getString("prenom")
                val id = jsonObject.getInt("id")
                val item = Item(id,listOf(nom,prenom))
                adherentsList.add(item)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            connection.disconnect()
        }

        return adherentsList
    }

}
