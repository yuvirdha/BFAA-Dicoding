package com.example.yuvirdhasubmission3bfaa.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuvirdhasubmission3bfaa.FavoriteUser
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.activity.DetailFavActivity
import com.example.yuvirdhasubmission3bfaa.activity.ProfileActivity
import com.example.yuvirdhasubmission3bfaa.databinding.ListRecyclerViewBinding

class FavAdapter(private val activity: Activity) : RecyclerView.Adapter<FavAdapter.FavoriteViewHolder>() {

    var myFavoriteUser = ArrayList<FavoriteUser>()
        set(MyFavsUser){
            if(myFavoriteUser.size > 0){
                this.myFavoriteUser.clear()
            }
            this.myFavoriteUser.addAll(myFavoriteUser)

            notifyDataSetChanged()
            field = MyFavsUser
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_recycler_view, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, pos: Int) {
        holder.bind(myFavoriteUser[pos])
        holder.itemView.setOnClickListener {

            //set data ke clickUser sesuai posisi di GithubUser
            val clickUser: FavoriteUser = myFavoriteUser[pos]

            //masuk ke intent
            val pindahKeProfil = Intent(holder.itemView.context, DetailFavActivity::class.java)

            //bawa data objek saat pindah ke profile
            pindahKeProfil.putExtra(ProfileActivity.EXTRA_GITHUB_USER, clickUser)

            //tampilkan activity profile
            holder.itemView.context.startActivity(pindahKeProfil)
        }
    }

    override fun getItemCount(): Int = this.myFavoriteUser.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListRecyclerViewBinding.bind(itemView)

        fun bind(favs: FavoriteUser) {
            //tampil avatar
            Glide.with(itemView.context)
                    .load(Uri.parse(favs.avatar))
                    .apply(RequestOptions().override(55, 55))
                    .into(binding.avatar)

            //tampil textView username
            binding.tvUsername.text = favs.username
        }
    }
}