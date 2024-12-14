package com.patpet.qiu

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.patpet.qiu.databinding.DialogUpdateUserBinding

class UpdateUserDialogFragment : DialogFragment() {

    private var listener: ((User) -> Unit)? = null

    private lateinit var binding: DialogUpdateUserBinding
    private lateinit var user: User

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogUpdateUserBinding.inflate(LayoutInflater.from(context))
        user = arguments?.getParcelable("user_data")!!

        // 初始化对话框的字段
        binding.editUsername.setText(user.username)
        binding.editPhoneNumber.setText(user.phoneNumber)
        binding.editRole.setText(user.role)
        binding.editGender.setText(user.gender)
        binding.editAge.setText(user.age.toString())
        binding.editAddress.setText(user.address)

        return AlertDialog.Builder(requireContext())
            .setTitle("Update User")
            .setView(binding.root)
            .setPositiveButton("Update") { _, _ ->
                // 更新用户信息
                user.username = binding.editUsername.text.toString()
                user.phoneNumber = binding.editPhoneNumber.text.toString()
                user.role = binding.editRole.text.toString()
                user.gender = binding.editGender.text.toString()
                user.age = binding.editAge.text.toString().toInt()
                user.address = binding.editAddress.text.toString()
                listener?.invoke(user)
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    fun setOnUpdateListener(callback: (User) -> Unit) {
        listener = callback
    }

    companion object {
        fun newInstance(user: User): UpdateUserDialogFragment {
            val fragment = UpdateUserDialogFragment()
            val bundle = Bundle()
            bundle.putParcelable("user_data", user)
            fragment.arguments = bundle
            return fragment
        }
    }
}
