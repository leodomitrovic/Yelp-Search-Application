package com.example.myapplication

class Rating(var name: String, var text: String, var rate: String, var time: String) {
    fun compare(b1: Rating, b2: Rating): Int {
        if (b1.time.compareTo(b2.time) < 0) return 1
        return 0
    }

    fun sorting(reviews: ArrayList<Rating>): ArrayList<Rating> {
        var i = 0
        var k = 0
        var j = 5

        while (i < reviews.size) {
            k = i
            j = i + 1
            while (j  < reviews.size)
            {
                if (compare(reviews.get(k), reviews.get(j)) == 1) k = j
                j++
            }
            val tmp = reviews[k]
            reviews[k] = reviews.get(i)
            reviews[i] = tmp
            i++
        }

        return reviews
    }
}