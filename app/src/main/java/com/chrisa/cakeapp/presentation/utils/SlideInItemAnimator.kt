package com.chrisa.cakeapp.presentation.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class SlideUpItemAnimator : DefaultItemAnimator() {

    private val pendingAdds = ArrayList<RecyclerView.ViewHolder>()

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.initialiseAnimationValues()
        pendingAdds.add(holder)
        return true
    }

    override fun runPendingAnimations() {
        super.runPendingAnimations()
        if (pendingAdds.isNotEmpty()) {
            for (i in pendingAdds.indices.reversed()) {
                val holder = pendingAdds[i]
                holder.itemView.animate()
                    .alpha(1f)
                    .translationX(0f)
                    .translationY(0f)
                    .setDuration(250L)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            dispatchAddStarting(holder)
                        }

                        override fun onAnimationEnd(animation: Animator) {
                            animation.listeners.remove(this)
                            dispatchAddFinished(holder)
                            dispatchFinishedWhenDone()
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            holder.clearAnimatedValues()
                        }
                    }).interpolator = getLinearOutSlowInInterpolator(
                    holder.itemView.context
                )
                pendingAdds.removeAt(i)
            }
        }
    }

    private fun getLinearOutSlowInInterpolator(context: Context): TimeInterpolator {
        return AnimationUtils.loadInterpolator(context, android.R.interpolator.linear_out_slow_in)
    }

    override fun endAnimation(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate().cancel()
        if (pendingAdds.remove(holder)) {
            dispatchAddFinished(holder)
            holder.clearAnimatedValues()
        }
        super.endAnimation(holder)
    }

    override fun endAnimations() {
        for (i in pendingAdds.indices.reversed()) {
            val holder = pendingAdds[i]
            holder.clearAnimatedValues()
            dispatchAddFinished(holder)
            pendingAdds.removeAt(i)
        }
        super.endAnimations()
    }

    override fun isRunning(): Boolean {
        return pendingAdds.isNotEmpty() || super.isRunning()
    }

    private fun dispatchFinishedWhenDone() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    private fun RecyclerView.ViewHolder.initialiseAnimationValues() {
        with(itemView) {
            alpha = 0f
            translationY = (height * 0.4f)
        }
    }

    private fun RecyclerView.ViewHolder.clearAnimatedValues() {
        with(itemView) {
            alpha = 1f
            translationX = 0f
            translationY = 0f
        }
    }
}