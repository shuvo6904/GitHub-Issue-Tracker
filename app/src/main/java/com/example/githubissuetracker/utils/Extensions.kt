package com.example.githubissuetracker.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

inline fun <reified T> Context.openActivity(extras: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.extras()
    startActivity(intent)
}

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

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(name) as? T
    }
}

fun EditText.doAfterTextChangedFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    doAfterTextChanged {
        query.value = it.toString()
    }
    return query
}

fun ImageView.loadImage(url: String?, drawable: Int) {
    Glide.with(context)
        .load(url)
        .placeholder(drawable)
        .skipMemoryCache(false)
        .into(this)
}