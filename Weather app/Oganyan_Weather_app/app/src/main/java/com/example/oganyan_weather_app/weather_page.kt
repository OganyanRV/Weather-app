package com.example.oganyan_weather_app

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.appcompat.widget.ResourceManagerInternal.get
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.EmptyCoroutineContext.get


class weather_page : AppCompatActivity() {
    var CITY: ArrayList<String> = ArrayList<String>()

    // Put here your own api key [Current Weather Data API]
    val API: String = BuildConfig.API_WEATHER

    val cont: Context = this
    var error: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_page)
        supportActionBar?.hide()
        CITY = intent.getSerializableExtra("city") as ArrayList<String>
        error = intent.getSerializableExtra("error") as Boolean


        weatherTask().execute()

    }

    fun reload_page(v: View) {
        weatherTask().execute()
    }


    inner class weatherTask() : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                val cur: String = CITY[0]
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$cur&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: IOException) {
                try {
                    val cur: String = CITY[1]
                    response =
                        URL("https://api.openweathermap.org/data/2.5/weather?q=$cur&units=metric&appid=$API").readText(
                            Charsets.UTF_8
                        )
                } catch (e: IOException) {
                    try {
                        val cur: String = CITY[2]
                        response =
                            URL("https://api.openweathermap.org/data/2.5/weather?q=$cur&units=metric&appid=$API").readText(
                                Charsets.UTF_8
                            )
                    } catch (e: IOException) {
                        try {
                            val cur: String = CITY[3]
                            response =
                                URL("https://api.openweathermap.org/data/2.5/weather?q=$cur&units=metric&appid=$API").readText(
                                    Charsets.UTF_8
                                )
                        } catch (e: IOException) {
                            val intent = Intent(cont, search_page::class.java)
                            intent.putExtra("error", true);
                            startActivity(intent)
                            return null
                        }

                    }
                }
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result == null) {
                error = true
                finish()
                return
            }
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            val wind = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
            val visib = jsonObj["visibility"]
//            val visib = jsonObj.getJSONObject("visibility")
            val updatedAt: Long = jsonObj.getLong("dt")
            val myLocale = Locale("ru", "RU")
            val updatedAtText =
                SimpleDateFormat("dd/MM/yyyy hh:mm a", myLocale).format(
                    Date(updatedAt * 1000)
                )

            val temp = main.getString("temp") + "°C"
            val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
            val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
            val feel = "Feels like " + main.getString("feels_like") + "°C"
            val pressure = main.getString("pressure") + " hPa"
            val humidity = main.getString("humidity") + " %"

            // val visibility = (visib.getLong(0)).toString() + " m"

            val sunrise: Long = sys.getLong("sunrise")
            val sunset: Long = sys.getLong("sunset")
            val windSpeed = wind.getString("speed") + " m/s"
            val weatherDescription = weather.getString("description")
            val icon = weather.getString("icon")

            val address = jsonObj.getString("name") + ", " + sys.getString("country")

            /* Populating extracted data into our views */
            findViewById<TextView>(R.id.address).text = address
            findViewById<TextView>(R.id.updated_at).text = updatedAtText
            findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
            findViewById<TextView>(R.id.temp).text = temp
            findViewById<TextView>(R.id.temp_min).text = tempMin
            findViewById<TextView>(R.id.temp_max).text = tempMax
            findViewById<TextView>(R.id.sunrise).text =
                SimpleDateFormat("hh:mm a", myLocale).format(Date(sunrise * 1000))
            findViewById<TextView>(R.id.sunset).text =
                SimpleDateFormat("hh:mm a", myLocale).format(Date(sunset * 1000))
            findViewById<TextView>(R.id.wind).text = windSpeed
            findViewById<TextView>(R.id.pressure).text = pressure
            findViewById<TextView>(R.id.humidity).text = humidity
            findViewById<TextView>(R.id.visibility).text = visib.toString() + " m"
            findViewById<TextView>(R.id.feels_like).text = feel
            val img = findViewById<ImageView>(R.id.weather_img)
            val str = "https://openweathermap.org/img/wn/$icon@2x.png"
            Picasso.with(this@weather_page).load(str).into(img)
            error = false
        }

    }
}



