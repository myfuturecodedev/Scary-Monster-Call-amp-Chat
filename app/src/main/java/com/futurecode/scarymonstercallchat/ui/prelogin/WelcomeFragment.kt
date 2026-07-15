package com.futurecode.scarymonstercallchat.ui.prelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.futurecode.scarymonstercallchat.R
import com.futurecode.scarymonstercallchat.activity.MainActivity
import com.futurecode.scarymonstercallchat.base.BaseFragment
import com.futurecode.scarymonstercallchat.databinding.FragmentWelcomeBinding

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            (activity as? MainActivity)?.goToMain()
        }

    }
}