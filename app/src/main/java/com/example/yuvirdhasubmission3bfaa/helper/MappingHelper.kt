package com.example.yuvirdhasubmission3bfaa.helper

import android.database.Cursor
import com.example.yuvirdhasubmission3bfaa.FavoriteUser
import com.example.yuvirdhasubmission3bfaa.database.UserContract

object MappingHelper {

    fun mapCursorToArrayList(favsCursor: Cursor?): ArrayList<FavoriteUser> {
        val favList = ArrayList<FavoriteUser>()

        favsCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR))
                favList.add(FavoriteUser(username, avatar))
            }
        }
        return favList
    }


    fun mapCursorToObject(favsCursor: Cursor?): FavoriteUser{
        var favs = FavoriteUser()

        favsCursor?.apply{
            moveToFirst()
            val username = (getColumnIndexOrThrow(UserContract.UserColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.AVATAR))
        }
        return favs
    }

}