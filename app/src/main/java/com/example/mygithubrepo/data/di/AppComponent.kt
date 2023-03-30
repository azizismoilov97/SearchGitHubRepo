package com.example.mygithubrepo.data.di


import android.app.Application
import android.content.Context
import com.example.mygithubrepo.presenter.vm.ViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        ContextModule::class
    ]
)
interface AppComponent {
    fun context(): Context
    fun applicationContext(): Application
    fun viewModelsFactory(): ViewModelFactory

}
