package com.example.myapplication

import androidx.lifecycle.MutableLiveData
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.StringBuilder

class DetailsRepository() {
    var dataset = Business("", "", "", "", "", "", "", "", 0.0)
    var dataset_reviews = ArrayList<Rating>()

    fun getDetails(id: String): MutableLiveData<Business> {
        var data = MutableLiveData(Business("", "", "", "", "", "", "", "", 0.0))
        val client = OkHttpClient()
        val request = Request.Builder().url("https://api.yelp.com/v3/businesses/" + id)
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
                val all = JSONObject(result)
                var name = ""
                try {
                    if (!all.isNull("name")) name = all.getString("name")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                var image = ""
                try {
                    if (!all.isNull("image_url")) image = all.getString("image_url")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                var phone = ""
                try {
                    if (!all.isNull("phone")) phone = all.getString("phone")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                var rate = ""
                try {
                    if (!all.isNull("rating")) rate = all.getString("rating")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                var address = ""
                try {
                    val location = all.getJSONObject("location")
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
                    address = addr + city + state + country
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                var categories = ""
                try {
                    val categoriesList = all.getJSONArray("categories")
                    val size = categoriesList.length()
                    var j = 0
                    while (j < size) {
                        val category = categoriesList.getJSONObject(j)
                        if (j < size - 1) {
                            categories = categories.plus(category.getString("title") + ", ")
                        } else {
                            categories = categories.plus(category.getString("title"))
                        }
                        j++
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_WEEK) - 1
                var hours = ""
                try {
                    val pom = all.getJSONArray("hours")
                    if (pom.length() > 0) {
                        val workinghours = pom.getJSONObject(0).getJSONArray("open").getJSONObject(day)
                        val start = StringBuilder().append(workinghours.getString("start").subSequence(0, 2)).append(":").append(workinghours.getString("start").subSequence(2, 4)).toString()
                        val end = StringBuilder().append(workinghours.getString("end").subSequence(0, 2)).append(":").append(workinghours.getString("end").subSequence(2, 4)).toString()
                        hours = StringBuilder().append(start).append(" - ").append(end).toString()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val business = Business(id, name, image, categories, hours, address, phone, rate, 0.0)
                dataset = business
                data.postValue(dataset)
            }
        })
        return data
    }

    fun getReviews(id: String): MutableLiveData<ArrayList<Rating>> {
        var data = MutableLiveData(ArrayList<Rating>(0))
        val client = OkHttpClient()
        val request = Request.Builder().url("https://api.yelp.com/v3/businesses/" + id + "/reviews")
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
                val all = obj.getJSONArray("reviews")
                val size = all.length()
                var j = 0
                while (j < size) {
                    val businessjson = all.getJSONObject(j)
                    val name = businessjson.getJSONObject("user").getString("name")
                    val rate = businessjson.getString("rating")
                    var text = businessjson.getString("text")
                    var time = businessjson.getString("time_created")
                    var review = Rating(name, text, rate, time)
                    dataset_reviews.add(review)
                    data.postValue(dataset_reviews)
                    j++
                }
                println("Size " + size)
            }
        })
        return data
    }
}
