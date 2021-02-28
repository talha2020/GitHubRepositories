package com.example.githubrepositories.activity

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {
    @Provides
    fun layoutInflater(activity: AppCompatActivity) = LayoutInflater.from(activity)
}