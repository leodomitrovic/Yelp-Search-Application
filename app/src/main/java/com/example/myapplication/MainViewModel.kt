package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class MainViewModel(application: Application): AndroidViewModel(application) {
    lateinit var businesses: MutableLiveData<ArrayList<Business>>
    private val repo = BusinessesRepository()

    fun init(url: String) {
        businesses = repo.getBusinesses(url)
    }

    fun getBusinesses(): LiveData<ArrayList<Business>> {
        return businesses
    }
}