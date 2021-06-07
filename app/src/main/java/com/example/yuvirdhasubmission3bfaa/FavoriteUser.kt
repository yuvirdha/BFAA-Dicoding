package com.example.yuvirdhasubmission3bfaa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteUser(
        var username: String? = "",
        var avatar: String? = ""
) : Parcelable