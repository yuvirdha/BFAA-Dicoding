package com.example.consumerfavoriteapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerfavoriteapp.databinding.ListRecyclerViewBinding

class FavAdapter() : RecyclerView.Adapter<FavAdapter.FavoriteViewHolder>() {

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
    }

    override fun getItemCount(): Int = this.myFavoriteUser.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListRecyclerViewBinding.bind(itemView)

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