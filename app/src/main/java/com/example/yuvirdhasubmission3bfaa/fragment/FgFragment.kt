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


class FgFragment : Fragment() {

    private lateinit var adapter: TablayoutAdapter

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
        val TAG = FgFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TablayoutAdapter()
        adapter.notifyDataSetChanged()

        //get data objek ke panggilData
        val panggilData = activity?.intent?.getParcelableExtra<GithubUser>(EXTRA_GITHUB_USER) as GithubUser

        //set panggilData ke username
        val username = panggilData.username

        //==================MULAI PARSING========================
        val data = ArrayList<GithubUser>()
        val client = AsyncHttpClient()

        client.addHeader("User-Agent", "request")
        client.addHeader("Authorization", "token b23cb12f456b5cfd102bdb2fe5c54e1db8c603d1")

        // url endpoint
        val urlFollowing = "https://api.github.com/users/$username/following"

        //cek status sudah masuk atau belum
        Log.d(TAG, "entered to the following link")

        //connect to progressbar
        val pbFg : ProgressBar = view.findViewById(R.id.pbFg)

        //mulai parsing
        client.get(urlFollowing, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray
            ) {
                val result = String(responseBody)
                val array = JSONArray(result)
                try {
                    for (i in 0 until array.length()) {

                        Log.d(TAG, "entered loop following")

                        val githubArray = array.getJSONObject(i)

                        //get data from GithubUser to tampilData
                        val tampilData = GithubUser()

                        //connect object ke API
                        tampilData.username = githubArray.getString("login")
                        tampilData.avatar = githubArray.getString("avatar_url")
                        data.add(tampilData)

                        Log.d(TAG, "connect data ke API FOLLOWING done")

                    }
                    //pasang ke adapter
                    adapter.setData(data)

                    //progressbar stop
                    pbFg.visibility = View.INVISIBLE

                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                        .show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                pbFg.visibility = View.INVISIBLE
                Log.d("GAGAL KONEKSI FOLLOWING", error.message.toString())
            }
        })

        //connect variabel ke id layout
        val fgRecyclerView : RecyclerView = view.findViewById(R.id.rvFg)

        //set recyclerview ke layout
        fgRecyclerView.layoutManager = LinearLayoutManager(activity)

        //set recyclerview ke adapter
        fgRecyclerView.adapter = adapter
    }
}

