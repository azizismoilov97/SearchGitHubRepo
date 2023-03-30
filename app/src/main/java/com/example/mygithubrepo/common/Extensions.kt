package com.example.mygithubrepo.common

import androidx.fragment.app.Fragment
import com.example.mygithubrepo.app.App
import com.example.mygithubrepo.data.di.AppComponent
import retrofit2.HttpException

fun HttpException.getRetryTime(): String?{
    return response()?.let {
        val headers = it.headers()
        Utils.convertEpoc(headers["X-RateLimit-Reset"]!!)
    }

}

fun Fragment.getAppComponent(): AppComponent =
    (requireContext().applicationContext as App).appComponent