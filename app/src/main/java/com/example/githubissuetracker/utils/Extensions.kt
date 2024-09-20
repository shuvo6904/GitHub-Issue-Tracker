package com.example.githubissuetracker.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.formatToDate(): String? {
    // Parse the date string
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Specify the time zone

    val date: Date? = inputFormat.parse(this)

    // Format to show only the date (e.g., 2024-09-19)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return date?.let { outputFormat.format(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun ImageView.loadImage(url: String?, drawable: Int) {
    Glide.with(context)
        .load(url)
        .placeholder(drawable)
        .skipMemoryCache(false)
        .into(this)
}