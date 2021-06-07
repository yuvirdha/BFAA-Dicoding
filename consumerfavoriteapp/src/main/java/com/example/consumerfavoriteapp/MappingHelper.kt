package com.example.consumerfavoriteapp

import android.database.Cursor

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