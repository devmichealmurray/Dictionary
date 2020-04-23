package com.devmmurray.dictionary

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Throws(IOException::class)
internal fun makeHTTPResponse(url: URL?): String? {
    val connection = url?.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Accept", "Application/json")
    connection.setRequestProperty("app_id", "b7bad206")
    connection.setRequestProperty("app_key", "e8df05526f8c300d873217566ebffc0d")

    return try {
        val inputStream = connection.inputStream
        if (connection.responseCode != HttpsURLConnection.HTTP_OK) {
            throw IOException("${connection.responseMessage} for $url")
        }
        if (inputStream != null) {
            convertStreamToString(inputStream)
        } else {
            "Error retreiving $url"
        }
    } finally {
        connection.disconnect()
    }
}

@Throws(IOException::class)
private fun convertStreamToString(inputStream: InputStream): String {
    val reader = BufferedReader(InputStreamReader(inputStream))
    val sb = StringBuilder()
    var line: String? = reader.readLine()
    while (line != null) {
        sb.append(line).append("\n")
        line = reader.readLine()
    }
    reader.close()
    return sb.toString()
}

internal fun extractJson(jsonResponse: String): Data? {
    var definition: Data? = null

    try {
        val jsonObject = JSONObject(jsonResponse)
        val jsonResults = jsonObject.getJSONArray("results")
        val firstResult = jsonResults.getJSONObject(0)
        val lexicalEntries = firstResult.getJSONArray("lexicalEntries")
        val firstLexEntry = lexicalEntries.getJSONObject(0)
        val entries = firstLexEntry.getJSONArray("entries")
        val firstEntry = entries.getJSONObject(0)
        val senses = firstEntry.getJSONArray("senses")
        val firstSense = senses.getJSONObject(0)
        val definitions = firstSense.getJSONArray("definitions")

        Log.d("ExtractJSON", definitions[0].toString())
        definition = Data(definitions[0].toString())

    } catch (e: JSONException) {
        e.printStackTrace()
        Log.e("extractJSON", "Error Parsing JSON")
        R.string.parsing_error
    }

    return definition
}