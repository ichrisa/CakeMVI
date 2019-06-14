package com.chrisa.cakeapp.presentation.cakes

import android.app.Activity
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.chrisa.cakeapp.data.api.cakes.Cake
import com.chrisa.cakeapp.presentation.utils.load

object CakeDialogHelper {

    fun show(activity: Activity, cake: Cake) {

        val interpolator = AnimationUtils.loadInterpolator(
            activity,
            android.R.interpolator.anticipate_overshoot
        )

        val view = LayoutInflater.from(activity).inflate(com.chrisa.cakeapp.R.layout.cake_dialog, null)

        val cakeImage: ImageView = view.findViewById(com.chrisa.cakeapp.R.id.cake_image)
        val cakeName: TextView = view.findViewById(com.chrisa.cakeapp.R.id.cake_name)
        val cakeDescription: TextView = view.findViewById(com.chrisa.cakeapp.R.id.cake_description)

        cakeImage.load(cake.imageUrl)
        cakeName.text = cake.title
        cakeDescription.text = cake.description

        val dialog = AlertDialog.Builder(activity)
            .setView(view)
            .create()

        val decor = dialog.window.decorView
        decor.alpha = 0f
        decor.translationY = activity.window.decorView.height * 0.4f

        dialog.show()

        decor.animate()
            .alpha(1.0f)
            .translationY(0.0f)
            .setDuration(800L)
            .setInterpolator(interpolator)
            .start()
    }
}