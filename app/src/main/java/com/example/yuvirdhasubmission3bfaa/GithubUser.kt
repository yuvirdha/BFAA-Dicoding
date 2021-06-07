package com.example.yuvirdhasubmission3bfaa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
        var name: String? = "",
        var username: String? = "",
        var location: String? = "",
        var company: String? = "",
        var repository: String? = "",
        var following: String? = "",
        var followers: String? = "",
        var avatar: String? = "",
) : Parcelable