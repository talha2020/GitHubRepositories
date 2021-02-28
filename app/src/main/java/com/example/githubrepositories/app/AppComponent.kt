package com.example.githubrepositories.app

import com.example.githubrepositories.activity.ActivityComponent
import com.example.githubrepositories.coroutines.CoroutinesModule
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, CoroutinesModule::class])
interface AppComponent {
    fun newActivityComponentBuilder(): ActivityComponent.Builder
}