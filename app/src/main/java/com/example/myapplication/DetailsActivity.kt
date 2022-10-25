package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var av: AdapterReviews? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        var rv = findViewById<RecyclerView>(R.id.ratings)
        rv.setLayoutManager(LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false))
        var model = ViewModelProvider(this).get(DetailsViewModel::class.java)
        model.init(intent.getStringExtra("id").toString())

        val businesObserver: Observer<Business> = object : Observer<Business> {
            override fun onChanged(business: Business) {
                binding = DataBindingUtil.bind(findViewById(R.id.root5))!!
                binding.business1 = business
                binding.executePendingBindings()
                Glide.with(applicationContext).load(business.photo).into(findViewById(R.id.imageView8))
            }
        }

        model.getBusinesses().observe(this, businesObserver)

        val reviewsObserver: Observer<ArrayList<Rating>> = object : Observer<ArrayList<Rating>> {
            override fun onChanged(reviews: ArrayList<Rating>) {
                var reviews1 = reviews
                if (reviews != null && av != null) {
                    if (reviews.size > 1) {
                        reviews1 = reviews[0].sorting(reviews)
                    }
                    av!!.reviews = reviews1
                    return
                }

                av = AdapterReviews(this@DetailsActivity, reviews1)
                rv!!.adapter = av
            }
        }
        model.getReviews().observe(this, reviewsObserver)
    }
}