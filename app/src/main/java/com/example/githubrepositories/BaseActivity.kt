package com.example.githubrepositories

import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    // Dependency injection
    private val appComponent get() = (application as GithubRepositoriesApplication).appComponent
    private val presentationComponent by lazy {
        appComponent.newActivityComponentBuilder()
            .activity(this)
            .build()
    }

    protected val injector get() = presentationComponent

}