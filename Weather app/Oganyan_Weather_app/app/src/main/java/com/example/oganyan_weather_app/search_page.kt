package com.example.oganyan_weather_app

import android.R
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.oganyan_weather_app.adapter.PlaceAutoSuggestAdapter
import com.google.android.gms.maps.model.LatLng
import java.net.URL


class search_page : AppCompatActivity() {
    var was_error: Boolean = false
    var info: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.oganyan_weather_app.R.layout.search_page)
        supportActionBar?.hide()
        if (was_error == true) {
            Toast.makeText(
                applicationContext,
                "Try again!",
                Toast.LENGTH_SHORT
            ).show()
        }
        val autoCompleteTextView =
            findViewById<AutoCompleteTextView>(com.example.oganyan_weather_app.R.id.autocomplete)
        autoCompleteTextView.setAdapter(
            PlaceAutoSuggestAdapter(
                this,
                R.layout.simple_list_item_1
            )
        )
        autoCompleteTextView.onItemClickListener =
            OnItemClickListener { _, _, _, _ ->
                val latLng =
                    getLatLngFromAddress(autoCompleteTextView.text.toString())
                if (latLng != null) {
                    val address: Address? = getAddressFromLatLng(latLng)
                    if (address != null) {
                        info.clear()
                        if (address.locality != null) {
                            info.add(address.locality)
                        }
                        if (address.adminArea != null) {
                            info.add(address.adminArea)
                        }
                        if (address.subAdminArea != null) {
                            info.add(address.subAdminArea)
                        }
                        if (address.subLocality != null) {
                            info.add(address.subLocality)
                        }
                    }
                }
            }
        autoCompleteTextView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                HideMyKeyBoard()
                true
            } else {
                false
            }
        }
    }

    fun HideMyKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


    }

    private fun getLatLngFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this)
        val addressList: List<Address>?

        addressList = geocoder.getFromLocationName(address, 1)
        if (addressList != null) {
            val singleaddress: Address = addressList[0]
            return LatLng(singleaddress.getLatitude(), singleaddress.getLongitude())
        } else {
            return null
        }

    }

    private fun getAddressFromLatLng(latLng: LatLng): Address? {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5)
        return addresses?.get(0)

    }

    fun open_dialog(view: View) {
        val intent = Intent(this, weather_page::class.java)
        val info_str =
            findViewById<TextView>(com.example.oganyan_weather_app.R.id.autocomplete).text.toString()
        if (!info_str.equals("")) {
            intent.putExtra("city", info)
            intent.putExtra("error", was_error)
            startActivity(intent)
        } else {
            Toast.makeText(
                applicationContext,
                "Choose something!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    fun submit(view: View) {}
}
