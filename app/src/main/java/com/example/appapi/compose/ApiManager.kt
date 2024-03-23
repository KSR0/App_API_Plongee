import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ApiManager {
    private const val API_URL = "https://dev-sae301grp2.users.info.unicaen.fr/api/memberList"
    private const val API_KEY = "39e6f6de81cb7155fd9d3cb7b800e661417d84f3bfd4ebfa47b7345adb94ee66"

    fun getMemberList(): MutableList<String> {
        val url = URL(API_URL)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("X-API-Key", API_KEY)

        val response = mutableListOf<String>()
        try {
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val jsonArray = JSONArray(inputStream.use { it.readText() })
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val nb = jsonObject.getString("nb")
                val name = jsonObject.getString("name")
                response.add("$nb: $name")
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
}
