package com.example.myapplication

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.RecyclerviewItemReviewBinding


class AdapterReviews(private var activity: Activity, var reviews: ArrayList<Rating>): RecyclerView.Adapter<AdapterReviews.ViewHolder>() {
    private lateinit var binding: RecyclerviewItemReviewBinding

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_review, parent, false)
        binding = DataBindingUtil.bind(view)!!
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews.get(position)
        binding.review = review
        binding.executePendingBindings()

    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}