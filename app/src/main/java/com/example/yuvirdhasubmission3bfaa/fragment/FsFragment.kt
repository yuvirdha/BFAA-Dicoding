package com.example.yuvirdhasubmission3bfaa.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yuvirdhasubmission3bfaa.GithubUser
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.adapter.TablayoutAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FsFragment : Fragment() {

    private lateinit var adapter: TablayoutAdapter

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
        val TAG = FsFragment::class.java.simpleName
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TablayoutAdapter()
        adapter.notifyDataSetChanged()

        //get data objek ke panggilData
        val panggilData = activity?.intent?.getParcelableExtra<GithubUser>(EXTRA_GITHUB_USER) as GithubUser

        //set panggilData ke username
        val username = panggilData.username

        //================PARSING JSON======================

        val data = ArrayList<GithubUser>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token b23cb12f456b5cfd102bdb2fe5c54e1db8c603d1")
        client.addHeader("User-Agent", "request")

        // url endpoint
        val urlFollowers = "https://api.github.com/users/$username/followers"

        //cek status dengan log sudah masuk atau belum
        Log.d(TAG, "entered to the followers link")

        //connect to progressbar
        val pbFs : ProgressBar = view.findViewById(R.id.pbFs)

        //mulai parsing
        client.get(urlFollowers, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                pbFs.visibility = View.INVISIBLE
                val result = String(responseBody)
                val array = JSONArray(result)
                try {

                    for (i in 0 until array.length()) {

                        Log.d(TAG, "entered loop followers")

                        val githubArray = array.getJSONObject(i)

                        //get data from GithubUser to tampilData
                        val tampilData = GithubUser()

                        //connect object ke API
                        tampilData.username = githubArray.getString("login")
                        tampilData.avatar = githubArray.getString("avatar_url")
                        data.add(tampilData)

                        Log.d(TAG, "connect data ke API done")

                    }
                    //pasang ke adapter
                    adapter.setData(data)

                    //progressbar stop
                    pbFs.visibility = View.INVISIBLE

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                pbFs.visibility = View.INVISIBLE
                Log.d("GAGAL KONEKSI FOLLOWERS", error.message.toString())
            }
        })

        //connect variabel ke id layout
        val fsRecyclerView : RecyclerView = view.findViewById(R.id.rvFs)

        //set recyclerview ke layout
        fsRecyclerView.layoutManager = LinearLayoutManager(activity)

        //set recyclerview ke adapter
        fsRecyclerView.adapter = adapter
    }
}