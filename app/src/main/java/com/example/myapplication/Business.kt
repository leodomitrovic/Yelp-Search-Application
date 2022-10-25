package com.example.myapplication

class Business(var id: String, var name: String, var photo: String, var categories: String, var hours: String, var address: String, var contact: String, var rate: String, var distance: Double) {

    fun compare(b1: Business, b2: Business): Int {
        if (b1.distance == b2.distance) {
            if (b1.rate.toDouble() < b2.rate.toDouble()) return 1
        }
        return 0
    }

    fun sorting(businesses: ArrayList<Business>): ArrayList<Business> {
        var i = 0
        var k = 0
        var j = 5

        while (i < businesses.size) {
            k = i
            j = i + 1
            while (j  < businesses.size)
            {
                if (compare(businesses.get(k), businesses.get(j)) == 1) k = j
                j++
            }
            val tmp = businesses.get(k)
            businesses[k] = businesses.get(i)
            businesses[i] = tmp
            i++
        }

        return businesses
    }
}