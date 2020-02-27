package com.ivzb.arch.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ShareCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ivzb.arch.R

fun navigationItemBackground(context: Context): Drawable? {
    // Need to inflate the drawable and CSL via AppCompatResources to work on Lollipop
    var background =
        AppCompatResources.getDrawable(context, R.drawable.navigation_item_background)
    if (background != null) {
        val tint = AppCompatResources.getColorStateList(
            context, R.color.navigation_item_background_tint
        )
        background = DrawableCompat.wrap(background.mutate())
        background.setTintList(tint)
    }
    return background
}

fun copy(activity: Activity, title: String, url: String) {
    val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText(title, url)
    clipboard.setPrimaryClip(clip)

    Toast.makeText(activity, "Link copied.", Toast.LENGTH_LONG).show()
}

fun share(activity: Activity, url: String) {
    ShareCompat.IntentBuilder.from(activity)
        .setType("text/plain")
        .setText(url)
        .setChooserTitle(R.string.a11y_share)
        .startChooser()
}
