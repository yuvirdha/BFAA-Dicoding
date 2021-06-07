package com.example.yuvirdhasubmission3bfaa.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yuvirdhasubmission3bfaa.FavoriteUser
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.adapter.FavAdapter
import com.example.yuvirdhasubmission3bfaa.database.FavHelper
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.MY_URI
import com.example.yuvirdhasubmission3bfaa.databinding.ActivityFavoriteBinding
import com.example.yuvirdhasubmission3bfaa.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(){

    private lateinit var adapter : FavAdapter
    private lateinit var binding : ActivityFavoriteBinding

    companion object{
        private const val EXTRA_FAVS = "extra_favs"
        val TAG: String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        //set title bar
        supportActionBar?.setTitle(R.string.favorite_page)

        // showing the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set binding
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //tampilkan recycler view
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)

        //connect recyclerview ke adapter
        adapter = FavAdapter(this)
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

            val helper = FavHelper.getInstance(applicationContext)

            //set progressbar to visible
            binding.pbFavorite.visibility = View.VISIBLE

            helper.open()

            val deferredFavs = async(Dispatchers.IO){

                //------------dengan helper--------------
                //val cursor = helper.queryAll()

                //------------dengan content resolver--------------
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
            helper.close()
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadFavoriteAsync()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}