package com.example.yuvirdhasubmission3bfaa.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yuvirdhasubmission3bfaa.GithubUser
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.adapter.Adapter
import com.example.yuvirdhasubmission3bfaa.databinding.ActivityMainBinding
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter
    private lateinit var binding: ActivityMainBinding

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set title bar
        supportActionBar?.setTitle(R.string.search)

        //set binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set adapter
        adapter = Adapter()
        adapter.notifyDataSetChanged()

        //panggil fungsi search
        cariUser()
    }

    private fun cariUser() {

        //connect to searchview
        val search : SearchView = findViewById(R.id.search)

        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(data: String): Boolean {
                if (data.isEmpty()) {
                    return true
                } else {
                    //panggil fungsi getDataCariUser
                    getDataCariUser(data)
                }
                return true
            }
            override fun onQueryTextChange(textChange: String): Boolean {
                return false
            }
        })
    }

    fun getDataCariUser(username: String?) {

        //connect to progressbar
        val pb : ProgressBar = findViewById(R.id.pb)

        //set progressbar to visible
        pb.visibility = View.VISIBLE

        val data = ArrayList<GithubUser>()
        val client = AsyncHttpClient()

        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token b23cb12f456b5cfd102bdb2fe5c54e1db8c603d1")

        //url endpoint
        val urlCariUser = "https://api.github.com/search/users?q=$username"
        Log.d(TAG, "entered to search link")

        client.get(urlCariUser, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()) {

                        val githubArray = item.getJSONObject(i)

                        //get data from GithubUser to tampilData
                        val tampilData = GithubUser()

                        //connect object ke API
                        tampilData.username = githubArray.getString("login")
                        tampilData.avatar = githubArray.getString("avatar_url")
                        data.add(tampilData)

                        Log.d(TAG, "connect data ke API search done")
                    }
                    //pasang ke adapter
                    adapter.setData(data)

                    //progressbar stop = username found
                    pb.visibility = View.INVISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                pb.visibility = View.INVISIBLE
                Log.d("GAGAL KONEKSI SEARCH", error.message.toString())
            }
        })

        //tampilkan recycler view
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //connect recyclerview ke adapter
        binding.recyclerView.adapter = adapter
    }

    //create menu
    override fun onCreateOptionsMenu(main_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_language, main_menu)
        return super.onCreateOptionsMenu(main_menu)
    }

    // create the options
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language) {
            val intentLanguage = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intentLanguage)
        }
        else if (item.itemId == R.id.favorite) {
            val intentFavorite = Intent(this, FavoriteActivity::class.java)
            startActivity(intentFavorite)
        }
        else if (item.itemId == R.id.alarm) {
            val intentAlarm = Intent(this, NotificationActivity::class.java)
            startActivity(intentAlarm)
        }
        return super.onOptionsItemSelected(item)
    }
}