package com.example.mygithubrepo.app

import android.app.Application
import com.example.mygithubrepo.data.di.AppComponent
import com.example.mygithubrepo.data.di.ContextModule
import com.example.mygithubrepo.data.di.DaggerAppComponent


class App : Application() {

    val appComponent: AppComponent by lazy {
        initializeComponent(this)
    }

    private fun initializeComponent(application: Application): AppComponent {
        return DaggerAppComponent
            .builder()
            .contextModule(ContextModule(application))
            .build()
    }


}