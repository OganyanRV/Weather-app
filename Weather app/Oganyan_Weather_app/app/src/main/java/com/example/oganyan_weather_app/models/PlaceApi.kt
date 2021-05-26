package com.example.android_auto_complete_location.models

import android.util.Log
import com.example.oganyan_weather_app.BuildConfig
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class PlaceApi {

    // Put here your own api key [Google maps API]
    val API: String = BuildConfig.API_SEARCH

    fun autoComplete(input: String): ArrayList<String> {
        val arrayList: ArrayList<String> = ArrayList<String>()
        var connection: HttpURLConnection? = null
        val jsonResult = StringBuilder()
        val sb =
            StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?")
        sb.append("input=$input")
        sb.append("&key=$API")
        val url = URL(sb.toString())
        connection = url.openConnection() as HttpURLConnection
        val inputStreamReader =
            InputStreamReader(connection.inputStream)
        var read: Int
        val buff = CharArray(1024)
        while (inputStreamReader.read(buff).also { read = it } != -1) {
            jsonResult.append(buff, 0, read)
        }

        connection?.disconnect()

        val jsonObject = JSONObject(jsonResult.toString())
        val prediction = jsonObject.getJSONArray("predictions")
        for (i in 0 until prediction.length()) {
            arrayList.add(prediction.getJSONObject(i).getString("description"))
        }

        return arrayList
    }
}

