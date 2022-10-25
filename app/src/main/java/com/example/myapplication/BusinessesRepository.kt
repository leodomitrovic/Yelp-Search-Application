package com.example.myapplication

import android.app.Application
import androidx.lifecycle.MutableLiveData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class BusinessesRepository(application: Application) {
    var dataset = ArrayList<Business>()

    fun getBusinesses(url: String): MutableLiveData<ArrayList<Business>> {
        var data = MutableLiveData<ArrayList<Business>>()
        val client = OkHttpClient()
        val url1 = url.replace(" ", "+")
        val request = Request.Builder().url("https://api.yelp.com/v3/businesses/search?" + url1 + "&limit=20&sort_by=distance")
            .header("Authorization", "Bearer 7zFBriJwVkajwWG8ITKbqUifiYZp6kCWDHpPJC3uintrcvQcSJl0VYdLQz9-uw-eapTYpfgS-MYM3QOItLuL_Mf2GFmV7zfZiPLpce4jec6E8WnsX8sWOodNHn9VY3Yx").build()
        println(request.url)
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
                    if (!businessjson.getString("name").equals("")) {
                        name = businessjson.getString("name")
                    }
                    val id = businessjson.getString("id")
                    val rate = businessjson.getString("rating")
                    val image = businessjson.getString("image_url")
                    var distance = businessjson.getString("distance")
                    val location = businessjson.getJSONObject("location")
                    var addr = ""
                    if (!location.isNull("address1")) {
                        addr = location.getString("address1") + ", "
                    }
                    var city = ""
                    if (!location.isNull("city")) {
                        city = location.getString("city") + ", "
                    }
                    var state = ""
                    if (!location.isNull("state")) {
                        state = location.getString("state") + ", "
                    }
                    var country = ""
                    if (!location.isNull("country")) {
                        country = location.getString("country")
                    }
                    val address = addr + city  + state + country
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