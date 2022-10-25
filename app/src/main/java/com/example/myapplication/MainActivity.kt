package com.example.myapplication

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private var ab: AdapterBusinesses? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)
        var rv = findViewById<RecyclerView>(R.id.rv2)
        rv.setLayoutManager(LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false))
        var search = findViewById<ImageView>(R.id.imageView4)
        var term = findViewById<EditText>(R.id.editTextTextPersonName2)
        var location = findViewById<EditText>(R.id.editTextTextPersonName3)
        var value_term = ""
        var value_location = ""
        var model = ViewModelProvider(this).get(MainViewModel::class.java)

        search.setOnClickListener {
            if (term.text.isNotEmpty() && location.text.isEmpty()) {
                Toast.makeText(this@MainActivity, "Location is required", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                if (term.text.toString() != value_term || location.text.toString() != value_location) {
                    rv = findViewById(R.id.rv2)
                    rv.setLayoutManager(LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false))
                    value_term = term.text.toString()
                    value_location = location.text.toString()
                    model.businesses = MutableLiveData()
                    model = ViewModelProvider(this).get(MainViewModel::class.java)
                    model.init("term=" + value_term + "&location=" + value_location)
                    if (ab != null) {
                        var i = 0
                        while (i < ab!!.businesses.size) {
                            ab!!.businesses.removeAt(i)
                            ab!!.notifyItemRemoved(i)
                        }
                    }

                    val businessesObserver: Observer<ArrayList<Business>> = object : Observer<ArrayList<Business>> {
                        override fun onChanged(businesses: ArrayList<Business>) {
                            if (businesses.size < 20) return
                            if(ab == null) {
                                ab = AdapterBusinesses(this@MainActivity, businesses.get(0).sorting(businesses))
                                rv!!.adapter = ab
                            }
                            ab!!.businesses = businesses
                            ab!!.notifyDataSetChanged()
                            rv!!.adapter = ab
                        }
                    }
                    model.getBusinesses().observe(this, businessesObserver)
                }
            }
        }
    }
}