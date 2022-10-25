package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailsViewModel(application: Application): AndroidViewModel(application) {
    lateinit var businesses: MutableLiveData<Business>
    private val repo = DetailsRepository(application)
    lateinit var reviews: MutableLiveData<ArrayList<Rating>>

    fun init(id: String) {
        businesses = repo.getDetails(id)
        reviews = repo.getReviews(id)
    }

    fun getBusinesses(): LiveData<Business> {
        return businesses
    }

    fun getReviews(): LiveData<ArrayList<Rating>> {
        return reviews
    }
}