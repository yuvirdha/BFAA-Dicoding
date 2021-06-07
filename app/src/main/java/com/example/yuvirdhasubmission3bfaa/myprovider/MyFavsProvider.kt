package com.example.yuvirdhasubmission3bfaa.myprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.example.yuvirdhasubmission3bfaa.activity.FavoriteActivity.Companion.TAG
import com.example.yuvirdhasubmission3bfaa.database.FavHelper
import com.example.yuvirdhasubmission3bfaa.database.UserContract.AUTHORITY
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.MY_URI
import com.example.yuvirdhasubmission3bfaa.database.UserContract.UserColumns.Companion.TABLE_NAME

class MyFavsProvider : ContentProvider() {

    companion object {
        private const val FAV = 1
        private const val FAV_ID = 2
        private lateinit var helper: FavHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAV_ID)
        }
    }

    override fun onCreate(): Boolean {
        helper = FavHelper.getInstance(context as Context)
        helper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAV -> helper.queryAll()
            FAV_ID -> helper.queryByUsername(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAV) {
            sUriMatcher.match(uri) -> helper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(MY_URI, null)

        return Uri.parse("$MY_URI/$added")
    }


    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> helper.update(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(MY_URI, null)

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (FAV_ID) {
            sUriMatcher.match(uri) -> helper.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(MY_URI, null)

        return deleted
    }

}