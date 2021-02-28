package com.example.githubrepositories

import android.app.Application
import com.example.githubrepositories.app.AppComponent
import com.example.githubrepositories.app.AppModule
import com.example.githubrepositories.app.DaggerAppComponent

class GithubRepositoriesApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}