package com.example.consumerfavoriteapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerfavoriteapp.UserContract.UserColumns.Companion.MY_URI
import com.example.consumerfavoriteapp.databinding.ActivityMainFavoriteBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainFavoriteActivity : AppCompatActivity() {
    private lateinit var adapter : FavAdapter
    private lateinit var binding : ActivityMainFavoriteBinding

    companion object{
        const val EXTRA_FAVS = "extra_favs"
        val TAG: String = MainFavoriteActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_favorite)

        //set title bar
        supportActionBar?.setTitle(R.string.favorite_page)

        //set binding
        binding = ActivityMainFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tampilkan recycler view
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)

        //connect recyclerview ke adapter
        adapter = FavAdapter()
        binding.rvFavorite.adapter = adapter

        //handler thread --> observe data agar selalu update
        val ht = HandlerThread("DataObserver")
        ht.start()
        val my_h = Handler(ht.looper)
        val github = object : ContentObserver(my_h) {
            override fun onChange(self: Boolean) {
                loadFavoriteAsync()
            }
        }

        contentResolver.registerContentObserver(MY_URI, true, github)

        if (savedInstanceState == null) {
            loadFavoriteAsync()
        } else {
            val data = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_FAVS)
            if (data != null) {
                adapter.myFavoriteUser = data
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_FAVS, adapter.myFavoriteUser)
    }

    private fun loadFavoriteAsync(){
        GlobalScope.launch(Dispatchers.Main){
            //set progressbar to visible
            binding.pbFavorite.visibility = View.VISIBLE

            val deferredFavs = async(Dispatchers.IO){
                val cursor = contentResolver?.query(MY_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            //set progressbar to invisible
            binding.pbFavorite.visibility = View.INVISIBLE

            val favs = deferredFavs.await()
            if (favs.size > 0){
                adapter.myFavoriteUser = favs
            } else{
                adapter.myFavoriteUser = ArrayList()
                showSnackbarMessage("There's no data right now.")
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

}