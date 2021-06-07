package com.example.yuvirdhasubmission3bfaa.activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.yuvirdhasubmission3bfaa.FavoriteUser
import com.example.yuvirdhasubmission3bfaa.GithubUser
import com.example.yuvirdhasubmission3bfaa.R
import com.example.yuvirdhasubmission3bfaa.adapter.SectionsPagerAdapter
import com.example.yuvirdhasubmission3bfaa.database.FavHelper
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.AVATAR
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.MY_URI
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.USERNAME
import com.example.yuvirdhasubmission3bfaa.databinding.ActivityProfileBinding
import com.example.yuvirdhasubmission3bfaa.helper.MappingHelper
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var bind: ActivityProfileBinding
    private var favs: FavoriteUser? = null

    private var favoriteTrue = false
    private lateinit var helper: FavHelper

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
        const val  EXTRA_USER = "extra_user"
        val TAG: String = ProfileActivity::class.java.simpleName
    }

    private var avatar: String = ""
    private lateinit var callData: GithubUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set to the layout
        setContentView(R.layout.activity_profile)

        //activate binding
        bind = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // showing the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set panggilData to parcelable (GithubUser)
        callData = intent?.getParcelableExtra<GithubUser>(EXTRA_GITHUB_USER)!!

        //open the database helper
        helper = FavHelper.getInstance(applicationContext)
        helper.open()

       //get the data based on fav
        favs = intent.getParcelableExtra(EXTRA_USER)
        if (favs != null){
            getSQL()
            favoriteTrue = true
            val loveUp: Int = R.drawable.baseline_favorite_white_36dp
            bind.loveButton.setImageResource(loveUp)
        }else{
            //show parsing JSON result by username
            panggilDataProfile(callData.username.toString())
        }

        bind.loveButton.setOnClickListener(this)

        //-------------------------------------------VIEWPAGER TO TABLAYOUT---------------------------------------

        //set panggilData
        val spAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        spAdapter.username = callData.username

        //set viewpager to adapter
        val vPager: ViewPager = findViewById(R.id.viewPager)
        vPager.adapter = spAdapter

        //set tablayout to viewpager
        val tLayout: TabLayout = findViewById(R.id.tabLayout)
        tLayout.setupWithViewPager(vPager)

        //----------------------------------------------SHOW PROFILE----------------------------------------------

        //show avatar
        Glide.with(this)
            .load(callData.avatar)
            .apply(RequestOptions().override(55, 55))
            .into(bind.imgItemA)

        supportActionBar?.elevation = 0f


        //-------------------------------------------FAVORITE BUTTON SETTINGS------------------------------------------

        //LOAD DATA BASED ON USERNAME --> cek apakah username sudah ada di list favorit atau belum
        GlobalScope.launch(Dispatchers.Main){
            val favHelper = FavHelper.getInstance(applicationContext)
            favHelper.open()

            val deferredFav = async(Dispatchers.IO){
                val myCursor = favHelper.queryByUsername(callData.username.toString())
                MappingHelper.mapCursorToArrayList(myCursor)
            }

            val data = deferredFav.await()

            if(data.size > 0){
                favoriteTrue = true
                bind.loveButton.setImageResource(R.drawable.baseline_favorite_white_36dp)
            } else {
                favoriteTrue = false
                bind.loveButton.setImageResource(R.drawable.baseline_favorite_border_white_36dp)
            }
        }
    }


    //get data from database
    private fun getSQL(){
        val favs = intent.getParcelableExtra<FavoriteUser>(EXTRA_USER)!!
        bind.tvUsername.text = favs.username.toString()
        Glide.with(this)
            .load(favs.avatar)
            .into(bind.imgItemA)
        avatar = favs.avatar.toString()
    }


    //-----------------------------------------------------------PARSING DETAIL-------------------------------------------------------------

    private fun panggilDataProfile(username: String) {

        //connect to progressbar
        val pbProfile: ProgressBar = findViewById(R.id.pbProfile)

        val client = AsyncHttpClient()

        //akses link dan token
        client.addHeader("Authorization", "token b23cb12f456b5cfd102bdb2fe5c54e1db8c603d1")
        client.addHeader("User-Agent", "request")
        val urlProfile = "https://api.github.com/users/$username"

        //cek status link
        Log.d(TAG, "entered to user link")

        //set username as title bar
        supportActionBar?.title = username

        //mulai parsing
        client.get(urlProfile, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?
            ) {
                //load data
                pbProfile.visibility = View.INVISIBLE

                val data = responseBody?.let { String(it) }
                try {

                    //panggil data yang akan di tampilkan ke profile
                    val jsonObject = JSONObject(data)
                    val tampilName = jsonObject.getString("name")
                    val tampilUsername = jsonObject.getString("login")
                    val tampilLoc = jsonObject.getString("location")
                    val tampilCompany = jsonObject.getString("company")
                    val tampilRepo = jsonObject.getString("public_repos")
                    val tampilFollowing = jsonObject.getString("following")
                    val tampilFollowers = jsonObject.getString("followers")

                    //----------------------------------------------------SET TO TEXTVIEW WITH BINDING--------------------------------------------------

                    bind.tvName.text = tampilName
                    bind.tvUsername.text = tampilUsername
                    bind.tvLocation.text = tampilLoc
                    bind.tvCompany.text = tampilCompany
                    bind.tvRepo.text = tampilRepo
                    bind.tvFollowers.text = tampilFollowers.toString()
                    bind.tvFollowing.text = tampilFollowing.toString()

                    //stop loading = data called
                    pbProfile.visibility = View.INVISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                if (error != null) { Log.d("GAGAL KONEKSI PROFILE", error.message.toString())}
            }
        })
    }

    //-----------------------------------------------FAVORITE BUTTON SETTINGS------------------------------------------

    override fun onClick(v: View){
        //set button
        val loveUp : Int = R.drawable.baseline_favorite_white_36dp
        val loveX : Int = R.drawable.baseline_favorite_border_white_36dp

        if (v.id == R.id.loveButton) {
            if(favoriteTrue) {

                //hapus data berdasarkan username
                helper.deleteByUsername(callData.username.toString())
                contentResolver.delete(MY_URI, null, null)

                Toast.makeText(this, "This user is deleted from your favorite list!", Toast.LENGTH_SHORT).show()

                //set button bila ditekan
                bind.loveButton.setImageResource(loveX)
                favoriteTrue = false
            }
            else{
                val uname = callData.username
                val avatar = callData.avatar

                Log.d("AVATAR MASUK", avatar.toString())

                val data = ContentValues()
                data.put(USERNAME, uname)
                data.put(AVATAR, avatar)

                favoriteTrue = true

                //----------------------dengan helper----------------
                //insert data to database
                //helper.insert(data)

                //-----------------dengan content resolver--------------
                contentResolver.insert(MY_URI, data)

                Toast.makeText(this, "This user is added to your favorite list!", Toast.LENGTH_SHORT).show()
                bind.loveButton.setImageResource(loveUp)
            }
        }
    }

    //create menu
    override fun onCreateOptionsMenu(main_menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu, main_menu)
        return super.onCreateOptionsMenu(main_menu)
    }

    // create the options
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.language) {
            val intentLanguage = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intentLanguage)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        helper.close()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
