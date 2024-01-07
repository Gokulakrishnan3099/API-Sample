package com.gokul.sample.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gokul.sample.R
import com.gokul.sample.data.model.ListModel
import com.gokul.sample.databinding.ListLayoutBinding
import com.squareup.picasso.Picasso

class ListAdapter(val list : List<ListModel.Data>, context: AppCompatActivity): RecyclerView.Adapter<com.gokul.sample.ui.adapter.ListAdapter.ViewHolder>() {

    class ViewHolder(val view: ListLayoutBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListLayoutBinding = ListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].apply {
            holder.view.apply {
                txtName.text = "$first_name $last_name"
                txtEmail.text = email

                Picasso.get()
                    .load(avatar)
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_24)
                    .into(imageView)
            }
        }

    }

}