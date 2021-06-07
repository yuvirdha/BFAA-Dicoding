package com.example.yuvirdhasubmission3bfaa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuvirdhasubmission3bfaa.GithubUser
import com.example.yuvirdhasubmission3bfaa.activity.ProfileActivity
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.databinding.ListRecyclerViewBinding

class Adapter : RecyclerView.Adapter<Adapter.ListViewHolder>() {

    //set arraylist objek ke variabel gData
    private val gData = ArrayList<GithubUser>()

    fun setData(items: ArrayList<GithubUser>) {
        gData.clear()
        gData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(vg: ViewGroup, pos: Int): ListViewHolder {
        //set adapter ke layout
        val setView = LayoutInflater.from(vg.context).inflate(R.layout.list_recycler_view, vg, false)
        return ListViewHolder(setView)
    }

    override fun onBindViewHolder(listDataViewHolder: ListViewHolder, pos: Int) {
        listDataViewHolder.bind(gData[pos])
        listDataViewHolder.itemView.setOnClickListener {

            //set data ke clickUser sesuai posisi di GithubUser
            val clickUser: GithubUser = gData[pos]

            //masuk ke intent
            val pindahKeProfil = Intent(listDataViewHolder.itemView.context, ProfileActivity::class.java)

            //bawa data objek saat pindah ke profile
            pindahKeProfil.putExtra(ProfileActivity.EXTRA_GITHUB_USER, clickUser)

            //tampilkan activity profile
            listDataViewHolder.itemView.context.startActivity(pindahKeProfil)
        }
    }

    override fun getItemCount(): Int = gData.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //dekrasi variabel binding
        private val binding = ListRecyclerViewBinding.bind(itemView)

        fun bind(GithubUser: GithubUser) {
            //tampil avatar
            Glide.with(itemView.context)
                    .load(GithubUser.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.avatar)

            //tampil textView username
            binding.tvUsername.text = GithubUser.username
        }
    }
}