package de.jenswangenheim.cleanarchitecture

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun getCustomProgressDrawable(ctx: Context): CircularProgressDrawable {
    return CircularProgressDrawable(ctx).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.ic_launcher_foreground)
    Glide.with(context).setDefaultRequestOptions(options).load(url).into(this)
}