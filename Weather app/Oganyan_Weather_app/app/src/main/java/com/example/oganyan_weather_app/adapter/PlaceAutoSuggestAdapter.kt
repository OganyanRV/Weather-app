package com.example.oganyan_weather_app.adapter

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import com.example.android_auto_complete_location.models.PlaceApi

class PlaceAutoSuggestAdapter(context: Context, resId: Int) :
    ArrayAdapter<Any?>(context, resId), Filterable {
    var results: ArrayList<String>? = null
    var placeApi: PlaceApi = PlaceApi()


    override fun getCount(): Int {
        return results!!.size
    }

    override fun getItem(pos: Int): String? {
        return results!![pos]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    results = placeApi.autoComplete(constraint.toString())
                    filterResults.values = results
                    filterResults.count = results!!.size
                }
                return filterResults
            }

            protected override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
            ) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

}