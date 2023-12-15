import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

class HttpUtils {
    private val client = OkHttpClient()

    // Function to make a GET request
    fun doGet(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.body.string()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Function to make a POST request
    fun doPost(url: String, jsonBody: String): String? {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = RequestBody.create(mediaType, jsonBody)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.body.string()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Function to make a POST request
    fun doDelete(url: String, jsonBody: String): String? {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = RequestBody.create(mediaType, jsonBody)

        val request = Request.Builder()
            .url(url)
            .delete(requestBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.body.string()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
