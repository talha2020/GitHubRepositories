package com.example.githubrepositories

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import javax.inject.Inject

/**
 * I usually try to encapsulate the external libraries by my own interface. That way
 * we can swap out the internal implementation without effecting the consumers of that functionality.
 */
class ImageLoader @Inject constructor(private val activity: AppCompatActivity) {

    private val requestOptions = RequestOptions().circleCrop()

    fun loadImage(imageUrl: String, target: ImageView) {
        Glide.with(activity).load(imageUrl).apply(requestOptions).into(target)
    }
}