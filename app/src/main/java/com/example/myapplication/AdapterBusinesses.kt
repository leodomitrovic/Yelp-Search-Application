package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.RecyclerviewIdemBusinessBinding


class AdapterBusinesses(private var activity: Activity, var businesses: ArrayList<Business>): RecyclerView.Adapter<AdapterBusinesses.ViewHolder>() {
    private lateinit var binding: RecyclerviewIdemBusinessBinding


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_idem_business, parent, false)
        binding = DataBindingUtil.bind(view)!!
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val business = businesses.get(position)
        binding.business = business
        binding.executePendingBindings()
        Glide.with(activity.applicationContext).load(business.photo).into(holder.itemView.findViewById(R.id.imageView5))
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                activity.startActivity(Intent(activity.applicationContext, DetailsActivity::class.java).putExtra("id", business.id))
            }
        })

    }

    override fun getItemCount(): Int {
        return businesses.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}