package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class BusinessesRepository() {
    var dataset = ArrayList<Business>()

    fun getBusinesses(url: String): MutableLiveData<ArrayList<Business>> {
        var data = MutableLiveData<ArrayList<Business>>()
        val client = OkHttpClient()
        val url1 = url.replace(" ", "+")
        val request = Request.Builder().url("https://api.yelp.com/v3/businesses/search?" + url1 + "&limit=20&sort_by=distance")
            .header("Authorization", "Bearer 7zFBriJwVkajwWG8ITKbqUifiYZp6kCWDHpPJC3uintrcvQcSJl0VYdLQz9-uw-eapTYpfgS-MYM3QOItLuL_Mf2GFmV7zfZiPLpce4jec6E8WnsX8sWOodNHn9VY3Yx").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                data.postValue(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val result = Objects.requireNonNull(response.body!!.string())
                val obj = JSONObject(result)
                val all = obj.getJSONArray("businesses")
                val size = all.length()
                var j = 0
                while (j < size) {
                    val businessjson = all.getJSONObject(j)
                    var name = ""
                    try {
                        if (!businessjson.isNull("name")) name = businessjson.getString("name")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    var id = ""
                    try {
                        if (!businessjson.isNull("id")) id = businessjson.getString("id")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    var rate = ""
                    try {
                        if (!businessjson.isNull("rating")) rate = businessjson.getString("rating")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    var image = ""
                    try {
                        if (!businessjson.isNull("image_url")) image = businessjson.getString("image_url")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    var distance = ""
                    try {
                        if (!businessjson.isNull("distance")) distance = businessjson.getString("distance")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    var address = ""
                    try {
                        val location = businessjson.getJSONObject("location")
                        var addr = ""
                        try {
                            if (!location.isNull("address1")) {
                                addr = location.getString("address1") + ", "
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        var city = ""
                        try {
                            if (!location.isNull("city")) {
                                city = location.getString("city") + ", "
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        var state = ""
                        try {
                            if (!location.isNull("state")) {
                                state = location.getString("state") + ", "
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        var country = ""
                        try {
                            if (!location.isNull("country")) {
                                country = location.getString("country")
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        address = addr + city  + state + country
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    val business = Business(id, name, image, "", "", address, "", rate, distance.toDouble())
                    dataset.add(business)
                    data.postValue(dataset)
                    j++
                }
            }
        })
        data.postValue(dataset)
        return data
    }
}