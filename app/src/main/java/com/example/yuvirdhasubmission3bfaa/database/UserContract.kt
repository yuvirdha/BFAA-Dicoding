package com.example.yuvirdhasubmission3bfaa.database

import android.net.Uri
import android.provider.BaseColumns

object UserContract {

    const val AUTHORITY = "com.example.yuvirdhasubmission3bfaa"
    const val SCHEME = "content"

   internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "fav"
            const val USERNAME = "username"
            const val AVATAR = "avatar"

            // untuk membuat URI content://com.example.yuvirdhasubmission3bfaa/favorite

            val MY_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}