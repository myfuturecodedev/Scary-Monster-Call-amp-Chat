package com.futurecode.scarymonstercallchat.utils

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.model.Monster

class RandomCallManager(
    private val notificationView: View, // The included layout view
    private val onAcceptCall: (Monster) -> Unit // Callback to tell the Fragment to navigate
) {
    private val handler = Handler(Looper.getMainLooper())
    private val context = notificationView.context

    // Complete list of monsters using localized string resources
    private val monsterList = listOf(
        Monster(name = context.getString(R.string.monster_freddy), imageResId = R.drawable.call1, videoCall = R.raw.call1),
        Monster(name = context.getString(R.string.monster_jason), imageResId = R.drawable.call2, videoCall = R.raw.call2),
        Monster(name = context.getString(R.string.monster_jeff_the), imageResId = R.drawable.call3, videoCall = R.raw.call3),
        Monster(name = context.getString(R.string.monster_joker), imageResId = R.drawable.call4, videoCall = R.raw.call4),
        Monster(name = context.getString(R.string.monster_pennywise), imageResId = R.drawable.call5, videoCall = R.raw.call5),
        Monster(name = context.getString(R.string.monster_scary_claus), imageResId = R.drawable.call6, videoCall = R.raw.call6),
        Monster(name = context.getString(R.string.monster_serbian), imageResId = R.drawable.call7, videoCall = R.raw.call7),
        Monster(name = context.getString(R.string.monster_the_closet), imageResId = R.drawable.call8, videoCall = R.raw.call8),
        Monster(name = context.getString(R.string.monster_the_upside), imageResId = R.drawable.call9, videoCall = R.raw.call9),
        Monster(name = context.getString(R.string.monster_the_nun), imageResId = R.drawable.call10, videoCall = R.raw.call10)
    )

    // Find the views inside the notification layout
    private val tvName: TextView = notificationView.findViewById(R.id.tvNotifName)
    private val ivAvatar: ImageView = notificationView.findViewById(R.id.ivNotifAvatar)
    private val btnAccept: ImageButton = notificationView.findViewById(R.id.btnNotifAccept)
    private val btnDecline: ImageButton = notificationView.findViewById(R.id.btnNotifDecline)

    fun startTimer() {
        // Random time between 10 seconds and 20 seconds
        val randomDelay = (10_000L..20_000L).random()
        handler.postDelayed({
            showIncomingCall()
        }, randomDelay)
    }

    private fun showIncomingCall() {
        val randomMonster = monsterList.random()

        // Update UI
        tvName.text = randomMonster.name
        ivAvatar.setImageResource(randomMonster.imageResId)

        // Button Clicks
        btnDecline.setOnClickListener {
            hideIncomingCall()
        }

        btnAccept.setOnClickListener {
            hideIncomingCall()
            // Trigger the callback so the Fragment can handle the navigation
            onAcceptCall(randomMonster)
        }

        // Slide down animation
        notificationView.apply {
            visibility = View.VISIBLE
            translationY = -500f
            animate()
                .translationY(0f)
                .setDuration(400)
                .start()
        }
    }

    private fun hideIncomingCall() {
        notificationView.animate()
            .translationY(-500f)
            .setDuration(300)
            .withEndAction {
                notificationView.visibility = View.GONE
            }
            .start()
    }

    fun stopTimer() {
        // Prevents memory leaks if the user leaves the screen
        handler.removeCallbacksAndMessages(null)
    }
}