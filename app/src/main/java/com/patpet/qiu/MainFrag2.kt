package com.patpet.qiu

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class MainFrag2 : Fragment(R.layout.main_frag_02) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = view.findViewById<Button>(R.id.return_button_main_02)
        backButton.setOnClickListener {
            (activity as? MainActivity)?.onBackPressed()
        }
    }

    companion object {
        // 工厂方法，用于创建 Fragment 实例并传递参数
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFrag2().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }

}