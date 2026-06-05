package com.futurecode.scarymonstercallchat.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.futurecode.scarymonstercallchat.utils.ContextUtils
import com.futurecode.scarymonstercallchat.utils.PrefManager

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // 1. Read the saved language
        val prefManager = PrefManager.get(newBase)
        val languageCode = prefManager.selectedLanguage

        // 2. Wrap the context with the saved language
        val context = ContextUtils.updateLocale(newBase, languageCode)
        super.attachBaseContext(context)
    }
}